package com.github.zeroeighteightzero.lwlp;


import java.time.Duration;
import java.time.Instant;

public class MathTest {

    public static void main(String[] args) {
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
        Instant start = Instant.now();
        Printer.print(parser.parse(lexer.reader("((91 + 82 - 73 + 64 * 55 - 46 + 37 * 28 - 19 + 10 / 5) * (108 + 207 - 306 + 405 * 504 - 603 + 702 / 801 - 900) / (200 - 150 + 100 * 75 / 25 + 50 - 25 + 10 * 5 - 1)) * ((31 * 22 - 13 + 44 * 55 - 66 + 77 * 88 - 99 + 111 / 3) - (120 * 5 + 140 - 160 * 2 + 180 - 200 / 50) + (210 - 220 + 230 * 3 - 240 + 250 * 4 - 260 + 270 * 5)) / ((81 * 92 - 73 + 64 * 55 - 46 + 37 * 28 - 19 + 20) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) - 3)\n")), 0);
        System.out.println("Time to Lex & Parse : " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }

}
