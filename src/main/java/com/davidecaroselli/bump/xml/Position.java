package com.davidecaroselli.bump.xml;

public final class Position {

    private final int begin;
    private final int end;

    public Position(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public String getText(String source) {
        return source.substring(begin, end);
    }

    public String replace(String source, String replacement) {
        return source.substring(0, begin) + replacement + source.substring(end);
    }

    @Override
    public String toString() {
        return "Position{" + begin + ", " + end + '}';
    }
}
