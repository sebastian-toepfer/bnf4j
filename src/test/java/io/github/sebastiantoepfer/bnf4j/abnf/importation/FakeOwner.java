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

import io.github.sebastiantoepfer.bnf4j.abnf.importation.Creator;
import io.github.sebastiantoepfer.bnf4j.abnf.importation.Extractor;
import io.github.sebastiantoepfer.bnf4j.abnf.importation.ExtractorOwner;

final class FakeOwner implements Extractor, ExtractorOwner, Creator {

    private final Creator creator;

    FakeOwner() {
        this(null);
    }

    private FakeOwner(final Creator creator) {
        this.creator = creator;
    }

    @Override
    public Extractor append(final int codePoint) {
        return this;
    }

    @Override
    public Extractor imDone(final Creator creator) {
        return new FakeOwner(creator);
    }

    @Override
    public Creator finish() {
        return this;
    }

    @Override
    public <T> T createAs(final Class<T> cls) {
        return creator.createAs(cls);
    }
}
