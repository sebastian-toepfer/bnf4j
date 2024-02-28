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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.NumericCharacter;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValueRangeAlternatives;
import org.junit.jupiter.api.Test;

class NumValExtractorTest {

    @Test
    void should_create_bin_val() {
        assertThat(
            NumValExtractor.of(new FakeOwner())
                .append('b')
                .append('0')
                .append('1')
                .append('0')
                .finish()
                .createAs(Element.class),
            is(NumericCharacter.of(NumericCharacter.BASE.BINARY, 2))
        );
    }

    @Test
    void should_create_dec_val() {
        assertThat(
            NumValExtractor.of(new FakeOwner())
                .append('d')
                .append('3')
                .append('1')
                .append('0')
                .finish()
                .createAs(Element.class),
            is(NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 310))
        );
    }

    @Test
    void should_create_hex_val() {
        assertThat(
            NumValExtractor.of(new FakeOwner())
                .append('x')
                .append('3')
                .append('a')
                .append('A')
                .finish()
                .createAs(Element.class),
            is(NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x3AA))
        );
    }

    @Test
    void should_create_value_range() {
        assertThat(
            NumValExtractor.of(new FakeOwner())
                .append('x')
                .append('3')
                .append('a')
                .append('A')
                .append('-')
                .append('4')
                .append('0')
                .append('F')
                .finish()
                .createAs(Element.class),
            is(
                ValueRangeAlternatives.of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x3AA),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x40F)
                )
            )
        );
    }
}
