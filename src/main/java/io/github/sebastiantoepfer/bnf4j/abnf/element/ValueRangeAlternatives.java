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

import io.github.sebastiantoepfer.ddd.common.Media;
import java.util.Objects;

public final class ValueRangeAlternatives implements Element {

    public static ValueRangeAlternatives of(final NumericCharacter start, final NumericCharacter end) {
        return new ValueRangeAlternatives(start, end);
    }

    private final NumericCharacter start;
    private final NumericCharacter end;

    public ValueRangeAlternatives(final NumericCharacter start, final NumericCharacter end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("type", "val-range").withValue("from", start).withValue("to", end);
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        return start.lessThanOrEquals(codePoint) && end.greatherThaneOrEquals(codePoint);
    }

    @Override
    public String toString() {
        return "ValueRange{" + "start=" + start + ", end=" + end + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.start);
        hash = 89 * hash + Objects.hashCode(this.end);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValueRangeAlternatives other = (ValueRangeAlternatives) obj;
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        return Objects.equals(this.end, other.end);
    }
}
