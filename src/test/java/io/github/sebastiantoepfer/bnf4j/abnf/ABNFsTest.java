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
package io.github.sebastiantoepfer.bnf4j.abnf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.bnf4j.abnf.element.Alternative;
import io.github.sebastiantoepfer.bnf4j.abnf.element.Concatenation;
import io.github.sebastiantoepfer.bnf4j.abnf.element.NumericCharacter;
import io.github.sebastiantoepfer.bnf4j.abnf.element.OptionalSequence;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleName;
import io.github.sebastiantoepfer.bnf4j.abnf.element.RuleReference;
import io.github.sebastiantoepfer.bnf4j.abnf.element.SequenceGroup;
import io.github.sebastiantoepfer.bnf4j.abnf.element.StringElement;
import io.github.sebastiantoepfer.bnf4j.abnf.element.ValueRangeAlternatives;
import io.github.sebastiantoepfer.bnf4j.abnf.element.VariableRepetition;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class ABNFsTest {

    @Test
    void should_create_abnf_rules_from_string() {
        //Note: a rule MUST end with crlf!! see rfc5234. lf is already set by the formatting :)
        assertThat(
            ABNFs.of(
                """
                rulelist       =  1*( rule / (*c-wsp c-nl) )\r
                rule           =  rulename defined-as elements c-nl\r
                rulename       =  ALPHA *(ALPHA / DIGIT / "-")\r
                defined-as     =  *c-wsp ("=" / "=/") *c-wsp\r
                elements       =  alternation *c-wsp\r
                c-wsp          =  WSP / (c-nl WSP)\r
                c-nl           =  comment / CRLF\r
                comment        =  ";" *(WSP / VCHAR) CRLF\r
                alternation    =  concatenation\r
                                  *(*c-wsp "/" *c-wsp concatenation)\r
                concatenation  =  repetition *(1*c-wsp repetition)\r
                repetition     =  [repeat] element\r
                repeat         =  1*DIGIT / (*DIGIT "*" *DIGIT)\r
                element        =  rulename / group / option /\r
                                  char-val / num-val / prose-val\r
                group          =  "(" *c-wsp alternation *c-wsp ")"\r
                option         =  "[" *c-wsp alternation *c-wsp "]"\r
                char-val       =  DQUOTE *(%x20-21 / %x23-7E) DQUOTE\r
                num-val        =  "%" (bin-val / dec-val / hex-val)\r
                bin-val        =  "b" 1*BIT\r
                                  [ 1*("." 1*BIT) / ("-" 1*BIT) ]\r
                dec-val        =  "d" 1*DIGIT\r
                                  [ 1*("." 1*DIGIT) / ("-" 1*DIGIT) ]\r
                hex-val        =  "x" 1*HEXDIG\r
                                  [ 1*("." 1*HEXDIG) / ("-" 1*HEXDIG) ]\r
                """
            ).rules(),
            isAbnfSpecAsRuleList()
        );
    }

    @Test
    void should_create_abnf_rules_from_inputstream() {
        assertThat(
            ABNFs.of(ABNFs.class.getClassLoader().getResourceAsStream("ABNFspec.txt"), StandardCharsets.UTF_8).rules(),
            isAbnfSpecAsRuleList()
        );
    }

    private static Matcher<RuleList> isAbnfSpecAsRuleList() {
        return is(
            RuleList.of(
                Rule.of(
                    RuleName.of("rulelist"),
                    VariableRepetition.ofAtLeast(
                        SequenceGroup.of(
                            Alternative.of(
                                RuleReference.of(RuleName.of("rule")),
                                SequenceGroup.of(
                                    Concatenation.of(
                                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                                        RuleReference.of(RuleName.of("c-nl"))
                                    )
                                )
                            )
                        ),
                        1
                    )
                ),
                Rule.of(
                    RuleName.of("rule"),
                    Concatenation.of(
                        RuleReference.of(RuleName.of("rulename")),
                        RuleReference.of(RuleName.of("defined-as")),
                        RuleReference.of(RuleName.of("elements")),
                        RuleReference.of(RuleName.of("c-nl"))
                    )
                ),
                Rule.of(
                    RuleName.of("rulename"),
                    Concatenation.of(
                        RuleReference.of(CoreRules.ALPHA.asRuleName()),
                        VariableRepetition.of(
                            SequenceGroup.of(
                                Alternative.of(
                                    RuleReference.of(CoreRules.ALPHA.asRuleName()),
                                    RuleReference.of(CoreRules.DIGIT.asRuleName()),
                                    StringElement.of("-")
                                )
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("defined-as"),
                    Concatenation.of(
                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                        SequenceGroup.of(Alternative.of(StringElement.of("="), StringElement.of("=/"))),
                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp")))
                    )
                ),
                Rule.of(
                    RuleName.of("elements"),
                    Concatenation.of(
                        RuleReference.of(RuleName.of("alternation")),
                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp")))
                    )
                ),
                Rule.of(
                    RuleName.of("c-wsp"),
                    Alternative.of(
                        RuleReference.of(RuleName.of("WSP")),
                        SequenceGroup.of(
                            Concatenation.of(
                                RuleReference.of(RuleName.of("c-nl")),
                                RuleReference.of(CoreRules.WSP.asRuleName())
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("c-nl"),
                    Alternative.of(
                        RuleReference.of(RuleName.of("comment")),
                        RuleReference.of(CoreRules.CRLF.asRuleName())
                    )
                ),
                Rule.of(
                    RuleName.of("comment"),
                    Concatenation.of(
                        StringElement.of(";"),
                        VariableRepetition.of(
                            SequenceGroup.of(
                                Alternative.of(
                                    RuleReference.of(CoreRules.WSP.asRuleName()),
                                    RuleReference.of(CoreRules.VCHAR.asRuleName())
                                )
                            )
                        ),
                        RuleReference.of(CoreRules.CRLF.asRuleName())
                    )
                ),
                Rule.of(
                    RuleName.of("alternation"),
                    Concatenation.of(
                        RuleReference.of(RuleName.of("concatenation")),
                        VariableRepetition.of(
                            SequenceGroup.of(
                                Concatenation.of(
                                    VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                                    StringElement.of("/"),
                                    VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                                    RuleReference.of(RuleName.of("concatenation"))
                                )
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("concatenation"),
                    Concatenation.of(
                        RuleReference.of(RuleName.of("repetition")),
                        VariableRepetition.of(
                            SequenceGroup.of(
                                Concatenation.of(
                                    VariableRepetition.ofAtLeast(RuleReference.of(RuleName.of("c-wsp")), 1),
                                    RuleReference.of(RuleName.of("repetition"))
                                )
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("repetition"),
                    Concatenation.of(
                        OptionalSequence.of(RuleReference.of(RuleName.of("repeat"))),
                        RuleReference.of(RuleName.of("element"))
                    )
                ),
                Rule.of(
                    RuleName.of("repeat"),
                    Alternative.of(
                        VariableRepetition.ofAtLeast(RuleReference.of(CoreRules.DIGIT.asRuleName()), 1),
                        SequenceGroup.of(
                            Concatenation.of(
                                VariableRepetition.of(RuleReference.of(CoreRules.DIGIT.asRuleName())),
                                StringElement.of("*"),
                                VariableRepetition.of(RuleReference.of(CoreRules.DIGIT.asRuleName()))
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("element"),
                    Alternative.of(
                        RuleReference.of(RuleName.of("rulename")),
                        RuleReference.of(RuleName.of("group")),
                        RuleReference.of(RuleName.of("option")),
                        RuleReference.of(RuleName.of("char-val")),
                        RuleReference.of(RuleName.of("num-val")),
                        RuleReference.of(RuleName.of("prose-val"))
                    )
                ),
                Rule.of(
                    RuleName.of("group"),
                    Concatenation.of(
                        StringElement.of("("),
                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                        RuleReference.of(RuleName.of("alternation")),
                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                        StringElement.of(")")
                    )
                ),
                Rule.of(
                    RuleName.of("option"),
                    Concatenation.of(
                        StringElement.of("["),
                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                        RuleReference.of(RuleName.of("alternation")),
                        VariableRepetition.of(RuleReference.of(RuleName.of("c-wsp"))),
                        StringElement.of("]")
                    )
                ),
                Rule.of(
                    RuleName.of("char-val"),
                    Concatenation.of(
                        RuleReference.of(CoreRules.DQUOTE.asRuleName()),
                        VariableRepetition.of(
                            SequenceGroup.of(
                                Alternative.of(
                                    ValueRangeAlternatives.of(
                                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x20),
                                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x21)
                                    ),
                                    ValueRangeAlternatives.of(
                                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x23),
                                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7E)
                                    )
                                )
                            )
                        ),
                        RuleReference.of(CoreRules.DQUOTE.asRuleName())
                    )
                ),
                Rule.of(
                    RuleName.of("num-val"),
                    Concatenation.of(
                        StringElement.of("%"),
                        SequenceGroup.of(
                            Alternative.of(
                                RuleReference.of(RuleName.of("bin-val")),
                                RuleReference.of(RuleName.of("dec-val")),
                                RuleReference.of(RuleName.of("hex-val"))
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("bin-val"),
                    Concatenation.of(
                        StringElement.of("b"),
                        VariableRepetition.ofAtLeast(RuleReference.of(CoreRules.BIT.asRuleName()), 1),
                        OptionalSequence.of(
                            Alternative.of(
                                VariableRepetition.ofAtLeast(
                                    SequenceGroup.of(
                                        Concatenation.of(
                                            StringElement.of("."),
                                            VariableRepetition.ofAtLeast(
                                                RuleReference.of(CoreRules.BIT.asRuleName()),
                                                1
                                            )
                                        )
                                    ),
                                    1
                                ),
                                SequenceGroup.of(
                                    Concatenation.of(
                                        StringElement.of("-"),
                                        VariableRepetition.ofAtLeast(RuleReference.of(CoreRules.BIT.asRuleName()), 1)
                                    )
                                )
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("dec-val"),
                    Concatenation.of(
                        StringElement.of("d"),
                        VariableRepetition.ofAtLeast(RuleReference.of(CoreRules.DIGIT.asRuleName()), 1),
                        OptionalSequence.of(
                            Alternative.of(
                                VariableRepetition.ofAtLeast(
                                    SequenceGroup.of(
                                        Concatenation.of(
                                            StringElement.of("."),
                                            VariableRepetition.ofAtLeast(
                                                RuleReference.of(CoreRules.DIGIT.asRuleName()),
                                                1
                                            )
                                        )
                                    ),
                                    1
                                ),
                                SequenceGroup.of(
                                    Concatenation.of(
                                        StringElement.of("-"),
                                        VariableRepetition.ofAtLeast(RuleReference.of(CoreRules.DIGIT.asRuleName()), 1)
                                    )
                                )
                            )
                        )
                    )
                ),
                Rule.of(
                    RuleName.of("hex-val"),
                    Concatenation.of(
                        StringElement.of("x"),
                        VariableRepetition.ofAtLeast(RuleReference.of(CoreRules.HEXDIG.asRuleName()), 1),
                        OptionalSequence.of(
                            Alternative.of(
                                VariableRepetition.ofAtLeast(
                                    SequenceGroup.of(
                                        Concatenation.of(
                                            StringElement.of("."),
                                            VariableRepetition.ofAtLeast(
                                                RuleReference.of(CoreRules.HEXDIG.asRuleName()),
                                                1
                                            )
                                        )
                                    ),
                                    1
                                ),
                                SequenceGroup.of(
                                    Concatenation.of(
                                        StringElement.of("-"),
                                        VariableRepetition.ofAtLeast(RuleReference.of(CoreRules.HEXDIG.asRuleName()), 1)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }
}
