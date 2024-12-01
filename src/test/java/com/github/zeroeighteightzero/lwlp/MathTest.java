package com.github.zeroeighteightzero.lwlp;


import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MathTest {

    private static float term(ListNode t) {
        Node f = t.get("value");
        float left;
        if (f == null)
            left = expression((ListNode) ((ListNode) t.get("parentheses_expression")).get("expression"));
        else
            left = Float.parseFloat((((TokenNode) f).value));
        Node mdn = t.get("multiply_or_divide");
        if (mdn == null) return left;
        ListNode md = (ListNode) mdn;
        for (int i = 0; i < md.nodes.length; i++) {
            ListNode opf = (ListNode) md.nodes[i];
            String op = ((TokenNode) opf.get("operator")).value;
            float right;
            f = opf.get("value");
            if (f == null)
                right = expression((ListNode) ((ListNode) opf.get("parentheses_expression")).get("expression"));
            else
                right = Float.parseFloat(((TokenNode) f).value);
            if ("*".equals(op)) left *= right;
            if ("/".equals(op)) {
                if (right == 0)
                    throw new ArithmeticException();
                left /= right;
            }
        }
        return left;
    }

    private static float expression(ListNode expr) {
        Node t = expr.get("term_expression");
        if (t == null) {
            Printer.print(expr, 4);
            throw new RuntimeException("Hay");
        }
        float left = term((ListNode) t);
        Node asn = expr.get("add_or_subtract");
        if (asn == null) return left;
        ListNode as = (ListNode) asn;
        for (int i = 0; i < as.nodes.length; i++) {
            ListNode opt = (ListNode) as.nodes[i];
            String op = ((TokenNode) opt.get("operator")).value;
            float right = term((ListNode) opt.get("term_expression"));
            if ("+".equals(op)) left += right;
            if ("-".equals(op)) left -= right;
        }
        return left;
    }

    private static Lexer createLexer() {
        return new Lexer(
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
    }

    private static Parser createParser() {
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
        parser.addListener((token, message) -> {
            throw new IllegalArgumentException(message);
        });
        return parser;
    }

    private static float calculate(String input) {
        Lexer lexer = createLexer();
        Parser parser = createParser();
        ListNode program = parser.parse(lexer.reader(input));
        ListNode expr = (ListNode) program.nodes[0];
        return expression(expr);
    }

    @Test
    public void testAddition() {
        float input = 2 + 2;
        float result = calculate("2 + 2");
        assertEquals(input, result, 0.0001, "Invalid result for addition");
    }

    @Test
    public void testSubtraction() {
        float input = 5 - 3;
        float result = calculate("5 - 3");
        assertEquals(input, result, 0.0001, "Invalid result for subtraction");
    }

    @Test
    public void testMultiplication() {
        float input = 4 * 3;
        float result = calculate("4 * 3");
        assertEquals(input, result, 0.0001, "Invalid result for multiplication");
    }

    @Test
    public void testDivision() {
        float input = 10 / 2;
        float result = calculate("10 / 2");
        assertEquals(input, result, 0.0001, "Invalid result for division");
    }

    @Test
    public void testOperatorPrecedence() {
        float input = 2 + 3 * 4;
        float result = calculate("2 + 3 * 4");
        assertEquals(input, result, 0.0001, "Invalid result for operator precedence");
    }

    @Test
    public void testParentheses() {
        float input = (2 + 3) * 4;
        float result = calculate("(2 + 3) * 4");
        assertEquals(input, result, 0.0001, "Invalid result for parentheses");
    }

    @Test
    public void testNestedParentheses() {
        float input = ((2 + 3) * (4 - 1)) / 3f;
        float result = calculate("((2 + 3) * (4 - 1)) / 3");
        assertEquals(input, result, 0.0001, "Invalid result for nested parentheses");
    }

    @Test
    public void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> calculate("5 / 0"), "Expected division by zero exception");
    }

    @Test
    public void testComplexExpression() {
        float input = (3 + 5) * 2 - (4 / 2f);
        float result = calculate("(3 + 5) * 2 - (4 / 2)");
        assertEquals(input, result, 0.0001, "Invalid result for complex expression");
    }

    @Test
    public void testExtraSpaces() {
        float input = 2 + 2;
        float result = calculate("  2   +  2  ");
        assertEquals(input, result, 0.0001, "Invalid result for extra spaces");
    }

    @Test
    public void testInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> calculate("2 + * 3"), "Expected exception for invalid expression");
    }

    @Test
    public void testMathLong() {
        float input = ((91 + 82 - 73 + 64 * 55 - 46 + 37 * 28 - 19 + 10 / 5f) * (108 + 207 - 306 + 405 * 504 - 603 + 702 / 801f - 900) / (200 - 150 + 100 * 75 / 25f + 50 - 25 + 10 * 5 - 1)) * ((31 * 22 - 13 + 44 * 55 - 66 + 77 * 88 - 99 + 111 / 3f) - (120 * 5 + 140 - 160 * 2 + 180 - 200 / 50f) + (210 - 220 + 230 * 3 - 240 + 250 * 4 - 260 + 270 * 5)) / ((81 * 92 - 73 + 64 * 55 - 46 + 37 * 28 - 19 + 20) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3f) + (50 * 60 - 70 + 80 * 90 - 100) - 3);
        float result = calculate("((91 + 82 - 73 + 64 * 55 - 46 + 37 * 28 - 19 + 10 / 5) * (108 + 207 - 306 + 405 * 504 - 603 + 702 / 801 - 900) / (200 - 150 + 100 * 75 / 25 + 50 - 25 + 10 * 5 - 1)) * ((31 * 22 - 13 + 44 * 55 - 66 + 77 * 88 - 99 + 111 / 3) - (120 * 5 + 140 - 160 * 2 + 180 - 200 / 50) + (210 - 220 + 230 * 3 - 240 + 250 * 4 - 260 + 270 * 5)) / ((81 * 92 - 73 + 64 * 55 - 46 + 37 * 28 - 19 + 20) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) * (91 + 102 - 113 + 124 * 135 - 146 + 157 / 3) + (50 * 60 - 70 + 80 * 90 - 100) - 3)\n");
        assertEquals(input, result, "Invalid result for long expression");
    }

}
