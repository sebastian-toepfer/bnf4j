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

import io.github.sebastiantoepfer.bnf4j.abnf.Rule;
import io.github.sebastiantoepfer.bnf4j.abnf.RuleList;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleReference;
import org.junit.jupiter.api.Test;

class RuleListExtractorTest {

    @Test
    void should_create_list_with_one_rule_with_rulereference() {
        assertThat(
            RuleListExtractor.of()
                .append('a')
                .append('=')
                .append('b')
                .append('\r')
                .append('\n')
                .finish()
                .createAs(RuleList.class),
            is(RuleList.of(Rule.of(RuleName.of("a"), RuleReference.of(RuleName.of("b")))))
        );
    }

    @Test
    void should_create_list_with_two_rules_with_rulereference() {
        assertThat(
            RuleListExtractor.of()
                .append('a')
                .append('=')
                .append('b')
                .append('\r')
                .append('\n')
                .append('c')
                .append('=')
                .append('d')
                .append('\r')
                .append('\n')
                .finish()
                .createAs(RuleList.class),
            is(
                RuleList.of(
                    Rule.of(RuleName.of("a"), RuleReference.of(RuleName.of("b"))),
                    Rule.of(RuleName.of("c"), RuleReference.of(RuleName.of("d")))
                )
            )
        );
    }
}
