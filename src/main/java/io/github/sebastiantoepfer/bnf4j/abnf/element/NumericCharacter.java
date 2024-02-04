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
import java.util.Arrays;
import java.util.Objects;

public final class NumericCharacter implements Element {

    public static NumericCharacter of(final BASE base, final int value) {
        return new NumericCharacter(base, value);
    }

    private final BASE base;
    private final int value;

    private NumericCharacter(final BASE base, final int value) {
        this.base = Objects.requireNonNull(base);
        this.value = value;
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        return codePoint.isEqualsTo(value);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("type", "num-val").withValue("base", base.name()).withValue("value", value);
    }

    boolean lessThanOrEquals(final ValidateableCodePoint codePoint) {
        return codePoint.isGreaterOrEqualsThan(value);
    }

    boolean greatherThaneOrEquals(final ValidateableCodePoint codePoint) {
        return codePoint.isLessOrEqualsThan(value);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(value);
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
        final NumericCharacter other = (NumericCharacter) obj;
        return value == other.value;
    }

    @Override
    public String toString() {
        return "NumericCharacter{" + "base=" + base + ", value=" + value + '}';
    }

    public enum BASE {
        BINARY('b', 2),
        DECIMAL('d', 10),
        HEXADECIMAL('x', 16);

        private final char baseShortName;
        private final int radix;

        private BASE(final char baseShortName, final int base) {
            this.baseShortName = baseShortName;
            this.radix = base;
        }

        public Integer convert(final String value) {
            return Integer.valueOf(value, radix);
        }

        public static BASE findByShortName(final char baseChar) {
            return Arrays
                .stream(BASE.values())
                .filter(b -> b.baseShortName == baseChar)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        }
    }
}
