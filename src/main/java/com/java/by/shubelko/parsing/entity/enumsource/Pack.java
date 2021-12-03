package com.java.by.shubelko.parsing.entity.enumsource;

public enum Pack {

    PILL,
    CAPSULE,
    POWDER,
    OINTMENT,
    GEL,
    SOLUTION_FOR_INJECTION,
    MIXTURE,
    AEROSOL,
    DROP;

    private static final char UNDERSCORE = '_';
    private static final char HYPHEN = '-';

    @Override
    public String toString() {
        return this.name().toLowerCase().replace(UNDERSCORE, HYPHEN);
    }

    public static Pack valueOfXmlContent(String tag) {
        return Pack.valueOf(tag.toUpperCase().replace(HYPHEN, UNDERSCORE));
    }
}
