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

import io.github.sebastiantoepfer.bnf4j.abnf.ABNF;
import io.github.sebastiantoepfer.bnf4j.abnf.RuleList;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.logging.Logger;

public final class StreambasedABNF implements ABNF {

    public static ABNF of(final Reader reader) {
        return new StreambasedABNF(reader);
    }

    private static final Logger LOG = Logger.getLogger(StreambasedABNF.class.getName());

    private final Reader reader;

    private StreambasedABNF(final Reader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public RuleList rules() {
        LOG.entering(StreambasedABNF.class.getName(), "rules");
        final RuleList result;
        try {
            Extractor extractor = RuleListExtractor.of();
            int codePoint;
            while ((codePoint = reader.read()) != -1) {
                extractor = extractor.append(codePoint);
            }
            result = extractor.finish().createAs(RuleList.class);
        } catch (IOException ex) {
            final IllegalArgumentException thrown = new IllegalArgumentException(ex);
            LOG.throwing(StreambasedABNF.class.getName(), "rules", thrown);
            throw thrown;
        }
        LOG.exiting(TextABNF.class.getName(), "rules", result);
        return result;
    }
}
