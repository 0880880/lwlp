package com.github.zeroeighteightzero.lwlp;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Token {

    public static final Token NULL_TOKEN = new Token("__NULL__", "", 0, 0);

    private static int counter = 0;
    public int ID = counter++;
    public final String type;
    public final String value;
    public final int sourceStart;
    public final int sourceEnd;

}