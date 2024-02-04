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
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ValidateableCodePointTest {

    @Test
    void should_equals_contract() {
        EqualsVerifier.forClass(ValidateableCodePoint.class).verify();
    }

    @Test
    void should_not_be_creatable_with_position_lower_zero() {
        assertThrows(IllegalArgumentException.class, () -> ValidateableCodePoint.of(-1, 0x00));
    }

    @Test
    void should_not_be_creatable_with_codepoint_lower_zero() {
        assertThrows(IllegalArgumentException.class, () -> ValidateableCodePoint.of(0, -1));
    }

    @Test
    void should_return_his_position() {
        assertThat(ValidateableCodePoint.of(10, 0x13).position(), is(10));
    }

    @Test
    void should_repositionable_by_offset() {
        assertThat(ValidateableCodePoint.of(10, 0x13).repositionBackBy(9), is(ValidateableCodePoint.of(1, 0x13)));
    }
}
