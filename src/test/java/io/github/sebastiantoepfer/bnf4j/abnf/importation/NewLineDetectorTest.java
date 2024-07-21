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

import org.junit.jupiter.api.Test;

class NewLineDetectorTest {

    @Test
    void should_not_reach_end_after_cr() {
        assertThat(new NewLineDetector().append('\r').isEndReached(), is(false));
    }

    @Test
    void should_not_reach_end_after_crlf() {
        assertThat(new NewLineDetector().append('\r').append('\n').isEndReached(), is(false));
    }

    @Test
    void should_not_reach_end_after_crlfwsp() {
        assertThat(new NewLineDetector().append('\r').append('\n').append(' ').isEndReached(), is(false));
    }

    @Test
    void should_not_reach_end_after_crlflf() {
        assertThat(new NewLineDetector().append('\r').append('\n').append('\n').isEndReached(), is(false));
    }

    @Test
    void should_not_reach_end_after_crnonwsp() {
        assertThat(new NewLineDetector().append('\r').append('a').append('a').isEndReached(), is(false));
    }

    @Test
    void should_not_reach_end_after_lfnonwsp() {
        assertThat(new NewLineDetector().append('\n').append('a').isEndReached(), is(false));
    }

    @Test
    void should_not_reach_end_after_crlfwspnonwsp() {
        assertThat(new NewLineDetector().append('\r').append('\n').append(' ').append('a').isEndReached(), is(false));
    }

    @Test
    void should_reach_end_after_crlfnonwsp() {
        assertThat(new NewLineDetector().append('\r').append('\n').append('a').isEndReached(), is(true));
    }

    @Test
    void should_reach_end_after_crlfcrlfnonwsp() {
        assertThat(
            new NewLineDetector().append('\r').append('\n').append('\r').append('\n').append('a').isEndReached(),
            is(true)
        );
    }

    @Test
    void should_apply_codepoints() {
        assertThat(
            new NewLineDetector()
                .append('\r')
                .append('\n')
                .append('a')
                .applyTo(new StringExtractor())
                .finish()
                .createAs(String.class),
            is("\r\na")
        );
    }

    private static class StringExtractor implements Extractor {

        private final StringBuilder sb = new StringBuilder();

        @Override
        public Extractor append(final int codePoint) {
            sb.appendCodePoint(codePoint);
            return this;
        }

        @Override
        public Creator finish() {
            return new Creator() {
                @Override
                public <T> T createAs(final Class<T> cls) {
                    return cls.cast(sb.toString());
                }
            };
        }
    }
}
