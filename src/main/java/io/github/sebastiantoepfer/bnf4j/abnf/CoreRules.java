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
package io.github.sebastiantoepfer.bnf4j.abnf;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Alternative;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Concatenation;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Dimension;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.NumericCharacter;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.SequenceGroup;
import io.github.sebastiantoepfer.bnf4j.abnf.element.StringElement;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValidateableCodePoint;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValueRangeAlternatives;
import io.github.sebastiantoepfer.bnf4j.abnf.element.VariableRepetition;
import io.github.sebastiantoepfer.ddd.common.Media;

public enum CoreRules implements Element {
    ALPHA() {
        @Override
        Element definition() {
            return Alternative.of(
                ValueRangeAlternatives.of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x41),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x5A)
                ),
                ValueRangeAlternatives.of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x61),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7A)
                )
            );
        }
    },
    BIT() {
        @Override
        Element definition() {
            return Alternative.of(StringElement.of("0"), StringElement.of("1"));
        }
    },
    CHAR() {
        @Override
        Element definition() {
            return ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x01),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7F)
            );
        }
    },
    CR() {
        @Override
        Element definition() {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x0D);
        }
    },
    CRLF() {
        @Override
        Element definition() {
            return Concatenation.of(CR, LF);
        }
    },
    CTL() {
        @Override
        Element definition() {
            return Alternative.of(
                ValueRangeAlternatives.of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x00),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x1F)
                ),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7F)
            );
        }
    },
    DIGIT() {
        @Override
        Element definition() {
            return ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x30),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x39)
            );
        }
    },
    DQUOTE() {
        @Override
        Element definition() {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x22);
        }
    },
    HEXDIG() {
        @Override
        Element definition() {
            return Alternative.of(
                DIGIT,
                StringElement.of("A"),
                StringElement.of("B"),
                StringElement.of("C"),
                StringElement.of("D"),
                StringElement.of("E"),
                StringElement.of("F")
            );
        }
    },
    HTAB() {
        @Override
        Element definition() {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x09);
        }
    },
    LF() {
        @Override
        Element definition() {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x0A);
        }
    },
    LWSP() {
        @Override
        Element definition() {
            return VariableRepetition.of(SequenceGroup.of(Concatenation.of(Alternative.of(WSP, CRLF), WSP)));
        }
    },
    OCTET() {
        @Override
        Element definition() {
            return ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x00),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0xFF)
            );
        }
    },
    SP() {
        @Override
        Element definition() {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x20);
        }
    },
    VCHAR() {
        @Override
        Element definition() {
            return ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x21),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7E)
            );
        }
    },
    WSP {
        @Override
        Element definition() {
            return Alternative.of(SP, HTAB);
        }
    };

    public final RuleName asRuleName() {
        return RuleName.of(name());
    }

    @Override
    public final <T extends Media<T>> T printOn(final T media) {
        return Rule.of(asRuleName(), definition()).printOn(media).withValue("type", "corerule");
    }

    public final boolean isValidFor(final int codePoint) {
        return isValidFor(ValidateableCodePoint.of(0, codePoint));
    }

    @Override
    public final boolean isValidFor(final ValidateableCodePoint codePoint) {
        return definition().isValidFor(codePoint);
    }

    @Override
    public final Dimension dimension() {
        return definition().dimension();
    }

    abstract Element definition();
}
