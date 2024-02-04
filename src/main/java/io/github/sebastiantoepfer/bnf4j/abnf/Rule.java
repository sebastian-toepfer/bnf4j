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
package io.github.sebastiantoepfer.bnf4j.abnf;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValidateableCodePoint;
import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.common.Printable;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class Rule implements Printable {

    public static Rule of(final RuleName name, final Element elements) {
        return new Rule(name, elements);
    }

    private final RuleName name;
    private final Element elements;

    private Rule(final RuleName name, final Element elements) {
        this.name = Objects.requireNonNull(name);
        this.elements = Objects.requireNonNull(elements);
    }

    public boolean hasRuleName(final RuleName name) {
        return Objects.equals(this.name, name);
    }

    Predicate<String> asPredicate() {
        return s ->
            IntStream
                .range(0, s.length())
                .boxed()
                .map(i -> ValidateableCodePoint.of(i, s.codePointAt(i)))
                .allMatch(elements::isValidFor);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("name", name).withValue("type", "rule").withValue("elements", elements);
    }

    @Override
    public String toString() {
        return "Rule{" + "name=" + name + ", elements=" + elements + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + Objects.hashCode(this.elements);
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
        final Rule other = (Rule) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.elements, other.elements);
    }
}
