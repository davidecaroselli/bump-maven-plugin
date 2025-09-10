package com.davidecaroselli.bump.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class XMLTag {

    private static final String TagName = "(\\p{Alpha}|_|:)(\\p{Alpha}|\\p{Digit}|\\.|-|_|:|)*";
    private static final Pattern TagNameRegex = Pattern.compile(TagName);
    public static final Pattern REGEX = Pattern.compile("(<(" + TagName + ")[^>]*/?>)|(</(" + TagName + ")[^>]*>)");

    enum Type {
        OPENING,
        CLOSING,
        SELF_CLOSING
    }

    static XMLTag fromText(String text) {
        Matcher matcher = TagNameRegex.matcher(text);
        if (!matcher.find()) return null;
        String name = matcher.group().toLowerCase();

        if (text.startsWith("</"))
            return new XMLTag(name, Type.CLOSING);
        else if (text.endsWith("/>"))
            return new XMLTag(name, Type.SELF_CLOSING);
        else
            return new XMLTag(name, Type.OPENING);
    }

    private final String name;
    private final Type type;

    XMLTag(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }

}
