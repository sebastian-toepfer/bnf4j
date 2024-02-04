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
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class NumericCharacterTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(NumericCharacter.class).withOnlyTheseFields("value").verify();
    }

    @Test
    void should_parse_int_with_dec_radix() {
        assertThat(NumericCharacter.BASE.DECIMAL.convert("11"), is(11));
    }

    @Test
    void should_parse_int_with_bin_radix() {
        assertThat(NumericCharacter.BASE.BINARY.convert("11"), is(3));
    }

    @Test
    void should_create_bin_base() {
        assertThat(NumericCharacter.BASE.findByShortName('b'), is(NumericCharacter.BASE.BINARY));
    }

    @Test
    void should_create_dec_base() {
        assertThat(NumericCharacter.BASE.findByShortName('d'), is(NumericCharacter.BASE.DECIMAL));
    }

    @Test
    void should_create_hex_base() {
        assertThat(NumericCharacter.BASE.findByShortName('x'), is(NumericCharacter.BASE.HEXADECIMAL));
    }

    @Test
    void should_throw_exception_for_invalid_shortname() {
        assertThrows(IllegalArgumentException.class, () -> NumericCharacter.BASE.findByShortName('h'));
    }

    @Test
    void should_be_printable() {
        assertThat(
            NumericCharacter.of(NumericCharacter.BASE.BINARY, 0b10101).printOn(new HashMapMedia()),
            allOf(
                hasEntry(is("type"), is("num-val")),
                hasEntry(is("base"), is("BINARY")),
                (Matcher) hasEntry(is("value"), is(21))
            )
        );
    }
}
