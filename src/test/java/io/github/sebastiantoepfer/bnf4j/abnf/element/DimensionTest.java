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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.TreeSet;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class DimensionTest {

    @Test
    void should_equals_contract() {
        EqualsVerifier.forClass(Dimension.class).verify();
    }

    @Test
    void should_know_if_codepoint_is_in_range() {
        assertThat(Dimension.of(1).isInRange(ValidateableCodePoint.of(0, 0x13)), is(true));
    }

    @Test
    void should_know_if_codepoint_is_not_in_range() {
        assertThat(Dimension.of(1).isInRange(ValidateableCodePoint.of(1, 0x13)), is(false));
    }

    @Test
    void should_create_expanded_upper_bondary() {
        assertThat(Dimension.of(2).expandTo(Dimension.of(3, 7)), is(Dimension.of(2, 7)));
    }

    @Test
    void should_create_expanded_lower_and_upper_bondary() {
        assertThat(Dimension.of(2).expandTo(Dimension.of(1, 7)), is(Dimension.of(1, 7)));
    }

    @Test
    void should_create_expanded_lower_bondary() {
        assertThat(Dimension.of(2, 8).expandTo(Dimension.of(1, 7)), is(Dimension.of(1, 8)));
    }

    @Test
    void should_be_sortable() {
        assertThat(
            new TreeSet<>(
                List.of(
                    Dimension.of(6),
                    Dimension.of(2, 4),
                    Dimension.of(2),
                    Dimension.of(1, 7),
                    Dimension.of(2, 4),
                    Dimension.of(2, 6)
                )
            ),
            contains(Dimension.of(2), Dimension.of(2, 4), Dimension.of(2, 6), Dimension.of(6), Dimension.of(1, 7))
        );
    }
}
