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
import java.util.logging.Logger;

public final class TextABNF implements ABNF {

    private static final Logger LOG = Logger.getLogger(TextABNF.class.getName());

    public static TextABNF of(final CharSequence rules) {
        return new TextABNF(rules);
    }

    private final CharSequence rules;

    private TextABNF(final CharSequence rules) {
        this.rules = rules;
    }

    @Override
    public void close() {
        //nothing to do!
    }

    @Override
    public RuleList rules() {
        LOG.entering(TextABNF.class.getName(), "rules");
        final RuleList result = rules
            .codePoints()
            .boxed()
            .reduce(RuleListExtractor.of(), Extractor::append, (l, r) -> null)
            .finish()
            .createAs(RuleList.class);
        LOG.exiting(TextABNF.class.getName(), "rules", result);
        return result;
    }
}
