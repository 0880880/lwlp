package com.github.zeroeighteightzero.lwlp;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
class TokenPattern {
    public final String regex;
    public final String name;
    public String contentGroup;
}
