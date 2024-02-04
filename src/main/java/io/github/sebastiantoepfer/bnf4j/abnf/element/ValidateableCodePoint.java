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

public final class ValidateableCodePoint {

    public static ValidateableCodePoint of(final int position, final int value) {
        if (position < 0) {
            throw new IllegalArgumentException("position must be greather than 0!");
        }
        if (value < 0) {
            throw new IllegalArgumentException("value must be greather than 0!");
        }
        return new ValidateableCodePoint(position, value);
    }

    private final int position;
    private final int value;

    private ValidateableCodePoint(final int position, final int value) {
        this.position = position;
        this.value = value;
    }

    public ValidateableCodePoint repositionBackBy(final Dimension before) {
        return repositionTo(position - (int) before.length());
    }

    public ValidateableCodePoint repositionBackBy(final int offset) {
        return repositionTo(position - offset);
    }

    public ValidateableCodePoint repositionTo(final int newPosition) {
        return of(newPosition, value);
    }

    public int position() {
        return position;
    }

    public boolean isEqualsTo(final int codePoint) {
        return codePoint == value;
    }

    public boolean isUpperCaseOf(final int codePoint) {
        return Character.toUpperCase(codePoint) == value;
    }

    public boolean isLowerCaseOf(final int codePoint) {
        return Character.toLowerCase(codePoint) == value;
    }

    public boolean isGreaterOrEqualsThan(final int codePoint) {
        return codePoint <= value;
    }

    public boolean isLessOrEqualsThan(final int codePoint) {
        return value <= codePoint;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.position;
        hash = 71 * hash + this.value;
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
        final ValidateableCodePoint other = (ValidateableCodePoint) obj;
        if (this.position != other.position) {
            return false;
        }
        return this.value == other.value;
    }
}
