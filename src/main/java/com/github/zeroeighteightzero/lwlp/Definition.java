package com.github.zeroeighteightzero.lwlp;

public class Definition {

    public String name;
    public boolean visible = true;

    public CaseMatch match;

    public Definition(String name, CaseMatch match) {
        this.name = name;
        this.match = match;
    }

    public Definition(String name, CaseMatch match, boolean visible) {
        this.name = name;
        this.match = match;
        this.visible = visible;
    }

    public int check(Parser parser) {
        return match.check(parser, 0);
    }

}
