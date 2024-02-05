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
import org.junit.jupiter.api.Test;

class RuleReferenceTest {

    @Test
    void should_create_new() {
        assertThat(RuleReference.of(RuleName.of("name")), is(not(nullValue())));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(RuleReference.class).verify();
    }

    @Test
    void should_has_dimension_of_rulename_length() {
        assertThat(RuleReference.of(RuleName.of("rule")).dimension(), is(Dimension.of(4)));
    }

    @Test
    void should_be_printable() {
        assertThat(
            RuleReference.of(RuleName.of("test")).printOn(new HashMapMedia()),
            allOf(hasEntry(is("type"), is("rulename")), hasEntry(is("name"), is("test")))
        );
    }

    @Test
    void should_be_invalid_if_codepoint_is_not_equals_codepoint_at_position() {
        final Element element = RuleReference.of(RuleName.of("test"));
        assertThat(element.isValidFor(ValidateableCodePoint.of(1, 't')), is(false));
    }

    @Test
    void should_be_invalid_if_codepoint_is_out_of_position() {
        final Element element = RuleReference.of(RuleName.of("test"));
        assertThat(element.isValidFor(ValidateableCodePoint.of(4, 't')), is(false));
    }
}
