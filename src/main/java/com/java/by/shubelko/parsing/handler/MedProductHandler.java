package com.java.by.shubelko.parsing.handler;

import com.java.by.shubelko.parsing.builder.enumsource.MedProductXmlAttribute;
import com.java.by.shubelko.parsing.builder.enumsource.MedProductXmlTag;
import com.java.by.shubelko.parsing.entity.Baa;
import com.java.by.shubelko.parsing.entity.MedProduct;
import com.java.by.shubelko.parsing.entity.Medicine;
import com.java.by.shubelko.parsing.entity.enumsource.Country;
import com.java.by.shubelko.parsing.entity.enumsource.GroupATC;
import com.java.by.shubelko.parsing.entity.enumsource.Pack;
import org.xml.sax.Attributes;

import java.time.YearMonth;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.helpers.DefaultHandler;

public class MedProductHandler extends DefaultHandler {

    public static final Logger logger = LogManager.getLogger();
    private static final String SPACE_SEPARATOR = " ";
    private static Set<MedProduct> medProducts;
    private final EnumSet<MedProductXmlTag> possibleXmlTag;
    private MedProduct currentMedProduct;
    private MedProductXmlTag currentXmlTag;

    public MedProductHandler() {
		medProducts = new HashSet<MedProduct>();
        possibleXmlTag = EnumSet.range(MedProductXmlTag.NAME, MedProductXmlTag.DOSAGE);

    }

    public Set<MedProduct> getMedCatalog() {
        return medProducts;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        String medicineTag = MedProductXmlTag.MEDICINE.toString();
        String baaTag = MedProductXmlTag.BAA.toString();
        if (medicineTag.equals(qName) || baaTag.equals(qName)) {
            currentMedProduct = medicineTag.equals(qName) ? new Medicine() : new Baa();
            defineAttributes(attributes);
        } else {
            MedProductXmlTag temp = MedProductXmlTag.valueOfXmlTag(qName);
            if (possibleXmlTag.contains(temp)) {
                currentXmlTag = temp;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        String medicinTag = MedProductXmlTag.MEDICINE.toString();
        String baaTag = MedProductXmlTag.BAA.toString();

        if (medicinTag.equals(qName) || baaTag.equals(qName)) {
            medProducts.add(currentMedProduct);
            logger.info("add to set " + currentMedProduct);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

        String data = new String(ch, start, length).trim();

        if (currentXmlTag != null) {

            switch (currentXmlTag) {
                case NAME -> currentMedProduct.setName(data);
                case PHARMA -> currentMedProduct.setPharma(data);
                case GROUP -> currentMedProduct.setGroup(GroupATC.valueOf(data));
                case ANALOGS -> currentMedProduct.setAnalogs(Arrays.asList(data.split(SPACE_SEPARATOR)));
                case COUNTRY -> currentMedProduct.getVersion().setCountry(Country.valueOfXmlContent(data));
                case CERTIFICATE -> currentMedProduct.getVersion().setCertificate(data);
                case DATA_FROM -> currentMedProduct.getVersion().setDataFrom(YearMonth.parse(data));
                case DATA_TO -> currentMedProduct.getVersion().setDataTo(YearMonth.parse(data));
                case PACK -> currentMedProduct.getVersion().setPack(Pack.valueOfXmlContent(data));
                case DOSAGE -> currentMedProduct.getVersion().setDosage(data);
                case CODE_CAS -> {
                    Medicine temp = (Medicine) currentMedProduct;
                    temp.setCodeCAS(data);
                    currentMedProduct = temp;
                }
                case ACTIVE_SUBSTANCE -> {
                    Medicine temp = (Medicine) currentMedProduct;
                    temp.setActiveSubstance(data);
                    currentMedProduct = temp;
                }
                case NEED_RECIPE -> {
                    Medicine temp = (Medicine) currentMedProduct;
                    temp.setNeedRecipe(Boolean.parseBoolean(data));
                    currentMedProduct = temp;
                }
                case COMPOSITION -> {
                    Baa temp = (Baa) currentMedProduct;
                    temp.setComposition(Arrays.asList(data.split(SPACE_SEPARATOR)));
                    currentMedProduct = temp;
                }
                default -> throw new EnumConstantNotPresentException(currentXmlTag.getDeclaringClass(),
                        currentXmlTag.name());
            }
        }
        currentXmlTag = null;
    }

    private void defineAttributes(Attributes attributes) {

        String medProductId = attributes.getValue(MedProductXmlAttribute.ID.toString());
        currentMedProduct.setMedProductId(medProductId);

        String outOfProductions = attributes.getValue(MedProductXmlAttribute.OUT_OF_PRODUCTION.toString());
        if (outOfProductions != null) {
            currentMedProduct.setOutOfProduction(Boolean.parseBoolean(outOfProductions));
        } else {
            currentMedProduct.setOutOfProduction(MedProduct.DEFAULT_OUT_OF_PRODUCTION);
        }
    }

}
