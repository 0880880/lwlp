package com.github.zeroeighteightzero.lwlp;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    private void assertTokens(Lexer lexer, String input, String... expectedTokens) {
        List<Token> tokens = lexer.lex(input);
        assertEquals(expectedTokens.length, tokens.size(), "Token count mismatch");

        for (int i = 0; i < expectedTokens.length; i++) {
            assertEquals(expectedTokens[i], tokens.get(i).getType(), "Token type mismatch at index " + i);
        }
    }

    @Test
    public void testIntegerTokens() {
        Lexer lexer = new Lexer(new TokenPattern[]{
                new TokenPattern("[0-9]+", "INT"),
                new TokenPattern("\\+", "PLUS"),
                new TokenPattern("\\-", "MINUS"),
                new TokenPattern("\\*", "MULTIPLY"),
                new TokenPattern("\\/", "DIVIDE"),
                new TokenPattern("\\(", "LPAREN"),
                new TokenPattern("\\)", "RPAREN")
        });
        assertTokens(lexer, "123", "INT");
        assertTokens(lexer, "45 67", "INT", "INT");
    }

    @Test
    public void testSingleOperatorTokens() {
        Lexer lexer = new Lexer(new TokenPattern[]{
                new TokenPattern("[0-9]+", "INT"),
                new TokenPattern("\\+", "PLUS"),
                new TokenPattern("\\-", "MINUS"),
                new TokenPattern("\\*", "MULTIPLY"),
                new TokenPattern("\\/", "DIVIDE"),
                new TokenPattern("\\(", "LPAREN"),
                new TokenPattern("\\)", "RPAREN")
        });
        assertTokens(lexer, "+", "PLUS");
        assertTokens(lexer, "-", "MINUS");
        assertTokens(lexer, "*", "MULTIPLY");
        assertTokens(lexer, "/", "DIVIDE");
    }

    @Test
    public void testParenthesisTokens() {
        Lexer lexer = new Lexer(new TokenPattern[]{
                new TokenPattern("[0-9]+", "INT"),
                new TokenPattern("\\+", "PLUS"),
                new TokenPattern("\\-", "MINUS"),
                new TokenPattern("\\*", "MULTIPLY"),
                new TokenPattern("\\/", "DIVIDE"),
                new TokenPattern("\\(", "LPAREN"),
                new TokenPattern("\\)", "RPAREN")
        });
        assertTokens(lexer, "(", "LPAREN");
        assertTokens(lexer, ")", "RPAREN");
    }

    @Test
    public void testSimpleExpression() {
        Lexer lexer = new Lexer(new TokenPattern[]{
                new TokenPattern("[0-9]+", "INT"),
                new TokenPattern("\\+", "PLUS"),
                new TokenPattern("\\-", "MINUS"),
                new TokenPattern("\\*", "MULTIPLY"),
                new TokenPattern("\\/", "DIVIDE"),
                new TokenPattern("\\(", "LPAREN"),
                new TokenPattern("\\)", "RPAREN")
        });
        assertTokens(lexer, "2+2", "INT", "PLUS", "INT");
        assertTokens(lexer, "(3-1)*5", "LPAREN", "INT", "MINUS", "INT", "RPAREN", "MULTIPLY", "INT");
    }

    @Test
    public void testComplexExpression() {
        Lexer lexer = new Lexer(new TokenPattern[]{
                new TokenPattern("[0-9]+", "INT"),
                new TokenPattern("\\+", "PLUS"),
                new TokenPattern("\\-", "MINUS"),
                new TokenPattern("\\*", "MULTIPLY"),
                new TokenPattern("\\/", "DIVIDE"),
                new TokenPattern("\\(", "LPAREN"),
                new TokenPattern("\\)", "RPAREN")
        });
        assertTokens(lexer, "12+(4*5)-7/2", "INT", "PLUS", "LPAREN", "INT", "MULTIPLY", "INT", "RPAREN", "MINUS", "INT", "DIVIDE", "INT");
    }

    @Test
    public void testWhitespaceHandling() {
        Lexer lexer = new Lexer(new TokenPattern[]{
                new TokenPattern("[0-9]+", "INT"),
                new TokenPattern("\\+", "PLUS"),
                new TokenPattern("\\-", "MINUS"),
                new TokenPattern("\\*", "MULTIPLY"),
                new TokenPattern("\\/", "DIVIDE"),
                new TokenPattern("\\(", "LPAREN"),
                new TokenPattern("\\)", "RPAREN")
        });
        assertTokens(lexer, " 3 + 4 ", "INT", "PLUS", "INT");
        assertTokens(lexer, "( 10 - 3 ) * 2 ", "LPAREN", "INT", "MINUS", "INT", "RPAREN", "MULTIPLY", "INT");
    }
}
