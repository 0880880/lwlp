package com.github.zeroeighteightzero.lwlp;

public class ANDMatch extends CaseMatch {

    public CaseMatch[] caseMatches;

    public ANDMatch(String name, CaseMatch... caseMatches) {
        super(name);
        this.caseMatches = caseMatches;
    }

    @Override
    public int check(Parser parser, int offset) {
        int moves = 0;
        for (CaseMatch caseMatch : caseMatches) {
            int res = caseMatch.check(parser, offset + moves);
            if (res == 0) return 0;
            moves += res;
        }
        return moves;
    }

    @Override
    public Node create(Parser parser) {
        ListNode n = new ListNode(name);
        n.nodes = new Node[caseMatches.length];
        for (int i = 0; i < caseMatches.length; i++) {
            n.nodes[i] = caseMatches[i].create(parser);
        }
        return n;
    }
}
