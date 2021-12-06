package com.java.by.shubelko.parsing.builder.type;

public enum MedProductXmlTag {

	MED_PRODUCTS,
	MEDICINE,
	BAA,
	NAME,
	PHARMA,
	GROUP,
	ANALOGS,
	CODE_CAS,
	ACTIVE_SUBSTANCE,
	NEED_RECIPE,
	COMPOSITION,
	COUNTRY,
	CERTIFICATE,
	DATA_FROM,
	DATA_TO,
	PACK,
	DOSAGE,
	VERSION;
	
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
