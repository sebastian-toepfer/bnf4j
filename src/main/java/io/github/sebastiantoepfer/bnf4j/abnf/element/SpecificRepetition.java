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

public final class SpecificRepetition implements Element {

    public static SpecificRepetition of(final Element elementToRepeat, final int occurences) {
        return new SpecificRepetition(elementToRepeat, occurences);
    }

    private final Element elementToRepeat;
    private final int occurences;

    private SpecificRepetition(final Element elementToRepeat, final int occurences) {
        this.elementToRepeat = Objects.requireNonNull(elementToRepeat);
        this.occurences = occurences;
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media
            .withValue("type", "fix-repetition")
            .withValue("repeat", occurences)
            .withValue("element", elementToRepeat);
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        final boolean result;
        if (dimension().isInRange(codePoint)) {
            if (elementToRepeat.dimension().isInRange(codePoint)) {
                result = elementToRepeat.isValidFor(codePoint);
            } else {
                result =
                    of(elementToRepeat, occurences).isValidFor(codePoint.repositionBackBy(elementToRepeat.dimension()));
            }
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Dimension dimension() {
        return elementToRepeat.dimension().multipliesBy(occurences);
    }

    @Override
    public String toString() {
        return "SpecificRepetition{" + "elementToRepeat=" + elementToRepeat + ", occurences=" + occurences + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.elementToRepeat);
        hash = 89 * hash + this.occurences;
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
        final SpecificRepetition other = (SpecificRepetition) obj;
        if (this.occurences != other.occurences) {
            return false;
        }
        return Objects.equals(this.elementToRepeat, other.elementToRepeat);
    }
}
