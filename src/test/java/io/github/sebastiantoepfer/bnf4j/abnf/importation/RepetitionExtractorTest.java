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
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleReference;
import io.github.sebastiantoepfer.bnf4j.abnf.element.SpecificRepetition;
import io.github.sebastiantoepfer.bnf4j.abnf.element.VariableRepetition;
import org.junit.jupiter.api.Test;

class RepetitionExtractorTest {

    @Test
    void should_create_non_repeating_rulereference() {
        assertThat(
            RepetitionExtractor.of(new FakeOwner()).append('a').append(' ').finish().createAs(Element.class),
            is(RuleReference.of(RuleName.of("a")))
        );
    }

    @Test
    void should_create_atLeast_repeating_rulereference() {
        assertThat(
            RepetitionExtractor
                .of(new FakeOwner())
                .append('1')
                .append('*')
                .append('a')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(VariableRepetition.ofAtLeast(RuleReference.of(RuleName.of("a")), 1))
        );
    }

    @Test
    void should_create_specificrepetition_rulereference() {
        assertThat(
            RepetitionExtractor
                .of(new FakeOwner())
                .append('1')
                .append('1')
                .append('a')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(SpecificRepetition.of(RuleReference.of(RuleName.of("a")), 11))
        );
    }

    @Test
    void should_create_repeating_rulereference() {
        assertThat(
            RepetitionExtractor
                .of(new FakeOwner())
                .append('*')
                .append('a')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(VariableRepetition.of(RuleReference.of(RuleName.of("a"))))
        );
    }

    @Test
    void should_create_atMost_repeating_rulereference() {
        assertThat(
            RepetitionExtractor
                .of(new FakeOwner())
                .append('*')
                .append('1')
                .append('5')
                .append('a')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(VariableRepetition.ofAtMost(RuleReference.of(RuleName.of("a")), 15))
        );
    }

    @Test
    void should_create_between_repeating_rulereference() {
        assertThat(
            RepetitionExtractor
                .of(new FakeOwner())
                .append('1')
                .append('*')
                .append('2')
                .append('5')
                .append('a')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(VariableRepetition.ofBetween(RuleReference.of(RuleName.of("a")), 1, 25))
        );
    }
}
