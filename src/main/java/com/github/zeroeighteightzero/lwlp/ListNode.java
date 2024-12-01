package com.github.zeroeighteightzero.lwlp;

public class ListNode extends Node {

    public Node[] nodes;

    public ListNode(String name) {
        super(name);
    }

    public Node get(String name) {
        for (Node node : nodes) {
            if (node.name.equals(name))
                return node;
        }
        return null;
    }

}
