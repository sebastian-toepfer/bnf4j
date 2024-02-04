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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class StringElementTest {

    @Test
    void should_create_new() {
        assertThat(StringElement.of("test"), is(not(nullValue())));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(StringElement.class).withNonnullFields("value").verify();
    }

    @Test
    void should_return_string_length_as_dimension() {
        assertThat(StringElement.of("abc").dimension(), is(Dimension.of(3)));
    }

    @Test
    void should_be_invalid_if_codepoint_is_outside_the_dimension() {
        assertThat(StringElement.of("abc").isValidFor(ValidateableCodePoint.of(3, 'a')), is(false));
    }

    @Test
    void should_be_valid_if_codepoint_is_equals_codepoint_in_string_at_position() {
        assertThat(StringElement.of("abc").isValidFor(ValidateableCodePoint.of(0, 'a')), is(true));
        assertThat(StringElement.of("abc").isValidFor(ValidateableCodePoint.of(1, 'b')), is(true));
        assertThat(StringElement.of("abc").isValidFor(ValidateableCodePoint.of(2, 'c')), is(true));
    }

    @Test
    void should_be_invalid_if_codepoint_is_not_equals_codepoint_in_string_at_position() {
        assertThat(StringElement.of("abc").isValidFor(ValidateableCodePoint.of(0, 'b')), is(false));
        assertThat(StringElement.of("abc").isValidFor(ValidateableCodePoint.of(1, 'a')), is(false));
        assertThat(StringElement.of("abc").isValidFor(ValidateableCodePoint.of(2, 'b')), is(false));
    }

    @Test
    void should_be_valid_if_codePoint_is_lowercase_representation() {
        assertThat(StringElement.of("A").isValidFor(ValidateableCodePoint.of(0, 0x61)), is(true));
    }

    @Test
    void should_be_valid_if_codePoint_is_upercase_representation() {
        assertThat(StringElement.of("A").isValidFor(ValidateableCodePoint.of(0, 0x41)), is(true));
    }

    @Test
    void should_be_invalid_if_codePoint_is_neither_upercase_nor_lowercase_representation() {
        assertThat(StringElement.of("A").isValidFor(ValidateableCodePoint.of(0, 0x42)), is(false));
    }
}
