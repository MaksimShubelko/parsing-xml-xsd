package com.java.by.shubelko.parsing.entity.enumsource;

public enum GroupATC {

    METABOLIC,
    INTERMEDIATES,
    HISTAMINILYTICS,
    ACTINOMYCUNS,
    BISPHOSPHONATES,
    AMINOACIDS,
    ANTIBIOTICS,
    ANTIESTROGENS,
    ABSORBENTS,
    SULFONAMIDES,
    AMINOQUINOLINES,
    TAXA,
    BIGUANIDES,
    SOPORIFIC;

    private static final char UNDERSCORE = '_';
    private static final char HYPHEN = '-';

    @Override
    public String toString() {
        return this.name().toLowerCase().replace(UNDERSCORE, HYPHEN);
    }

    public static GroupATC valueOfXmlTag(String tag) {
        return GroupATC.valueOf(tag.toUpperCase().replace(HYPHEN, UNDERSCORE));
    }
}