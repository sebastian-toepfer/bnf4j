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

import static java.util.Comparator.comparingLong;

public final class Dimension implements Comparable<Dimension> {

    private static final Dimension ZERO = Dimension.of(0);
    private static final Dimension ONE = Dimension.of(1);

    public static Dimension zero() {
        return ZERO;
    }

    public static Dimension one() {
        return ONE;
    }

    public static Dimension of(final long size) {
        return of(size, size);
    }

    public static Dimension of(final long minSize, final long maxSize) {
        if (minSize < 0) {
            throw new IllegalArgumentException("minSize must be greather or equals than 0!");
        }
        if (maxSize < minSize) {
            throw new IllegalArgumentException("MaxSize must be greater or equals minSize!");
        }
        return new Dimension(minSize, maxSize);
    }

    private final long minSize;
    private final long maxSize;

    private Dimension(final long minSize, final long maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public boolean isInRange(final ValidateableCodePoint codepoint) {
        return codepoint.position() < maxSize;
    }

    Dimension expandTo(final Dimension newMax) {
        return of(Math.min(minSize, newMax.minSize), Math.max(maxSize, newMax.maxSize));
    }

    Dimension plus(final Dimension toAdd) {
        return of(minSize + toAdd.minSize, maxSize + toAdd.maxSize);
    }

    Dimension multipliesBy(final int multiplier) {
        return of(minSize * multiplier, maxSize * multiplier);
    }

    @Override
    public int compareTo(final Dimension t) {
        return comparingLong(Dimension::length).thenComparing(comparingLong(Dimension::minSize)).compare(this, t);
    }

    private long minSize() {
        return minSize;
    }

    long length() {
        return maxSize;
    }

    @Override
    public String toString() {
        return "Dimension{" + "minSize=" + minSize + ", maxSize=" + maxSize + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.minSize ^ (this.minSize >>> 32));
        hash = 53 * hash + (int) (this.maxSize ^ (this.maxSize >>> 32));
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
        final Dimension other = (Dimension) obj;
        if (this.minSize != other.minSize) {
            return false;
        }
        return this.maxSize == other.maxSize;
    }
}
