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

import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.QUOTATION_MARK;

import io.github.sebastiantoepfer.bnf4j.abnf.element.StringElement;
import java.util.Objects;

class CharValExtractor implements Extractor {

    static Extractor of(final ExtractorOwner owner) {
        return new CharValExtractor(owner, "");
    }

    private final ExtractorOwner owner;
    private final String value;

    private CharValExtractor(final ExtractorOwner owner, final String value) {
        this.owner = Objects.requireNonNull(owner);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Extractor append(final int codePoint) {
        final Extractor result;
        if (codePoint == QUOTATION_MARK) {
            result = owner.imDone(asCreator());
        } else {
            result = new CharValExtractor(owner, value.concat(Character.toString(codePoint)));
        }
        return result;
    }

    private Creator asCreator() {
        return new Creator() {
            @Override
            public <T> T createAs(final Class<T> cls) {
                return cls.cast(StringElement.of(value));
            }
        };
    }
}
