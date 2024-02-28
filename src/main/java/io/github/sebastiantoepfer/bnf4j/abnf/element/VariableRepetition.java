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

public final class VariableRepetition implements Element {

    public static VariableRepetition of(final Element element) {
        return ofAtLeast(element, 0);
    }

    public static VariableRepetition ofAtLeast(final Element element, final int minOccurrences) {
        return ofBetween(element, minOccurrences, Integer.MAX_VALUE);
    }

    public static VariableRepetition ofAtMost(final Element element, final int maxOccurrences) {
        return ofBetween(element, 0, maxOccurrences);
    }

    public static VariableRepetition ofExactly(final Element element, final int exactly) {
        return ofBetween(element, exactly, exactly);
    }

    public static VariableRepetition ofBetween(final Element element, final int min, final int max) {
        return new VariableRepetition(element, min, max);
    }

    private final int minOccurrences;
    private final int maxOccurrences;
    private final Element element;

    private VariableRepetition(final Element element, final int minOccurrences, final int maxOccurrences) {
        this.minOccurrences = minOccurrences;
        this.maxOccurrences = maxOccurrences;
        this.element = Objects.requireNonNull(element);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        final T result;
        if (minOccurrences > 0 && maxOccurrences < Integer.MAX_VALUE) {
            result = media.withValue("atLeast", minOccurrences).withValue("atMost", maxOccurrences);
        } else if (minOccurrences > 0) {
            result = media.withValue("atLeast", minOccurrences);
        } else if (maxOccurrences < Integer.MAX_VALUE) {
            result = media.withValue("atMost", maxOccurrences);
        } else {
            result = media;
        }
        return result.withValue("type", "repetition").withValue("element", element);
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        final boolean result;
        if (dimension().isInRange(codePoint)) {
            if (element.dimension().isInRange(codePoint)) {
                result = element.isValidFor(codePoint);
            } else {
                final int newMinOccurrences = Math.min(minOccurrences, 1);
                result = ofBetween(
                    element,
                    newMinOccurrences,
                    Math.max(newMinOccurrences, maxOccurrences - 1)
                ).isValidFor(codePoint.repositionBackBy(element.dimension()));
            }
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Dimension dimension() {
        return element
            .dimension()
            .multipliesBy(minOccurrences)
            .expandTo(element.dimension().multipliesBy(maxOccurrences));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.minOccurrences;
        hash = 17 * hash + this.maxOccurrences;
        hash = 17 * hash + Objects.hashCode(this.element);
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
        final VariableRepetition other = (VariableRepetition) obj;
        if (this.minOccurrences != other.minOccurrences) {
            return false;
        }
        if (this.maxOccurrences != other.maxOccurrences) {
            return false;
        }
        return Objects.equals(this.element, other.element);
    }

    @Override
    public String toString() {
        return (
            "VariableRepetition{" +
            "minOccurrences=" +
            minOccurrences +
            ", maxOccurrences=" +
            maxOccurrences +
            ", element=" +
            element +
            '}'
        );
    }
}
