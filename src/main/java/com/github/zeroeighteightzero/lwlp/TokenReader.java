package com.github.zeroeighteightzero.lwlp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TokenReader {

    private final LinkedList<Token> tokens;

    public TokenReader(Token[] tokens) {
        this.tokens = new LinkedList<>();
        Collections.addAll(this.tokens, tokens);
    }

    public Token peek(int offset) {
        if (offset < 0 || offset >= tokens.size()) {
            return Token.NULL_TOKEN;
        }
        return tokens.get(offset);
    }

    public Token get() {
        if (tokens.isEmpty()) throw new NoSuchElementException("No tokens available");
        return tokens.removeFirst();
    }

    public int remaining() {
        return tokens.size();
    }

    @Override
    public String toString() {
        return tokens.toString();
    }

}