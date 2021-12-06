package com.java.by.shubelko.parsing.builder.type;

public enum MedProductXmlAttribute {
	ID,
    OUT_OF_PRODUCTION;

    private static final char UNDERSCORE = '_';
    private static final char HYPHEN = '-';

    @Override
    public String toString() {
        return this.name().toLowerCase().replace(UNDERSCORE, HYPHEN);
    }

    public static MedProductXmlTag valueOfXmlTag(String tag) {
        return MedProductXmlTag.valueOf(tag.toUpperCase().replace(HYPHEN, UNDERSCORE));
    }
}
