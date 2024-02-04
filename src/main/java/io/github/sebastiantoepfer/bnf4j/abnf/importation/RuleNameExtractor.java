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
package io.github.sebastiantoepfer.bnf4j.abnf.importation;

import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import java.util.Objects;

final class RuleNameExtractor implements Extractor {

    public static Extractor of(final ExtractorOwner owner) {
        return new RuleNameExtractor(owner);
    }

    private final ExtractorOwner owner;
    private final String name;

    private RuleNameExtractor(final ExtractorOwner owner) {
        this(owner, "");
    }

    private RuleNameExtractor(final ExtractorOwner owner, final String name) {
        this.owner = Objects.requireNonNull(owner);
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public Extractor append(final int codePoint) {
        final Extractor result;
        if (name.isEmpty() && !Character.isLetter(codePoint)) {
            throw new IllegalArgumentException("rulename must start with a letter!");
        } else if (Character.isLetterOrDigit(codePoint) || codePoint == UsefulCodepoints.HYPHEN_MINUS) {
            result = new RuleNameExtractor(owner, name.concat(Character.toString(codePoint)));
        } else {
            result = owner.imDone(asCreator()).append(codePoint);
        }
        return result;
    }

    @Override
    public Creator finish() {
        return owner.imDone(asCreator()).finish();
    }

    private Creator asCreator() {
        return new Creator() {
            @Override
            public <T> T createAs(final Class<T> cls) {
                return cls.cast(RuleName.of(name));
            }
        };
    }
}
