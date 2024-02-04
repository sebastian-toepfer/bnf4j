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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Concatenation implements Element {

    public static Concatenation of(final Element left, final Element right, final Element... more) {
        final List<Element> concatenations = new ArrayList<>();
        concatenations.add(left);
        concatenations.add(right);
        concatenations.addAll(Arrays.asList(more));
        return of(concatenations);
    }

    public static Concatenation of(final Collection<? extends Element> alternatives) {
        return new Concatenation(alternatives);
    }

    private final List<Element> concatenations;

    private Concatenation(final Collection<? extends Element> concatenations) {
        this.concatenations = List.copyOf(concatenations);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("type", "concatenation").withValue("concatenations", List.copyOf(concatenations));
    }

    @Override
    public Dimension dimension() {
        return concatenations.stream().map(Element::dimension).reduce(Dimension.zero(), Dimension::plus);
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        final boolean result;
        if (dimension().isInRange(codePoint)) {
            result = new ListValidation(concatenations, Concatenation::of).isValidFor(codePoint);
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.concatenations);
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
        final Concatenation other = (Concatenation) obj;
        return Objects.equals(this.concatenations, other.concatenations);
    }

    @Override
    public String toString() {
        return "Concatenation{" + "concatenation=" + concatenations + '}';
    }
}
