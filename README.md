# Lightweight lexer and parser
A library for writing lexers and parsers

## Lexer:
```java
Lexer lexer = new Lexer(
        new TokenPattern[]{
                new TokenPattern("[0-9]+", "INT"),
                new TokenPattern("\\+", "PLUS"),
                new TokenPattern("\\-", "MINUS"),
                new TokenPattern("\\*", "MULTIPLY"),
                new TokenPattern("\\/", "DIVIDE"),
                new TokenPattern("\\(", "LPAREN"),
                new TokenPattern("\\)", "RPAREN")
        }
);
```

## Parser:
```java
Parser parser = new Parser(
        new Definition("expr",
                new OrderedMinMatch("expression", 1,
                        new DefMatch("left", "term"),
                        MultiMatch.min("add_or_subtract",
                                new ANDMatch("operation_and_term",
                                        new ORMatch(
                                                new TokenMatch("operator", "PLUS"),
                                                new TokenMatch("operator", "MINUS")
                                        ),
                                        new DefMatch("right", "term")
                                ),
                                1
                        )
                )
                , true),
        new Definition("term",
                new OrderedMinMatch("term_expression", 1,
                        new DefMatch("left_t", "factor"),
                        MultiMatch.min("multiply_or_divide",
                                new ANDMatch("operation_and_factor",
                                        new ORMatch(
                                                new TokenMatch("operator", "MULTIPLY"),
                                                new TokenMatch("operator", "DIVIDE")
                                        ),
                                        new DefMatch("right_t", "factor")
                                ),
                                1
                        )
                )
                , false),
        new Definition("factor",
                new ORMatch(
                        new TokenMatch("value", "INT"),
                        new ANDMatch("parentheses_expression",
                                new TokenMatch("", "LPAREN"),
                                new DefMatch("expr", "expr"),
                                new TokenMatch("", "RPAREN")
                        )
                )
                , false)
);
```

## Examples
- A basic math lexer, parser, and interpreter example [MathTest](https://github.com/0880880/lwlp/blob/main/src/test/java/com/github/zeroeighteightzero/lwlp/MathTest.java)
- A basic CSS lexer, and parser example [CSSTest](https://github.com/0880880/lwlp/blob/main/src/test/java/com/github/zeroeighteightzero/lwlp/CSSTest.java)

## GWT Support
Since this library relies on regex, the Lexer class is not compatible with GWT. However, there is a [GWTLexer](https://gist.github.com/0880880/f9d2c903a294cc81d39c3e6d8ed3f964) class that utilizes [RegExodus](https://github.com/tommyettinger/RegExodus).

This project is licensed under the MIT License.