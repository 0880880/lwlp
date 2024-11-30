package com.github.zeroeighteightzero.lwlp;

public class MultiMatch extends CaseMatch {

    public CaseMatch caseMatch;
    public int min = 0;
    public int max = Integer.MAX_VALUE;

    private MultiMatch(String name) {
        super(name);
    }

    private MultiMatch(String name, CaseMatch match, int num) {
        super(name);
        this.caseMatch = match;
        this.min = num;
        this.max = num;
    }

    private MultiMatch(String name, CaseMatch match, int min, int max) {
        super(name);
        this.caseMatch = match;
        this.min = min;
        this.max = max;
    }

    @Override
    public int check(Parser parser, int offset) {
        int moves = 0;
        int res = caseMatch.check(parser, offset + moves);
        while (res > 0) {
            moves += res;
            res = caseMatch.check(parser, offset + moves);
        }
        return moves;
    }

    @Override
    public Node create(Parser parser) {
        ListNode n = new ListNode(name);
        int moves = 0;
        int res = caseMatch.check(parser, moves);
        int count = 0;
        while (res > 0) {
            count++;
            moves += res;
            res = caseMatch.check(parser, moves);
        }
        n.nodes = new Node[count];
        for (int i = 0; i < count; i++) {
            n.nodes[i] = caseMatch.create(parser);
        }
        return n;
    }

    public static MultiMatch num(String name, CaseMatch match, int num) {
        MultiMatch m = new MultiMatch(name);
        m.caseMatch = match;
        m.min = num;
        m.max = num;
        return m;
    }

    public static MultiMatch min(String name, CaseMatch match, int min) {
        MultiMatch m = new MultiMatch(name);
        m.caseMatch = match;
        m.min = min;
        m.max = Integer.MAX_VALUE;
        return m;
    }

    public static MultiMatch max(String name, CaseMatch match, int max) {
        MultiMatch m = new MultiMatch(name);
        m.caseMatch = match;
        m.min = 0;
        m.max = max;
        return m;
    }

    public static MultiMatch range(String name, CaseMatch match, int min, int max) {
        MultiMatch m = new MultiMatch(name);
        m.caseMatch = match;
        m.min = min;
        m.max = max;
        return m;
    }

}
