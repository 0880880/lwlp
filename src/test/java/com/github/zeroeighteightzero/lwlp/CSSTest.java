package com.github.zeroeighteightzero.lwlp;


import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class CSSTest {

    public static void main(String[] args) {
        Lexer lexer = new Lexer(
                new TokenPattern[] {
                        new TokenPattern("\\{", "LBRACE"),
                        new TokenPattern("\\}", "RBRACE"),
                        new TokenPattern("[\\w-]+", "IDENTIFIER"),
                        new TokenPattern("\\d+(\\.\\d+)?[a-zA-Z]*", "NUMBER"),
                        new TokenPattern("#[\\w-]+", "HASH"),
                        new TokenPattern("\\.[\\w-]+", "CLASS"),
                        new TokenPattern("\\,", "COMMA"),
                        new TokenPattern("\\:", "COLON"),
                        new TokenPattern("\\;", "SEMICOLON"),
                        new TokenPattern("\\[[-_a-zA-Z0-9]+(?:([~|^$*]?=)[\"'][^\"']*[\"'])?\\]", "ATTR"),
                        new TokenPattern("\\:[_a-zA-Z-]+", "PSEUDOCLASS"),
                        new TokenPattern("\\:\\:[_a-zA-Z-]+", "PSEUDOELEM"),
                        new TokenPattern("[>~+]", "COMBINATOR"),
                }
        );

        Instant start = Instant.now();
        List<Token> tokens = lexer.lex("body { background-color: lightblue; } h1 { color: darkblue; font-size: 24px; } p { color: black; font-family: Arial, sans-serif; }");
        System.out.println("Lexer: Took " + Duration.between(start, Instant.now()).toMillis() + "ms");
        System.out.println(tokens);

        Parser parser = new Parser(
                new Definition("val",
                        new ORMatch(
                                new TokenMatch("value", "IDENTIFIER"),
                                new TokenMatch("value", "NUMBER")
                        ),
                        false
                ),
                new Definition("simple_selector",
                        new OrderedMinMatch("simple_selector", 1,
                                new ORMatch(
                                        new TokenMatch("tag", "IDENTIFIER"),
                                        new TokenMatch("id", "HASH"),
                                        new TokenMatch("class", "CLASS"),
                                        new TokenMatch("universal", "UNIVERSAL"),
                                        new TokenMatch("root", "ROOT")
                                ),
                                MultiMatch.min("attributes",
                                        new TokenMatch("attribute", "ATTR"),
                                        0
                                )
                        ),
                        false
                ),
                new Definition("compound_selector",
                        new OrderedMinMatch("compound_selector", 1,
                                new DefMatch("simple_selector", "simple_selector"),
                                MultiMatch.min("additional_selectors",
                                        new ORMatch(
                                                new DefMatch("simple_selector", "simple_selector"),
                                                new TokenMatch("pseudo_class", "PSEUDOCLASS")
                                        ),
                                        0
                                ),
                                MultiMatch.min("pseudo_element",
                                        new TokenMatch("pseudo_element", "PSEUDOELEMENT"),
                                        0
                                )
                        ),
                        false
                ),
                new Definition("selector",
                        new OrderedMinMatch("selector", 1,
                                new DefMatch("compound_selector", "compound_selector"),
                                MultiMatch.min("combinator_and_compound_selector",
                                        new ANDMatch("combinator_with_selector",
                                                new TokenMatch("combinator", "COMBINATOR"),
                                                new DefMatch("compound_selector", "compound_selector")
                                        ),
                                        0
                                )
                        ),
                        false
                ),
                new Definition("property",
                        new ANDMatch("property",
                                new TokenMatch("property_name", "IDENTIFIER"),
                                new TokenMatch("colon", "COLON"),
                                new OrderedMinMatch("values", 1,
                                        new DefMatch("value", "val"),
                                        MultiMatch.min("_",
                                                new ANDMatch("",
                                                        new TokenMatch("_", "COMMA"),
                                                        new DefMatch("value", "val")
                                                ),
                                                0)
                                        ),
                                new TokenMatch("semicolon", "SEMICOLON")
                        ),
                        false
                ),
                new Definition("rule",
                        new OrderedMinMatch("rule", 1,
                                new DefMatch("selector", "selector"),
                                new TokenMatch("lbrace", "LBRACE"),
                                MultiMatch.min("properties",
                                        new DefMatch("property", "property"),
                                        0
                                ),
                                new TokenMatch("rbrace", "RBRACE")
                        ),
                        true
                )
        );

        parser.parse(tokens);

    }

}
