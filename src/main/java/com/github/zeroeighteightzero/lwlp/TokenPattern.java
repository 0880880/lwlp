package com.github.zeroeighteightzero.lwlp;

class TokenPattern {
    public final String regex;
    public final String name;
    public String contentGroup;

    public TokenPattern(String regex, String name) {
        this.regex = regex;
        this.name = name;
    }

    public TokenPattern(String regex, String name, String contentGroup) {
        this.regex = regex;
        this.name = name;
        this.contentGroup = contentGroup;
    }
}
