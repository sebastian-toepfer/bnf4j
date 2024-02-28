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
package io.github.sebastiantoepfer.bnf4j.abnf.element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class ConcatenationTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Concatenation.class).verify();
    }

    @Test
    void should_return_dimension_as_sum() {
        assertThat(
            Concatenation.of(
                Alternative.of(StringElement.of("ab"), StringElement.of("cde")),
                Alternative.of(StringElement.of("x"), StringElement.of("yz"))
            ).dimension(),
            is(Dimension.of(3, 5))
        );
    }

    @Test
    void should_be_valid_if_codepoint_is_equals_codepoint_at_position() {
        final Concatenation concatenation = Concatenation.of(
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'a'),
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'b'),
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'c')
        );
        assertThat(concatenation.isValidFor(ValidateableCodePoint.of(0, 'a')), is(true));
        assertThat(concatenation.isValidFor(ValidateableCodePoint.of(1, 'b')), is(true));
        assertThat(concatenation.isValidFor(ValidateableCodePoint.of(2, 'c')), is(true));
    }

    @Test
    void should_be_invalid_if_codepoint_is_out_of_position() {
        final Concatenation concatenation = Concatenation.of(
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'a'),
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'b'),
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'c')
        );
        assertThat(concatenation.isValidFor(ValidateableCodePoint.of(3, 'c')), is(false));
    }

    @Test
    void should_be_invalid_if_codepoint_is_notequals_codepoint_at_position() {
        final Concatenation concatenation = Concatenation.of(
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'a'),
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'b'),
            NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'c')
        );
        assertThat(concatenation.isValidFor(ValidateableCodePoint.of(1, 'B')), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            Concatenation.of(StringElement.of("/"), StringElement.of(";")).printOn(new HashMapMedia()),
            allOf(
                (Matcher) hasEntry(is("type"), is("concatenation")),
                hasEntry(
                    is("concatenations"),
                    contains(
                        allOf(hasEntry("type", "char-val"), hasEntry("value", "/")),
                        allOf(hasEntry("type", "char-val"), hasEntry("value", ";"))
                    )
                )
            )
        );
    }
}
