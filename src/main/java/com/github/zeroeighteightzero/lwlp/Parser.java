package com.github.zeroeighteightzero.lwlp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    public Definition[] definitions;

    private int localOffset;
    private TokenReader reader;

    private boolean readMode;

    private ArrayList<ParserListener> listeners = new ArrayList<>();
    public boolean useDefaultListener = true;
    private final ParserListener defaultListener = (token, message) -> {
        throw new RuntimeException(message);
    };

    public Parser(Definition... definitions) {
        this.definitions = definitions;
    }

    public void addListener(ParserListener listener) {
        listeners.add(listener);
        useDefaultListener = false;
    }

    public void clearListeners() {
        listeners.clear();
    }

    public Node parse(TokenReader reader) {
        this.localOffset = 0;
        this.reader = reader;
        readMode = true;
        LinkedList<Definition> defs = new LinkedList<>();
        while (reader.remaining() - this.localOffset != 0) {
            boolean found = false;
            for (Definition definition : definitions) {
                if (definition.visible) {
                    int res = definition.match.check(this, 0);
                    if (res > 0) {
                        defs.add(definition);
                        localOffset += res;
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                Token t = read(0);
                if (useDefaultListener) defaultListener.error(t, "No match found");
                for (ParserListener listener : listeners) {
                    listener.error(t, "No match found");
                }
            }
        }
        readMode = false;
        this.localOffset = 0;
        ListNode program = new ListNode("program");
        program.nodes = new Node[defs.size()];
        int i = 0;
        while (!defs.isEmpty()) {
            program.nodes[i] = defs.removeFirst().match.create(this);
            i++;
        }
        return program;
    }

    public void parse(List<Token> tokens) {
        parse(new TokenReader(tokens.toArray(new Token[0])));
    }

    public Token read(int offset) {
        if (offset >= reader.remaining()) return null;
        return reader.peek(this.localOffset + offset);
    }

    public Definition findDefinition(String name) {
        for (Definition def : definitions) {
            if (name.equals(def.name))
                return def;
        }
        return null;
    }

    public Token eat() {
        if (readMode) throw new RuntimeException("Must not be in read mode");
        if (0 == reader.remaining()) return null;
        return reader.get();
    }
}
