package com.github.zeroeighteightzero.lwlp;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CSSTest {

    private static CaseMatch whitespaceCaseFLR(CaseMatch caseMatch) {
        return new ORMatch(
                new ANDMatch("_",
                        new TokenMatch("_", "WHITESPACE"),
                        caseMatch,
                        new TokenMatch("_", "WHITESPACE")
                ),
                new ANDMatch("_",
                        new TokenMatch("_", "WHITESPACE"),
                        caseMatch
                ),
                new ANDMatch("_",
                        caseMatch,
                        new TokenMatch("_", "WHITESPACE")
                ),
                caseMatch
        );
    }

    private static CaseMatch whitespaceTokenFLR(String name, String type) {
        return whitespaceCaseFLR(new TokenMatch(name, type));
    }

    public static String readResourceFile(String fileName) {
        InputStream inputStream = CSSTest.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) throw new IllegalArgumentException("File not found: " + fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    public static void test(String fileName) {
        Lexer lexer = new Lexer(
                new TokenPattern[]{
                        new TokenPattern("\\/\\*[\\s\\S]*?\\*\\/", "COMMENT").ignore(),
                        new TokenPattern("\\s+", "WHITESPACE"),
                        new TokenPattern("\\{", "LBRACE"),
                        new TokenPattern("\\}", "RBRACE"),
                        new TokenPattern("\\[", "LBRACKET"),
                        new TokenPattern("\\]", "RBRACKET"),
                        new TokenPattern("(?:\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"|'([^'\\\\]*(\\\\.[^'\\\\]*)*)')", "STRING"),
                        new TokenPattern("[^\\W\\d][\\w-]*", "IDENTIFIER"),
                        new TokenPattern("\\d+(\\.\\d+)?", "NUMBER"),
                        new TokenPattern("#[\\w-]+", "HASH"),
                        new TokenPattern("\\.[\\w-]+", "CLASS"),
                        new TokenPattern("\\,", "COMMA"),
                        new TokenPattern("\\:", "COLON"),
                        new TokenPattern("\\;", "SEMICOLON"),
                        new TokenPattern("\\=", "EQUALS"),
                        new TokenPattern("[>~+]", "COMBINATOR"),
                }
        );

        List<Token> tokens = lexer.lex(readResourceFile(fileName));

        Parser parser = new Parser(
                new Definition("val",
                        new ORMatch(
                                new TokenMatch("value_id", "IDENTIFIER"),
                                new ANDMatch("value_unit",
                                        new TokenMatch("value", "NUMBER"),
                                        new TokenMatch("unit", "IDENTIFIER")
                                ),
                                new TokenMatch("value_num", "NUMBER"),
                                new TokenMatch("value_str", "STRING")
                        ),
                        false
                ),
                new Definition("simple_selector",
                        new OrderedMinMatch("simple_selector", 1,
                                new ORMatch(
                                        new TokenMatch("tag", "IDENTIFIER"),
                                        new TokenMatch("id", "HASH"),
                                        new TokenMatch("class", "CLASS"),
                                        new ANDMatch("pseudo_class",
                                                new TokenMatch("_", "COLON"),
                                                new TokenMatch("class", "IDENTIFIER")
                                        ),
                                        new ANDMatch("attribute",
                                                new ORMatch(
                                                        new ANDMatch("_",
                                                                new TokenMatch("_", "LBRACKET"),
                                                                new TokenMatch("key", "IDENTIFIER"),
                                                                new TokenMatch("equals", "EQUALS"),
                                                                new DefMatch("value", "val"),
                                                                new TokenMatch("_", "RBRACKET")
                                                        ),
                                                        new ANDMatch("_",
                                                                new TokenMatch("_", "LBRACKET"),
                                                                new TokenMatch("key", "IDENTIFIER"),
                                                                new TokenMatch("_", "RBRACKET")
                                                        )
                                                )
                                        )
                                )
                        ),
                        false
                ),
                new Definition("compound_selector",
                        new OrderedMinMatch("compound_selector", 1,
                                new DefMatch("simple_selector", "simple_selector"),
                                MultiMatch.min("additional_selectors",
                                        new DefMatch("simple_selector", "simple_selector"),
                                        0
                                ),
                                new ANDMatch("pseudo_element",
                                        new TokenMatch("_", "COLON"),
                                        new TokenMatch("_", "COLON"),
                                        new TokenMatch("element", "IDENTIFIER")
                                )
                        ),
                        false
                ),
                new Definition("selector",
                        new OrderedMinMatch("selector", 1,
                                new DefMatch("compound_selector", "compound_selector"),
                                MultiMatch.min("combinator_and_compound_selector",
                                        new ORMatch(
                                                new ANDMatch("combinator_with_selector",
                                                        new ORMatch(
                                                                new TokenMatch("combinator", "COMBINATOR"),
                                                                new TokenMatch("_", "WHITESPACE")
                                                        ),
                                                        new DefMatch("compound_selector", "compound_selector")
                                                ),
                                                new DefMatch("compound_selector", "compound_selector")
                                        ),
                                        0
                                )
                        ),
                        false
                ),
                new Definition("property",
                        new ANDMatch("property",
                                whitespaceTokenFLR("property_name", "IDENTIFIER"),
                                whitespaceTokenFLR("colon", "COLON"),
                                new OrderedMinMatch("values", 1,
                                        new DefMatch("value", "val"),
                                        MultiMatch.min("_",
                                                new ANDMatch("",
                                                        new ORMatch(
                                                                new ANDMatch("_",
                                                                        new TokenMatch("_", "COMMA"),
                                                                        new TokenMatch("_", "WHITESPACE")
                                                                ),
                                                                new TokenMatch("_", "WHITESPACE")
                                                        ),
                                                        new DefMatch("value", "val")
                                                ),
                                                0)
                                ),
                                whitespaceTokenFLR("semicolon", "SEMICOLON")
                        ),
                        false
                ),
                new Definition("rule",
                        whitespaceCaseFLR(
                                new OrderedMinMatch("rule", 1,
                                        new DefMatch("selector", "selector"),
                                        whitespaceTokenFLR("lbrace", "LBRACE"),
                                        MultiMatch.min("properties",
                                                new DefMatch("property", "property"),
                                                0
                                        ),
                                        whitespaceTokenFLR("rbrace", "RBRACE")
                                )
                        ),
                        true
                )
        );

        parser.parse(tokens);

    }

    @Test
    public void cssTest0() {
        assertDoesNotThrow(() -> {
            test("css_test0.css");
        });
    }

    @Test
    public void cssTest1() {
        assertDoesNotThrow(() -> {
            test("css_test1.css");
        });
    }

    @Test
    public void cssTest2() {
        assertDoesNotThrow(() -> {
            test("css_test2.css");
        });
    }

}
