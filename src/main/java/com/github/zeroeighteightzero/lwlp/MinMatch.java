package com.github.zeroeighteightzero.lwlp;

public class MinMatch extends CaseMatch {

    public CaseMatch[] caseMatches;
    public int min;

    public MinMatch(String name, int min, CaseMatch... caseMatches) {
        super(name);
        this.min = min;
        this.caseMatches = caseMatches;
    }

    @Override
    public int check(Parser parser, int offset) {
        int moves = 0;
        int count = 0;
        for (CaseMatch caseMatch : caseMatches) {
            int res = caseMatch.check(parser, offset + moves);
            moves += res;
            count++;
        }
        if (count < min) return 0;
        return moves;
    }

    @Override
    public Node create(Parser parser) {
        ListNode n = new ListNode(name);
        int moves = 0;
        int count = 0;
        for (CaseMatch caseMatch : caseMatches) {
            int res = caseMatch.check(parser, moves);
            moves += res;
            count++;
        }
        n.nodes = new Node[count];
        moves = 0;
        count = 0;
        for (CaseMatch caseMatch : caseMatches) {
            int res = caseMatch.check(parser, 0);
            moves += res;
            if (res != 0) n.nodes[count] = caseMatch.create(parser);
            count++;
            if (n.nodes.length == count) return n;
        }
        return n;
    }
}
