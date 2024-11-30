package com.github.zeroeighteightzero.lwlp;

public abstract class CaseMatch {

    public String name;

    public CaseMatch(String name) {
        this.name = name;
    }

    public abstract int check(Parser parser, int offset);

    public abstract Node create(Parser parser);

}
