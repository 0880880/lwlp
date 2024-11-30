package com.github.zeroeighteightzero.lwlp;

public class ORMatch extends CaseMatch {

    public CaseMatch[] caseMatches;

    public ORMatch(CaseMatch... caseMatches) {
        super("_");
        this.caseMatches = caseMatches;
    }

    @Override
    public int check(Parser parser, int offset) {
        for (CaseMatch caseMatch : caseMatches) {
            int res = caseMatch.check(parser, offset);
            if (res != 0) return res;
        }
        return 0;
    }

    @Override
    public Node create(Parser parser) {
        for (CaseMatch caseMatch : caseMatches) {
            int res = caseMatch.check(parser, 0);
            if (res != 0) return caseMatch.create(parser);
        }
        return null;
    }
}
