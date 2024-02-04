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
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class VariableRepetitionTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(VariableRepetition.class).verify();
    }

    @Test
    void should_create_an_exactly() {
        assertThat(
            VariableRepetition.ofExactly(RuleReference.of(RuleName.of("test")), 2),
            is(VariableRepetition.ofBetween(RuleReference.of(RuleName.of("test")), 2, 2))
        );
    }

    @Test
    void should_create_new_at_most_element() {
        assertThat(VariableRepetition.ofAtMost(StringElement.of("1"), 2), is(not(nullValue())));
    }

    @Test
    void should_create_new_at_least_element() {
        assertThat(VariableRepetition.ofAtLeast(StringElement.of("1"), 2), is(not(nullValue())));
    }

    @Test
    void should_create_new_optional_element() {
        assertThat(VariableRepetition.of(StringElement.of("1")), is(not(nullValue())));
    }

    @Test
    void should_be_printable() {
        assertThat(
            VariableRepetition.of(StringElement.of(".")).printOn(new HashMapMedia()),
            allOf(
                (Matcher) hasEntry(is("type"), is("repetition")),
                not(hasKey("atLeast")),
                not(hasKey("atMost")),
                hasEntry(is("element"), allOf(hasEntry(is("type"), is("char-val")), hasEntry(is("value"), is("."))))
            )
        );
    }

    @Test
    void should_be_printable_with_atLeast() {
        assertThat(
            VariableRepetition.ofAtLeast(StringElement.of("."), 2).printOn(new HashMapMedia()),
            allOf(
                (Matcher) hasEntry(is("type"), is("repetition")),
                (Matcher) hasEntry(is("atLeast"), is(2)),
                not(hasKey("atMost")),
                hasEntry(is("element"), allOf(hasEntry(is("type"), is("char-val")), hasEntry(is("value"), is("."))))
            )
        );
    }

    @Test
    void should_be_printable_with_atMost() {
        assertThat(
            VariableRepetition.ofAtMost(StringElement.of("."), 10).printOn(new HashMapMedia()),
            allOf(
                (Matcher) hasEntry(is("type"), is("repetition")),
                (Matcher) hasEntry(is("atMost"), is(10)),
                not(hasKey("atLeast")),
                hasEntry(is("element"), allOf(hasEntry(is("type"), is("char-val")), hasEntry(is("value"), is("."))))
            )
        );
    }

    @Test
    void should_be_printable_with_between() {
        assertThat(
            VariableRepetition.ofBetween(StringElement.of("."), 2, 10).printOn(new HashMapMedia()),
            allOf(
                (Matcher) hasEntry(is("type"), is("repetition")),
                (Matcher) hasEntry(is("atLeast"), is(2)),
                (Matcher) hasEntry(is("atMost"), is(10)),
                hasEntry(is("element"), allOf(hasEntry(is("type"), is("char-val")), hasEntry(is("value"), is("."))))
            )
        );
    }

    @Test
    void should_dimension_as_multimple_of() {
        assertThat(
            VariableRepetition
                .ofBetween(Alternative.of(StringElement.of("ab"), StringElement.of("cde")), 2, 4)
                .dimension(),
            is(Dimension.of(4, 12))
        );
    }

    @Test
    void should_be_valid_if_codepoint_matches_codepoint_at_posion_of_first_repeation() {
        assertThat(
            VariableRepetition
                .ofAtLeast(NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'a'), 2)
                .isValidFor(ValidateableCodePoint.of(0, 'a')),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_codepoint_matches_codepoint_at_posion_of_any_repeation() {
        assertThat(
            VariableRepetition
                .ofAtLeast(NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'a'), 2)
                .isValidFor(ValidateableCodePoint.of(4, 'a')),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_codepoint_not_equals_at_posion_for_min_repeation() {
        assertThat(
            VariableRepetition
                .ofAtLeast(NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'a'), 2)
                .isValidFor(ValidateableCodePoint.of(1, 'b')),
            is(false)
        );
    }

    @Test
    void should_be_invalod_if_codepoint_out_of_posion() {
        assertThat(
            VariableRepetition
                .ofAtMost(NumericCharacter.of(NumericCharacter.BASE.DECIMAL, 'a'), 2)
                .isValidFor(ValidateableCodePoint.of(3, 'a')),
            is(false)
        );
    }
}
