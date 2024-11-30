package com.github.zeroeighteightzero.lwlp;

public class DefMatch extends CaseMatch {

    public final String defName;

    public DefMatch(String name, String defName) {
        super(name);
        this.defName = defName;
    }

    @Override
    public int check(Parser parser, int offset) {
        //System.out.println(" > " + defName);
        int i = parser.findDefinition(defName).match.check(parser, offset);
        //System.out.println(defName + " " + i);
        return i;
    }

    @Override
    public Node create(Parser parser) {
        return parser.findDefinition(defName).match.create(parser);
    }
}
