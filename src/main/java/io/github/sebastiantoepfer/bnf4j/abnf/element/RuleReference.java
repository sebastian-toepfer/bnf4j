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

public final class RuleReference implements Element {

    public static RuleReference of(final RuleName name) {
        return new RuleReference(name);
    }

    private final RuleName name;

    private RuleReference(final RuleName name) {
        this.name = name;
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        return ruleNameAsStringElement().isValidFor(codePoint);
    }

    @Override
    public Dimension dimension() {
        return ruleNameAsStringElement().dimension();
    }

    Element ruleNameAsStringElement() {
        return StringElement.of(name.name());
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("type", "rule-ref").withValue("name", name);
    }

    @Override
    public String toString() {
        return "RuleReference{" + "name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.name);
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
        final RuleReference other = (RuleReference) obj;
        return Objects.equals(this.name, other.name);
    }
}
