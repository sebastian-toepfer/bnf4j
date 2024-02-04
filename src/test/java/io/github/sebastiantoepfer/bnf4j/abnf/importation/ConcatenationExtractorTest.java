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

import io.github.sebastiantoepfer.bnf4j.abnf.element.Alternative;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Concatenation;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleReference;
import io.github.sebastiantoepfer.bnf4j.abnf.element.VariableRepetition;
import org.junit.jupiter.api.Test;

class ConcatenationExtractorTest {

    @Test
    void should_create_rulereference() {
        assertThat(
            ConcatenationExtractor.of(new FakeOwner()).append('a').append(' ').finish().createAs(Element.class),
            is(RuleReference.of(RuleName.of("a")))
        );
    }

    @Test
    void should_create_repeating_rulereference() {
        assertThat(
            ConcatenationExtractor
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
    void should_create_elements_from_multiline() {
        assertThat(
            ConcatenationExtractor
                .of(new FakeOwner())
                .append('a')
                .append('\r')
                .append('\n')
                .append(' ')
                .append('b')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(Concatenation.of(RuleReference.of(RuleName.of("a")), RuleReference.of(RuleName.of("b"))))
        );
    }

    @Test
    void should_create_elements_as_alternative() {
        assertThat(
            ConcatenationExtractor
                .of(new FakeOwner())
                .append('a')
                .append('/')
                .append('b')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(Alternative.of(RuleReference.of(RuleName.of("a")), RuleReference.of(RuleName.of("b"))))
        );
    }

    @Test
    void should_create_elements_as_mixed_with_alternative() {
        assertThat(
            ConcatenationExtractor
                .of(new FakeOwner())
                .append('a')
                .append(' ')
                .append('b')
                .append('/')
                .append('c')
                .append('/')
                .append('d')
                .append(' ')
                .append('e')
                .append(' ')
                .finish()
                .createAs(Element.class),
            is(
                Concatenation.of(
                    RuleReference.of(RuleName.of("a")),
                    Alternative.of(
                        RuleReference.of(RuleName.of("b")),
                        RuleReference.of(RuleName.of("c")),
                        RuleReference.of(RuleName.of("d"))
                    ),
                    RuleReference.of(RuleName.of("e"))
                )
            )
        );
    }
}
