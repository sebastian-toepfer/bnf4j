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
import java.util.Locale;
import java.util.Objects;

public final class StringElement implements Element {

    public static StringElement of(final String value) {
        return new StringElement(value);
    }

    private final String value;

    private StringElement(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        return (
            dimension().isInRange(codePoint) &&
            (codePoint.isEqualsTo(value.codePointAt(codePoint.position())) ||
                codePoint.isUpperCaseOf(value.codePointAt(codePoint.position())) ||
                codePoint.isLowerCaseOf(value.codePointAt(codePoint.position())))
        );
    }

    @Override
    public Dimension dimension() {
        return Dimension.of(value.length());
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("type", "char-val").withValue("value", value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.value.toLowerCase(Locale.US));
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
        final StringElement other = (StringElement) obj;
        return Objects.equals(this.value.toLowerCase(Locale.US), other.value.toLowerCase(Locale.US));
    }

    @Override
    public String toString() {
        return "StringElement{" + "value=" + value + '}';
    }
}
