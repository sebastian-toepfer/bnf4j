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

import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.EQUALS_SIGN;

import io.github.sebastiantoepfer.bnf4j.abnf.Rule;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import java.util.Objects;

final class RuleExtractor implements Extractor, ExtractorOwner {

    public static Extractor of(final ExtractorOwner owner) {
        return new RuleExtractor(owner);
    }

    private final ExtractorOwner owner;

    private RuleExtractor(final ExtractorOwner owner) {
        this.owner = Objects.requireNonNull(owner);
    }

    @Override
    public Extractor append(final int codePoint) {
        final Extractor result;
        if (Character.isAlphabetic(codePoint)) {
            result = RuleNameExtractor.of(this).append(codePoint);
        } else {
            result = this;
        }
        return result;
    }

    @Override
    public Extractor imDone(final Creator creator) {
        return new DefinedAsExtractor(owner, creator.createAs(RuleName.class));
    }

    private static class DefinedAsExtractor implements Extractor, ExtractorOwner {

        private final ExtractorOwner owner;
        private final RuleName ruleName;

        public DefinedAsExtractor(final ExtractorOwner owner, final RuleName ruleName) {
            this.owner = Objects.requireNonNull(owner);
            this.ruleName = Objects.requireNonNull(ruleName);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (codePoint == EQUALS_SIGN) {
                result = ConcatenationExtractor.of(this);
            } else {
                result = this;
            }
            return result;
        }

        @Override
        public Extractor imDone(final Creator creator) {
            return owner.imDone(
                new Creator() {
                    @Override
                    public <T> T createAs(final Class<T> cls) {
                        return cls.cast(Rule.of(ruleName, creator.createAs(Element.class)));
                    }
                }
            );
        }
    }
}
