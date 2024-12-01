package com.github.zeroeighteightzero.lwlp;

public class Token {

    public static final Token NULL_TOKEN = new Token("__NULL__", "", 0, 0);

    private static int counter = 0;
    public int ID = counter++;
    public final String type;
    public final String value;
    public final int sourceStart;
    public final int sourceEnd;

    public Token(String type, String value, int sourceStart, int sourceEnd) {
        this.type = type;
        this.value = value;
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
    }

    public int getID() {
        return this.ID;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public int getSourceStart() {
        return this.sourceStart;
    }

    public int getSourceEnd() {
        return this.sourceEnd;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Token)) return false;
        final Token other = (Token) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getID() != other.getID()) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
        if (this.getSourceStart() != other.getSourceStart()) return false;
        if (this.getSourceEnd() != other.getSourceEnd()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Token;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getID();
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        result = result * PRIME + this.getSourceStart();
        result = result * PRIME + this.getSourceEnd();
        return result;
    }

    public String toString() {
        return "Token(ID=" + this.getID() + ", type=" + this.getType() + ", value=" + this.getValue() + ", sourceStart=" + this.getSourceStart() + ", sourceEnd=" + this.getSourceEnd() + ")";
    }
}