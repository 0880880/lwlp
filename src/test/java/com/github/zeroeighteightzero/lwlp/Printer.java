package com.github.zeroeighteightzero.lwlp;

import java.lang.reflect.Field;

public class Printer {

    public static void print(Node n, int depth) {
        StringBuilder db = new StringBuilder();
        for (int i = 0; i < depth; i++) db.append("    ");
        System.out.println(db + n.name + "@" + n.getClass().getSimpleName() + "   ==> {");
        Field[] fields = n.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Class<?> t = f.getType();
            if (Node.class.isAssignableFrom(t)) {
                try {
                    Printer.print((Node) f.get(n), depth + 1);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (t.isArray() && Node.class.isAssignableFrom(t.getComponentType())) {
                Node[] array;
                try {
                    array = (Node[]) f.get(n);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                for (Node item : array) {
                    Printer.print(item, depth + 1);
                }
            } else {
                try {
                    System.out.println(db + "    " + f.getName() + "@" + f.getType().getSimpleName() + "  : " + f.get(n));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println(db + "}");
    }

}
