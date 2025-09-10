package com.davidecaroselli.bump.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public final class XMLFinder {

    public static Position find(String xmlDocument, String... path) {
        List<String> target = Arrays.asList(path);
        List<String> current = new ArrayList<>();

        int begin = -1;

        Matcher matcher = XMLTag.REGEX.matcher(xmlDocument);
        while (matcher.find()) {
            XMLTag tag = XMLTag.fromText(matcher.group());
            if (tag == null) continue;

            if (XMLTag.Type.SELF_CLOSING == tag.type()) {
                current.add(tag.name());
                if (current.equals(target))
                    return new Position(matcher.end(), matcher.end());
                current.remove(current.size() - 1);
            } else if (XMLTag.Type.OPENING == tag.type()) {
                current.add(tag.name());
                if (current.equals(target))
                    begin = matcher.end();
            } else if (XMLTag.Type.CLOSING == tag.type()) {
                if (current.equals(target))
                    return new Position(begin, matcher.start());
                if (!current.isEmpty()) current.remove(current.size() - 1);
            }
        }

        return null;
    }

}
