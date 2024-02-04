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

import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.SOLIDUS;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Alternative;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

class AlternativeExtractor implements Extractor, ExtractorOwner {

    static Extractor of(
        final ExtractorOwner owner,
        final Element element,
        final ElementEndDetector endOfElementDelimiter
    ) {
        return new AlternativeExtractor(owner, List.of(element), endOfElementDelimiter, RepetitionExtractor::of);
    }

    private final ExtractorOwner owner;
    private final List<Element> elements;
    private final ElementEndDetector endOfElementDetector;
    private final Function<AlternativeExtractor, Extractor> newElement;

    private AlternativeExtractor(
        final ExtractorOwner owner,
        final Collection<Element> elements,
        final ElementEndDetector endOfElementDelimiter,
        final Function<AlternativeExtractor, Extractor> newElement
    ) {
        this.owner = Objects.requireNonNull(owner);
        this.elements = List.copyOf(elements);
        this.endOfElementDetector = Objects.requireNonNull(endOfElementDelimiter);
        this.newElement = Objects.requireNonNull(newElement);
    }

    @Override
    public Extractor append(final int codePoint) {
        final Extractor result;
        final ElementEndDetector currentEndOfElementDetector = endOfElementDetector.append(codePoint);
        if (currentEndOfElementDetector.isEndReached()) {
            result = currentEndOfElementDetector.applyTo(owner.imDone(asCreator()));
        } else if (Character.isWhitespace(codePoint)) {
            result = new AlternativeExtractor(owner, elements, currentEndOfElementDetector, newElement);
        } else if (codePoint == SOLIDUS) {
            result = new AlternativeExtractor(owner, elements, currentEndOfElementDetector, RepetitionExtractor::of);
        } else {
            result = newElement.apply(this).append(codePoint);
        }
        return result;
    }

    @Override
    public Extractor imDone(final Creator creator) {
        final Collection<Element> newElements = new ArrayList<>(elements);
        newElements.add(creator.createAs(Element.class));
        return new AlternativeExtractor(owner, newElements, endOfElementDetector, p -> owner.imDone(p.asCreator()));
    }

    @Override
    public Creator finish() {
        return owner.imDone(asCreator()).finish();
    }

    private Creator asCreator() {
        return new Creator() {
            @Override
            public <T> T createAs(final Class<T> cls) {
                return cls.cast(Alternative.of(elements));
            }
        };
    }
}
