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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Alternative;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Concatenation;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleReference;
import io.github.sebastiantoepfer.bnf4j.abnf.element.SequenceGroup;
import io.github.sebastiantoepfer.bnf4j.abnf.element.StringElement;
import io.github.sebastiantoepfer.bnf4j.abnf.element.VariableRepetition;
import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RuleTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Rule.class).verify();
    }

    @Test
    void should_know_his_name() {
        final Rule rule = Rule.of(RuleName.of("date"), RuleReference.of(RuleName.of("DIGIT")));

        assertThat(rule.hasRuleName(RuleName.of("date")), is(true));
        assertThat(rule.hasRuleName(RuleName.of("iso-date-time")), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            Rule.of(RuleName.of("rulename"), Alternative.of(StringElement.of("/"), StringElement.of(";"))).printOn(
                new HashMapMedia()
            ),
            allOf(
                (Matcher) hasEntry(is("type"), is("rule")),
                hasEntry(is("name"), allOf(hasEntry(is("name"), is("rulename")), hasEntry(is("type"), is("rulename")))),
                hasEntry(
                    is("elements"),
                    allOf(
                        (Matcher) hasEntry(is("type"), is("alternative")),
                        hasEntry(
                            is("alternatives"),
                            contains(
                                allOf(hasEntry("type", "char-val"), hasEntry("value", "/")),
                                allOf(hasEntry("type", "char-val"), hasEntry("value", ";"))
                            )
                        )
                    )
                )
            )
        );
    }

    @Nested
    class Validation {

        private Rule ruleName;

        @BeforeEach
        void initRule() {
            ruleName = Rule.of(
                RuleName.of("rulename"),
                Concatenation.of(
                    CoreRules.ALPHA,
                    VariableRepetition.of(
                        SequenceGroup.of(Alternative.of(CoreRules.ALPHA, CoreRules.DIGIT, StringElement.of("-")))
                    )
                )
            );
        }

        @Test
        void should_be_valid_for_alphas_only() {
            assertThat(ruleName.asPredicate().test("rulename"), is(true));
        }

        @Test
        void should_be_valid_for_valid_alpha_and_digits() {
            assertThat(ruleName.asPredicate().test("rul3nam3"), is(true));
        }

        @Test
        void should_be_valid_for_valid_alpha_and_minus() {
            assertThat(ruleName.asPredicate().test("rule-name"), is(true));
        }

        @Test
        void should_be_invalid_for_value_with_digist_at_start() {
            assertThat(ruleName.asPredicate().test("1rule"), is(false));
        }

        @Test
        void should_be_invalid_for_value_with_solidus() {
            assertThat(ruleName.asPredicate().test("rule/name"), is(false));
        }
    }
}
