# BNF4j
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=sebastian-toepfer_bnf4j)

# Indroduction
Lib to proccess [bnf](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) in java. the library should enable the use 
of bnf to validate (string-)values

# Usage

add dependency
```xml
<groupId>io.github.sebastian-toepfer.bnf4j</groupId>
<artifactId>bnf4j</artifactId>
<version>0.1.0</version>
```

Create an ABNF from string, each rule must be terminated with CRLF as required by spec.
```java
//ABNF as ABNF :); we use LF as a line separator, so we have to insert a CR to each line
ABNFs
    .of(
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
    )
    .rules()
```

Create an ABNF from InputStream like FileInputStream:
```java
final InputStream is = ...
ABNFs
    .of(
        is, StandardCharsets.UTF_8
    )
    .rules()
```
