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

import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.RIGHT_PARENTHESIS;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import io.github.sebastiantoepfer.bnf4j.abnf.element.SequenceGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

final class GroupExtractor implements Extractor, ExtractorOwner {

    static Extractor of(final ExtractorOwner owner) {
        return new GroupExtractor(owner, List.of());
    }

    private final ExtractorOwner owner;
    private final List<Element> elements;

    private GroupExtractor(final ExtractorOwner owner, final Collection<Element> elements) {
        this.owner = Objects.requireNonNull(owner);
        this.elements = List.copyOf(elements);
    }

    @Override
    public Extractor append(final int codePoint) {
        final Extractor result;
        if (codePoint == RIGHT_PARENTHESIS) {
            result = owner.imDone(asCreator());
        } else {
            result = ConcatenationExtractor.of(this, RIGHT_PARENTHESIS).append(codePoint);
        }
        return result;
    }

    @Override
    public Extractor imDone(final Creator creator) {
        final List<Element> newElements = new ArrayList<>(elements);
        newElements.add(creator.createAs(Element.class));
        return new GroupExtractor(owner, newElements);
    }

    private Creator asCreator() {
        return new Creator() {
            @Override
            public <T> T createAs(final Class<T> cls) {
                return cls.cast(SequenceGroup.of(elements));
            }
        };
    }
}
