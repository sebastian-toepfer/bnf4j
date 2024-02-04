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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValidateableCodePoint;
import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CoreRulesTest {

    @Test
    void should_be_printable() {
        assertThat(
            CoreRules.VCHAR.printOn(new HashMapMedia()),
            allOf(
                (Matcher) hasEntry(is("type"), is("corerule")),
                hasEntry(is("name"), allOf(hasEntry(is("name"), is("VCHAR")), hasEntry(is("type"), is("rulename")))),
                hasEntry(
                    is("elements"),
                    allOf(
                        hasEntry(is("type"), is("val-range")),
                        hasEntry(
                            is("from"),
                            allOf(hasEntry(is("base"), is("HEXADECIMAL")), (Matcher) hasEntry(is("value"), is(0x21)))
                        ),
                        hasEntry(
                            is("to"),
                            allOf(hasEntry(is("base"), is("HEXADECIMAL")), (Matcher) hasEntry(is("value"), is(0x7E)))
                        )
                    )
                )
            )
        );
    }

    @Nested
    class Alpha {

        @Test
        void should_return_rulename() {
            assertThat(CoreRules.ALPHA.asRuleName(), is(RuleName.of("ALPHA")));
        }

        @ParameterizedTest(name = "{0} is a valid alpha")
        @ValueSource(ints = { 0x41, 0x51, 0x5A, 0x61, 0x71, 0x7A })
        void should_be_valid_for(final int codePoint) {
            assertThat(CoreRules.ALPHA.isValidFor(codePoint), is(true));
        }

        @ParameterizedTest(name = "{0} is not a valid alpha")
        @ValueSource(ints = { 0x30, 0x39, 0x5B, 0x60, 0x7B })
        void should_be_invalid_for(final int codePoint) {
            assertThat(CoreRules.ALPHA.isValidFor(codePoint), is(false));
        }
    }

    @Nested
    class Bit {

        @Test
        void should_be_valid_for_codepoint_of_zero() {
            assertThat(CoreRules.BIT.isValidFor(0x30), is(true));
        }

        @Test
        void should_be_valid_for_codepoint_of_one() {
            assertThat(CoreRules.BIT.isValidFor(0x31), is(true));
        }

        @Test
        void should_be_invalid_for_codepoint_of_two() {
            assertThat(CoreRules.BIT.isValidFor(0x32), is(false));
        }
    }

    @Nested
    class Char {

        @ParameterizedTest(name = "{0} is a valid char")
        @ValueSource(ints = { 0x01, 0x11, 0x21, 0x31, 0x41, 0x51, 0x61, 0x71, 0x7F })
        void should_be_valid_for(final int codePoint) {
            assertThat(CoreRules.CHAR.isValidFor(codePoint), is(true));
        }

        @ParameterizedTest(name = "{0} is not a valid char")
        @ValueSource(ints = { 0x00, 0x80, 0x81, 0x91, 0xA1, 0xB1, 0xC1, 0xD1, 0xE1, 0xF1, 0xFF })
        void should_be_invalid_for(final int codePoint) {
            assertThat(CoreRules.CHAR.isValidFor(codePoint), is(false));
        }
    }

    @Nested
    class Cr {

        @Test
        void should_be_valid_for_cr() {
            assertThat(CoreRules.CR.isValidFor(0x0D), is(true));
        }

        @Test
        void should_be_invalid_for_whitespace() {
            assertThat(CoreRules.CR.isValidFor(0x00), is(false));
        }

        @Test
        void should_be_invalid_for_linefeed() {
            assertThat(CoreRules.CR.isValidFor(0x0A), is(false));
        }

        @Test
        void should_be_invalid_for_a() {
            assertThat(CoreRules.CR.isValidFor(0x41), is(false));
        }
    }

    @Nested
    class Crlf {

        @Test
        void should_return_rulename() {
            assertThat(CoreRules.CRLF.asRuleName(), is(RuleName.of("CRLF")));
        }

        @Test
        void should_be_valid_for_crlf() {
            final Element element = CoreRules.CRLF;

            assertThat(element.isValidFor(ValidateableCodePoint.of(0, '\r')), is(true));
            assertThat(element.isValidFor(ValidateableCodePoint.of(1, '\n')), is(true));
        }
    }

    @Nested
    class Ctl {

        @Test
        void should_return_rulename() {
            assertThat(CoreRules.CTL.asRuleName(), is(RuleName.of("CTL")));
        }

        @ParameterizedTest(name = "{0} is a valid CTL")
        @ValueSource(ints = { 0x00, 0x01, 0x11, 0x1A, 0x1F, 0x7F })
        void should_be_valid_for(final int codePoint) {
            assertThat(CoreRules.CTL.isValidFor(codePoint), is(true));
        }

        @ParameterizedTest(name = "{0} is not a valid CTL")
        @ValueSource(ints = { 0x20, 0x30, 0x6A, 0x7A, 0x7E })
        void should_be_invalid_for(final int codePoint) {
            assertThat(CoreRules.CTL.isValidFor(codePoint), is(false));
        }
    }

    @Nested
    class Digit {

        @Test
        void should_return_rulename() {
            assertThat(CoreRules.DIGIT.asRuleName(), is(RuleName.of("DIGIT")));
        }

        @ParameterizedTest(name = "{0} is a valid digit")
        @ValueSource(ints = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39 })
        void should_be_valid_for(final int codePoint) {
            assertThat(CoreRules.DIGIT.isValidFor(codePoint), is(true));
        }

        @ParameterizedTest(name = "{0} is not a valid dig")
        @ValueSource(ints = { 0x00, 0x10, 0x21, 0x29, 0x40, 0x50, 0x60 })
        void should_be_invalid_for(final int codePoint) {
            assertThat(CoreRules.DIGIT.isValidFor(codePoint), is(false));
        }
    }

    @Nested
    class Dquote {

        @Test
        void should_be_valid() {
            assertThat(CoreRules.DQUOTE.isValidFor(0x22), is(true));
        }

        @Test
        void should_be_invalid() {
            assertThat(CoreRules.DQUOTE.isValidFor(0x21), is(false));
        }
    }

    @Nested
    class Hexdig {

        @ParameterizedTest(name = "{0} is a valid hexdig")
        @ValueSource(
            ints = {
                0x30,
                0x31,
                0x32,
                0x33,
                0x34,
                0x35,
                0x36,
                0x37,
                0x38,
                0x39,
                0x41,
                0x42,
                0x43,
                0x44,
                0x45,
                0x46,
                0x61,
                0x62,
                0x64,
                0x65,
                0x66,
            }
        )
        void should_be_valid_for(final int codePoint) {
            assertThat(CoreRules.HEXDIG.isValidFor(codePoint), is(true));
        }

        @ParameterizedTest(name = "{0} is not a valid hexdig")
        @ValueSource(ints = { 0x00, 0x10, 0x21, 0x29, 0x47, 0x50, 0x60, 0x67, 0x71 })
        void should_be_invalid_for(final int codePoint) {
            assertThat(CoreRules.HEXDIG.isValidFor(codePoint), is(false));
        }
    }

    @Nested
    class Htab {

        @Test
        void should_be_valid_for_htab() {
            assertThat(CoreRules.HTAB.isValidFor(0x09), is(true));
        }

        @Test
        void should_be_invalid_for_whitespace() {
            assertThat(CoreRules.HTAB.isValidFor(0x00), is(false));
        }

        @Test
        void should_be_invalid_for_linefeed() {
            assertThat(CoreRules.HTAB.isValidFor(0x0A), is(false));
        }

        @Test
        void should_be_invalid_for_a() {
            assertThat(CoreRules.HTAB.isValidFor(0x41), is(false));
        }
    }

    @Nested
    class Lf {

        @Test
        void should_return_rulename() {
            assertThat(CoreRules.LF.asRuleName(), is(RuleName.of("LF")));
        }

        @Test
        void should_be_valid_for_linefeed() {
            assertThat(CoreRules.LF.isValidFor(0x0A), is(true));
        }

        @Test
        void should_be_invalid_for_whitespace() {
            assertThat(CoreRules.LF.isValidFor(0x00), is(false));
        }

        @Test
        void should_be_invalid_for_cr() {
            assertThat(CoreRules.LF.isValidFor(0x0D), is(false));
        }

        @Test
        void should_be_invalid_for_a() {
            assertThat(CoreRules.LF.isValidFor(0x41), is(false));
        }
    }

    @Nested
    class Lwsp {

        @Test
        void should_return_rulename() {
            assertThat(CoreRules.LWSP.asRuleName(), is(RuleName.of("LWSP")));
        }

        @Test
        void should_be_valid_for_sp_sp() {
            final Element element = CoreRules.LWSP;

            assertThat(element.isValidFor(ValidateableCodePoint.of(0, 0x20)), is(true));
            assertThat(element.isValidFor(ValidateableCodePoint.of(1, 0x20)), is(true));
        }

        @Test
        void should_be_valid_for_sp_htab() {
            final Element element = CoreRules.LWSP;

            assertThat(element.isValidFor(ValidateableCodePoint.of(0, 0x20)), is(true));
            assertThat(element.isValidFor(ValidateableCodePoint.of(1, 0x09)), is(true));
        }

        @Test
        void should_be_valid_for_htab_sp() {
            final Element element = CoreRules.LWSP;

            assertThat(element.isValidFor(ValidateableCodePoint.of(0, 0x09)), is(true));
            assertThat(element.isValidFor(ValidateableCodePoint.of(1, 0x20)), is(true));
        }

        @Test
        void should_be_valid_for_crlf_sp() {
            final Element element = CoreRules.LWSP;

            assertThat(element.isValidFor(ValidateableCodePoint.of(0, '\r')), is(true));
            assertThat(element.isValidFor(ValidateableCodePoint.of(1, '\n')), is(true));
            assertThat(element.isValidFor(ValidateableCodePoint.of(2, 0x20)), is(true));
        }
    }

    @Nested
    class Octet {

        @ParameterizedTest(name = "{0} is a valid octet")
        @ValueSource(ints = { 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0xA1, 0xFF })
        void should_be_valid_for(final int codePoint) {
            assertThat(CoreRules.OCTET.isValidFor(codePoint), is(true));
        }

        @ParameterizedTest(name = "{0} is not a valid octet")
        @ValueSource(ints = { 0x100, 0x101 })
        void should_be_invalid_for(final int codePoint) {
            assertThat(CoreRules.OCTET.isValidFor(codePoint), is(false));
        }
    }

    @Nested
    class Space {

        @Test
        void should_be_valid_for_space() {
            assertThat(CoreRules.SP.isValidFor(0x20), is(true));
        }

        @Test
        void should_be_invalid_for_tab() {
            assertThat(CoreRules.SP.isValidFor(0x09), is(false));
        }

        @Test
        void should_be_invalid_for_linefeed() {
            assertThat(CoreRules.SP.isValidFor(0x0A), is(false));
        }
    }

    @Nested
    class VisibleCharacters {

        @ParameterizedTest(name = "{0} is a valid vchat")
        @ValueSource(ints = { 0x21, 0x31, 0x42, 0x55, 0x65, 0x71, 0x7E })
        void should_be_valid_for(final int codePoint) {
            assertThat(CoreRules.VCHAR.isValidFor(codePoint), is(true));
        }

        @ParameterizedTest(name = "{0} is not a valid dig")
        @ValueSource(ints = { 0x00, 0x10, 0x20, 0x7F, 0x80 })
        void should_be_invalid_for(final int codePoint) {
            assertThat(CoreRules.VCHAR.isValidFor(codePoint), is(false));
        }
    }

    @Nested
    class WhiteSpace {

        @Test
        void should_be_valid_for_space() {
            assertThat(CoreRules.WSP.isValidFor(0x20), is(true));
        }

        @Test
        void should_be_valid_for_htab() {
            assertThat(CoreRules.WSP.isValidFor(0x09), is(true));
        }

        @Test
        void should_be_valid_for_linefeed() {
            assertThat(CoreRules.WSP.isValidFor(0x0A), is(false));
        }
    }
}
