package com.github.zeroeighteightzero.lwlp;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class Printer {

    public static void print(Node n, int depth) {
        StringBuilder db = new StringBuilder();
        for (int i = 0; i < depth; i++) db.append("    ");
        System.out.println(db + n.name + "@" + n.getClass().getSimpleName() + "   ==> {");
        Field[] fields = ClassReflection.getFields(n.getClass());
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Class<?> t = f.getType();
            if (ClassReflection.isAssignableFrom(Node.class, t)) {
                try {
                    Printer.print((Node) f.get(n), depth + 1);
                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            } else if (t.isArray() && ClassReflection.isAssignableFrom(Node.class, t.getComponentType())) {
                try {
                    Node[] array = (Node[]) f.get(n);
                    for (Node item : array) {
                        Printer.print(item, depth + 1);
                    }
                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    System.out.println(db + "    " + f.getName() + "@" + f.getType().getSimpleName() + "  : " + f.get(n));
                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println(db + "}");
    }

}
