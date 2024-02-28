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

import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.ASTERISK;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.SpecificRepetition;
import io.github.sebastiantoepfer.bnf4j.abnf.element.VariableRepetition;
import java.util.Objects;

class RepetitionExtractor implements Extractor {

    static Extractor of(final ExtractorOwner owner) {
        return new RepetitionExtractor(owner);
    }

    private final ExtractorOwner owner;

    private RepetitionExtractor(final ExtractorOwner owner) {
        this.owner = Objects.requireNonNull(owner);
    }

    @Override
    public Extractor append(final int codePoint) {
        Extractor result;
        if (Character.isDigit(codePoint)) {
            result = new SpecificRepetitionExtractor(owner, Character.toString(codePoint));
        } else if (codePoint == ASTERISK) {
            result = new UnspecificRepetitionExtractor(owner);
        } else {
            result = ElementExtractor.of(owner).append(codePoint);
        }
        return result;
    }

    private static class SpecificRepetitionExtractor implements Extractor, ExtractorOwner {

        private final ExtractorOwner owner;
        private final String repeat;

        public SpecificRepetitionExtractor(final ExtractorOwner owner, final String repeat) {
            this.owner = Objects.requireNonNull(owner);
            this.repeat = Objects.requireNonNull(repeat);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (codePoint == ASTERISK) {
                result = new AtLeastRepetitionExtractor(owner, repeat);
            } else if (Character.isDigit(codePoint)) {
                result = new SpecificRepetitionExtractor(owner, repeat.concat(Character.toString(codePoint)));
            } else {
                result = ElementExtractor.of(this).append(codePoint);
            }
            return result;
        }

        @Override
        public Extractor imDone(final Creator creator) {
            return owner.imDone(
                new Creator() {
                    @Override
                    public <T> T createAs(final Class<T> cls) {
                        return cls.cast(
                            SpecificRepetition.of(creator.createAs(Element.class), Integer.parseInt(repeat))
                        );
                    }
                }
            );
        }
    }

    private static class AtLeastRepetitionExtractor implements Extractor, ExtractorOwner {

        private final ExtractorOwner owner;
        private final String minRepeat;

        private AtLeastRepetitionExtractor(final ExtractorOwner owner, final String minRepeat) {
            this.owner = Objects.requireNonNull(owner);
            this.minRepeat = Objects.requireNonNull(minRepeat);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (Character.isDigit(codePoint)) {
                result = new BetweenRepetitionExtractor(owner, minRepeat, Character.toString(codePoint));
            } else {
                result = ElementExtractor.of(this).append(codePoint);
            }
            return result;
        }

        @Override
        public Extractor imDone(final Creator creator) {
            return owner.imDone(
                new Creator() {
                    @Override
                    public <T> T createAs(final Class<T> cls) {
                        return cls.cast(
                            VariableRepetition.ofAtLeast(creator.createAs(Element.class), Integer.parseInt(minRepeat))
                        );
                    }
                }
            );
        }
    }

    private static class UnspecificRepetitionExtractor implements Extractor, ExtractorOwner {

        private final ExtractorOwner owner;

        public UnspecificRepetitionExtractor(final ExtractorOwner owner) {
            this.owner = Objects.requireNonNull(owner);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (Character.isDigit(codePoint)) {
                result = new AtMostRepetitionExtractor(owner, Character.toString(codePoint));
            } else {
                result = ElementExtractor.of(this).append(codePoint);
            }
            return result;
        }

        @Override
        public Extractor imDone(final Creator creator) {
            return owner.imDone(
                new Creator() {
                    @Override
                    public <T> T createAs(final Class<T> cls) {
                        return cls.cast(VariableRepetition.of(creator.createAs(Element.class)));
                    }
                }
            );
        }
    }

    private static class AtMostRepetitionExtractor implements Extractor, ExtractorOwner {

        private final ExtractorOwner owner;
        private final String maxRepeat;

        public AtMostRepetitionExtractor(final ExtractorOwner owner, final String maxRepeat) {
            this.owner = Objects.requireNonNull(owner);
            this.maxRepeat = Objects.requireNonNull(maxRepeat);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (Character.isDigit(codePoint)) {
                result = new AtMostRepetitionExtractor(owner, maxRepeat.concat(Character.toString(codePoint)));
            } else {
                result = ElementExtractor.of(this).append(codePoint);
            }
            return result;
        }

        @Override
        public Extractor imDone(final Creator creator) {
            return owner.imDone(
                new Creator() {
                    @Override
                    public <T> T createAs(final Class<T> cls) {
                        return cls.cast(
                            VariableRepetition.ofAtMost(creator.createAs(Element.class), Integer.parseInt(maxRepeat))
                        );
                    }
                }
            );
        }
    }

    private static class BetweenRepetitionExtractor implements Extractor, ExtractorOwner {

        private final ExtractorOwner owner;
        private final String minRepeat;
        private final String maxRepeat;

        public BetweenRepetitionExtractor(final ExtractorOwner owner, final String minRepeat, final String maxRepeat) {
            this.owner = Objects.requireNonNull(owner);
            this.minRepeat = Objects.requireNonNull(minRepeat);
            this.maxRepeat = Objects.requireNonNull(maxRepeat);
        }

        @Override
        public Extractor append(final int codePoint) {
            final Extractor result;
            if (Character.isDigit(codePoint)) {
                result = new BetweenRepetitionExtractor(
                    owner,
                    minRepeat,
                    maxRepeat.concat(Character.toString(codePoint))
                );
            } else {
                result = ElementExtractor.of(this).append(codePoint);
            }
            return result;
        }

        @Override
        public Extractor imDone(final Creator creator) {
            return owner.imDone(
                new Creator() {
                    @Override
                    public <T> T createAs(final Class<T> cls) {
                        return cls.cast(
                            VariableRepetition.ofBetween(
                                creator.createAs(Element.class),
                                Integer.parseInt(minRepeat),
                                Integer.parseInt(maxRepeat)
                            )
                        );
                    }
                }
            );
        }
    }
}
