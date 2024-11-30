package com.github.zeroeighteightzero.lwlp;

public class OrderedMinMatch extends CaseMatch {

    public CaseMatch[] caseMatches;
    public int min;

    public OrderedMinMatch(String name, int min, CaseMatch... caseMatches) {
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
            if (res == 0 && count >= min) return moves;
            if (res == 0) return 0;
            moves += res;
            count++;
        }
        if (count < min) return 0;
        return moves;
    }

    @Override
    public Node create(Parser parser) {
        int moves = 0;
        int count = 0;
        for (CaseMatch caseMatch : caseMatches) {
            int res = caseMatch.check(parser, moves);
            if (res == 0) break;
            moves += res;
            count++;
        }
        ListNode n = new ListNode(name);
        n.nodes = new Node[count];
        for (int i = 0; i < count; i++) {
            CaseMatch caseMatch = caseMatches[i];
            n.nodes[i] = caseMatch.create(parser);
        }
        return n;
    }
}
