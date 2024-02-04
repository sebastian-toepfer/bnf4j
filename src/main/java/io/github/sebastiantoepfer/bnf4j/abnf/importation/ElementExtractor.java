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

import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.LEFT_PARENTHESIS;
import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.LEFT_SQUARE_BRACKET;
import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.PERCENT_SIGN;
import static io.github.sebastiantoepfer.bnf4j.abnf.importation.UsefulCodepoints.QUOTATION_MARK;

import java.util.Objects;

final class ElementExtractor implements Extractor {

    static Extractor of(final ExtractorOwner owner) {
        return new ElementExtractor(owner);
    }

    private final ExtractorOwner owner;

    private ElementExtractor(final ExtractorOwner owner) {
        this.owner = Objects.requireNonNull(owner);
    }

    @Override
    public Extractor append(final int codePoint) {
        final Extractor result;
        if (Character.isAlphabetic(codePoint)) {
            result = RuleReferenceExtractor.of(owner).append(codePoint);
        } else if (codePoint == LEFT_PARENTHESIS) {
            result = GroupExtractor.of(owner);
        } else if (codePoint == LEFT_SQUARE_BRACKET) {
            result = OptionExtractor.of(owner);
        } else if (codePoint == QUOTATION_MARK) {
            result = CharValExtractor.of(owner);
        } else if (codePoint == PERCENT_SIGN) {
            result = NumValExtractor.of(owner);
        } else {
            result = this;
        }
        return result;
    }
}
