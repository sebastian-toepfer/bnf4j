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

import io.github.sebastiantoepfer.bnf4j.abnf.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class ConcationTypeSwitch {

    private final ExtractorOwner owner;
    private final ElementEndDetector endOfElementDetector;
    private final List<Element> elements;

    public ConcationTypeSwitch(
        final ExtractorOwner owner,
        final ElementEndDetector endOfElementDetector,
        final List<Element> elements
    ) {
        this.owner = Objects.requireNonNull(owner);
        this.endOfElementDetector = Objects.requireNonNull(endOfElementDetector);
        this.elements = List.copyOf(elements);
    }

    public Extractor switchTo(final ExtractorOwnerFactory ownerFactory, final ExtractorFactory targetFactory) {
        final List<Element> newElements = new ArrayList<>(elements);
        final Element element = newElements.remove(newElements.size() - 1);
        return targetFactory.create(
            ownerFactory.create(owner, endOfElementDetector, newElements),
            endOfElementDetector,
            element
        );
    }
}
