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
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class ValueRangeAlternativesTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ValueRangeAlternatives.class).verify();
    }

    @Test
    void should_create_a_new_value_range() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.BINARY, 0),
                NumericCharacter.of(NumericCharacter.BASE.BINARY, 1)
            ),
            is(not(nullValue()))
        );
    }

    @Test
    void should_be_invalid_if_value_too_small() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x02),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x04)
            ).isValidFor(ValidateableCodePoint.of(0, 0x01)),
            is(false)
        );
    }

    @Test
    void should_be_invalid_if_value_too_big() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x02),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x04)
            ).isValidFor(ValidateableCodePoint.of(0, 0x05)),
            is(false)
        );
    }

    @Test
    void should_be_valid_if_value_is_in_range() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x02),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x04)
            ).isValidFor(ValidateableCodePoint.of(0, 0x03)),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_value_is_minimum() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x02),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x04)
            ).isValidFor(ValidateableCodePoint.of(0, 0x02)),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_value_is_maximum() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x02),
                NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x04)
            ).isValidFor(ValidateableCodePoint.of(0, 0x04)),
            is(true)
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.BINARY, 0b10101),
                NumericCharacter.of(NumericCharacter.BASE.BINARY, 0b10111)
            ).printOn(new HashMapMedia()),
            allOf(
                hasEntry(is("type"), is("val-range")),
                hasEntry(
                    is("from"),
                    allOf(hasEntry(is("base"), is("BINARY")), (Matcher) hasEntry(is("value"), is(21)))
                ),
                hasEntry(is("to"), allOf(hasEntry(is("base"), is("BINARY")), (Matcher) hasEntry(is("value"), is(23))))
            )
        );
    }

    @Test
    void should_return_two_as_dimension() {
        assertThat(
            ValueRangeAlternatives.of(
                NumericCharacter.of(NumericCharacter.BASE.BINARY, 0b10101),
                NumericCharacter.of(NumericCharacter.BASE.BINARY, 0b10111)
            ).dimension(),
            is(Dimension.of(1))
        );
    }
}
