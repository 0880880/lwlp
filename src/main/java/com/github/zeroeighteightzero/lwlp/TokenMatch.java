package com.github.zeroeighteightzero.lwlp;

public class TokenMatch extends CaseMatch {

    public final String type;

    public TokenMatch(String name, String type) {
        super(name);
        this.type = type;
    }

    @Override
    public int check(Parser parser, int offset) {
        Token r = parser.read(offset);
        if (r == null) return 0;
        //System.out.println((r.type.equals(type) ? 1 : 0) + "  " + type + " YAYA");
        return r.type.equals(type) ? 1 : 0;
    }

    @Override
    public Node create(Parser parser) {
        Token t = parser.eat();
        TokenNode n = new TokenNode(name);
        n.type = t.type;
        n.value = t.value;
        return n;
    }
}
