package com.java.by.shubelko.parsing.handler;

import com.java.by.shubelko.parsing.builder.type.MedProductXmlAttribute;
import com.java.by.shubelko.parsing.builder.type.MedProductXmlTag;
import com.java.by.shubelko.parsing.entity.Baa;
import com.java.by.shubelko.parsing.entity.AbstractMedProduct;
import com.java.by.shubelko.parsing.entity.Medicine;
import com.java.by.shubelko.parsing.entity.type.Country;
import com.java.by.shubelko.parsing.entity.type.GroupATC;
import com.java.by.shubelko.parsing.entity.type.Pack;
import org.xml.sax.Attributes;

import java.time.YearMonth;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.helpers.DefaultHandler;

public class MedProductHandler extends DefaultHandler {
    public static final Logger logger = LogManager.getLogger();
    private static final String SPACE_SEPARATOR = " ";
    private static Set<AbstractMedProduct> abstractMedProducts;
    private final EnumSet<MedProductXmlTag> possibleXmlTag;
    private AbstractMedProduct currentAbstractMedProduct;
    private MedProductXmlTag currentXmlTag;

    public MedProductHandler() {
		abstractMedProducts = new HashSet<AbstractMedProduct>();
        possibleXmlTag = EnumSet.range(MedProductXmlTag.NAME, MedProductXmlTag.DOSAGE);

    }

    public Set<AbstractMedProduct> getMedCatalog() {
        return abstractMedProducts;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        String medicineTag = MedProductXmlTag.MEDICINE.toString();
        String baaTag = MedProductXmlTag.BAA.toString();
        if (medicineTag.equals(qName) || baaTag.equals(qName)) {
            currentAbstractMedProduct = medicineTag.equals(qName) ? new Medicine() : new Baa();
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
            abstractMedProducts.add(currentAbstractMedProduct);
            logger.info("add to set " + currentAbstractMedProduct);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

        String data = new String(ch, start, length).trim();

        if (currentXmlTag != null) {
            switch (currentXmlTag) {
                case NAME -> currentAbstractMedProduct.setName(data);
                case PHARMA -> currentAbstractMedProduct.setPharma(data);
                case GROUP -> currentAbstractMedProduct.setGroup(GroupATC.valueOfXmlTag(data));
                case ANALOGS -> currentAbstractMedProduct.setAnalogs(Arrays.asList(data.split(SPACE_SEPARATOR)));
                case COUNTRY -> currentAbstractMedProduct.getVersion().setCountry(Country.valueOfXmlTag(data));
                case CERTIFICATE -> currentAbstractMedProduct.getVersion().setCertificate(data);
                case DATA_FROM -> currentAbstractMedProduct.getVersion().setDataFrom(YearMonth.parse(data));
                case DATA_TO -> currentAbstractMedProduct.getVersion().setDataTo(YearMonth.parse(data));
                case PACK -> currentAbstractMedProduct.getVersion().setPack(Pack.valueOfXmlTag(data));
                case DOSAGE -> currentAbstractMedProduct.getVersion().setDosage(data);
                case CODE_CAS -> {
                    Medicine temp = (Medicine) currentAbstractMedProduct;
                    temp.setCodeCAS(data);
                    currentAbstractMedProduct = temp;
                }
                case ACTIVE_SUBSTANCE -> {
                    Medicine temp = (Medicine) currentAbstractMedProduct;
                    temp.setActiveSubstance(data);
                    currentAbstractMedProduct = temp;
                }
                case NEED_RECIPE -> {
                    Medicine temp = (Medicine) currentAbstractMedProduct;
                    temp.setNeedRecipe(Boolean.parseBoolean(data));
                    currentAbstractMedProduct = temp;
                }
                case COMPOSITION -> {
                    Baa temp = (Baa) currentAbstractMedProduct;
                    temp.setComposition(Arrays.asList(data.split(SPACE_SEPARATOR)));
                    currentAbstractMedProduct = temp;
                }
                default -> throw new EnumConstantNotPresentException(currentXmlTag.getDeclaringClass(),
                        currentXmlTag.name());
            }
        }
        currentXmlTag = null;
    }

    private void defineAttributes(Attributes attributes) {

        String medProductId = attributes.getValue(MedProductXmlAttribute.ID.toString());
        currentAbstractMedProduct.setMedProductId(medProductId);

        String outOfProductions = attributes.getValue(MedProductXmlAttribute.OUT_OF_PRODUCTION.toString());
        if (outOfProductions != null) {
            currentAbstractMedProduct.setOutOfProduction(Boolean.parseBoolean(outOfProductions));
        }
    }

}
