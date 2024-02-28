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
import io.github.sebastiantoepfer.bnf4j.abnf.element.OptionalSequence;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleReference;
import io.github.sebastiantoepfer.bnf4j.abnf.element.SequenceGroup;
import io.github.sebastiantoepfer.bnf4j.abnf.element.StringElement;
import org.junit.jupiter.api.Test;

class ElementExtractorTest {

    @Test
    void should_create_rulereference() {
        assertThat(
            ElementExtractor.of(new FakeOwner()).append('a').finish().createAs(Element.class),
            is(RuleReference.of(RuleName.of("a")))
        );
    }

    @Test
    void should_create_group() {
        assertThat(
            ElementExtractor.of(new FakeOwner()).append('(').append('a').append(')').finish().createAs(Element.class),
            is(SequenceGroup.of(RuleReference.of(RuleName.of("a"))))
        );
    }

    @Test
    void should_create_option() {
        assertThat(
            ElementExtractor.of(new FakeOwner()).append('[').append('a').append(']').finish().createAs(Element.class),
            is(OptionalSequence.of(RuleReference.of(RuleName.of("a"))))
        );
    }

    @Test
    void should_char_val() {
        assertThat(
            ElementExtractor.of(new FakeOwner()).append('"').append('a').append('"').finish().createAs(Element.class),
            is(StringElement.of("a"))
        );
    }

    @Test
    void should_num_val() {
        assertThat(
            ElementExtractor.of(new FakeOwner())
                .append('%')
                .append('b')
                .append('1')
                .append('0')
                .append('1')
                .append('0')
                .finish()
                .createAs(Element.class),
            is(NumericCharacter.of(NumericCharacter.BASE.BINARY, 10))
        );
    }
}
