package com.java.by.shubelko.parsing.entity.type;

public enum Country {

    GERMANY,
    BELARUS,
    BELGIUM,
    RUSSIA,
    CZECH_REPUBLIC,
    INDIA,
    ISRAEL,
    POLAND,
    CHINA;

    private static final char UNDERSCORE = '_';
    private static final char HYPHEN = '-';

    @Override
    public String toString() {
        return this.name().toLowerCase().replace(UNDERSCORE, HYPHEN);
    }

    public static Country valueOfXmlTag(String tag) {
        return Country.valueOf(tag.toUpperCase().replace(HYPHEN, UNDERSCORE));
    }
}
