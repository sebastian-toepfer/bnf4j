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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class OptionalSequenceTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(OptionalSequence.class).verify();
    }

    @Test
    void should_create_new_optionalsequence() {
        assertThat(OptionalSequence.of(StringElement.of(".")), is(not(nullValue())));
    }

    @Test
    void should_has_dimension_from_zero_to_max() {
        assertThat(
            OptionalSequence.of(List.of(StringElement.of("a"), StringElement.of("b"))).dimension(),
            is(Dimension.of(0, 2))
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            OptionalSequence.of(List.of(StringElement.of("/"), StringElement.of(";"))).printOn(new HashMapMedia()),
            allOf(
                (Matcher) hasEntry(is("type"), is("option")),
                hasEntry(
                    is("optionals"),
                    contains(
                        allOf(hasEntry("type", "char-val"), hasEntry("value", "/")),
                        allOf(hasEntry("type", "char-val"), hasEntry("value", ";"))
                    )
                )
            )
        );
    }

    @Test
    void should_be_valid_for_every_codepoint() {
        assertThat(
            OptionalSequence
                .of(List.of(StringElement.of("/"), StringElement.of(";")))
                .isValidFor(ValidateableCodePoint.of(0, ',')),
            is(true)
        );
    }
}
