package com.github.zeroeighteightzero.lwlp;

public class TokenPattern {
    public final String regex;
    public final String name;
    public String contentGroup;
    public boolean ignore;

    public TokenPattern(String regex, String name) {
        this.regex = regex;
        this.name = name;
    }

    public TokenPattern(String regex, String name, String contentGroup) {
        this.regex = regex;
        this.name = name;
        this.contentGroup = contentGroup;
    }

    public TokenPattern ignore() {
        this.ignore = true;
        return this;
    }

}
