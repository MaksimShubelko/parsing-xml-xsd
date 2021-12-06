package com.java.by.shubelko.parsing.entity.type;

public enum GroupATC {

    METABOLIC,
    INTERMEDIATES,
    HISTAMINOLYTICS,
    ACTINOMYCINS,
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