/*
 * The MIT License
 *
 * Copyright 2023 sebastian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.sebastiantoepfer.bnf4j.abnf.importation;

import static io.github.sebastiantoepfer.bnf4j.abnf.element.NumericCharacter.BASE.BINARY;
import static io.github.sebastiantoepfer.bnf4j.abnf.element.NumericCharacter.BASE.DECIMAL;
import static io.github.sebastiantoepfer.bnf4j.abnf.element.NumericCharacter.BASE.HEXADECIMAL;

import io.github.sebastiantoepfer.bnf4j.abnf.CoreRules;
import io.github.sebastiantoepfer.bnf4j.abnf.element.NumericCharacter;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValidateableCodePoint;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValueRangeAlternatives;
import java.util.Objects;

class NumValExtractor implements Extractor {

    static Extractor of(final ExtractorOwner owner) {
        return new NumValExtractor(owner);
    }

    private final ExtractorOwner owner;

    private NumValExtractor(final ExtractorOwner owner) {
        this.owner = Objects.requireNonNull(owner);
    }

    @Override
    public Extractor append(final int codePoint) {
        final NumericCharacter.BASE base = NumericCharacter.BASE.findByShortName(Character.toChars(codePoint)[0]);
        final CoreRules rule = switch (base) {
            case BINARY -> CoreRules.BIT;
            case DECIMAL -> CoreRules.DIGIT;
            case HEXADECIMAL -> CoreRules.HEXDIG;
            default -> throw new AssertionError();
        };
        return new SpecificNumValExtractor(owner, base, rule);
    }

    private static class SpecificNumValExtractor implements Extractor {

        private final ExtractorOwner owner;
        private final NumericCharacter.BASE base;
        private final CoreRules rule;
        private final String value;

        public SpecificNumValExtractor(
            final ExtractorOwner owner,
            final NumericCharacter.BASE base,
            final CoreRules rule
        ) {
            this(owner, base, rule, "0");
        }

        public SpecificNumValExtractor(
            final ExtractorOwner owner,
            final NumericCharacter.BASE base,
            final CoreRules rule,
            final String value
        ) {
            this.owner = Objects.requireNonNull(owner);
            this.base = Objects.requireNonNull(base);
            this.rule = Objects.requireNonNull(rule);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (codePoint == UsefulCodepoints.HYPHEN_MINUS) {
                result = new ValueRangeExtractor(owner, base, rule, value);
            } else if (Character.isWhitespace(codePoint) || !rule.isValidFor(ValidateableCodePoint.of(0, codePoint))) {
                result = owner.imDone(asCreator()).append(codePoint);
            } else {
                result = new SpecificNumValExtractor(owner, base, rule, value.concat(Character.toString(codePoint)));
            }
            return result;
        }

        @Override
        public Creator finish() {
            return owner.imDone(asCreator()).finish();
        }

        private Creator asCreator() {
            return new Creator() {
                @Override
                public <T> T createAs(final Class<T> cls) {
                    return cls.cast(NumericCharacter.of(base, base.convert(value)));
                }
            };
        }
    }

    private static class ValueRangeExtractor implements Extractor {

        private final ExtractorOwner owner;
        private final NumericCharacter.BASE base;
        private final CoreRules rule;
        private final String start;
        private final String end;

        public ValueRangeExtractor(
            final ExtractorOwner owner,
            final NumericCharacter.BASE base,
            final CoreRules rule,
            final String start
        ) {
            this(owner, base, rule, start, "0");
        }

        private ValueRangeExtractor(
            final ExtractorOwner owner,
            final NumericCharacter.BASE base,
            final CoreRules rule,
            final String start,
            final String end
        ) {
            this.owner = Objects.requireNonNull(owner);
            this.base = Objects.requireNonNull(base);
            this.rule = Objects.requireNonNull(rule);
            this.start = Objects.requireNonNull(start);
            this.end = Objects.requireNonNull(end);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (Character.isWhitespace(codePoint) || !rule.isValidFor(ValidateableCodePoint.of(0, codePoint))) {
                result = owner.imDone(asCreator()).append(codePoint);
            } else {
                result = new ValueRangeExtractor(owner, base, rule, start, end.concat(Character.toString(codePoint)));
            }
            return result;
        }

        @Override
        public Creator finish() {
            return owner.imDone(asCreator()).finish();
        }

        private Creator asCreator() {
            return new Creator() {
                @Override
                public <T> T createAs(final Class<T> cls) {
                    return cls.cast(
                        ValueRangeAlternatives.of(
                            NumericCharacter.of(base, base.convert(start)),
                            NumericCharacter.of(base, base.convert(end))
                        )
                    );
                }
            };
        }
    }
}
