package com.github.zeroeighteightzero.lwlp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final TokenPattern[] patterns;

    private final Pattern regex;

    public Lexer(TokenPattern[] tokenPatterns) {

        patterns = tokenPatterns;

        StringBuilder regexBuilder = new StringBuilder();

        for (int i = 0; i < patterns.length; i++) {
            if (i != 0) regexBuilder.append("|");
            if (!patterns[i].name.matches("[A-Za-z0-9]+"))
                throw new RuntimeException("Invalid group name. (Doesn't match [A-Za-z0-9])");
            regexBuilder.append("(?<").append(patterns[i].name).append(">").append(patterns[i].regex).append(")");
        }

        regex = Pattern.compile(regexBuilder.toString(), Pattern.MULTILINE);

    }

    public Pattern getRegex() {
        return regex;
    }

    public List<Token> lex(String source) {

        source = source.replaceAll("//[^\\n]*|/\\*[\\s\\S]*?\\*/", "");
        Matcher matcher = regex.matcher(source);

        ArrayList<Token> tokens = new ArrayList<>();
        int lastMax = 0;

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {
                    String tokenValue = matcher.group(i);
                    for (TokenPattern tokenPattern : patterns) {
                        Pattern p = Pattern.compile(tokenPattern.regex);
                        Matcher m = p.matcher(tokenValue);
                        if (m.find() && matcher.start() >= lastMax) {
                            String c = tokenValue;
                            if (tokenPattern.contentGroup != null) c = m.group(c);
                            lastMax = matcher.end(i);
                            Token token = new Token(tokenPattern.name, c, matcher.start(i), matcher.end(i)); // FIXME: Start and end don't have correct values when customGroup is set.
                            if (!tokenPattern.ignore) tokens.add(token);
                            break;
                        }
                    }
                }
            }
        }

        return tokens;

    }

    public TokenReader reader(List<Token> tokens) {
        return new TokenReader(tokens.toArray(new Token[0]));
    }


    public TokenReader reader(String source) {
        return new TokenReader(lex(source).toArray(new Token[0]));
    }
}
