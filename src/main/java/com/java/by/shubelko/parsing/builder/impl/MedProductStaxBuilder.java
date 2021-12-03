package com.java.by.shubelko.parsing.builder.impl;

import com.java.by.shubelko.parsing.entity.Baa;
import com.java.by.shubelko.parsing.entity.MedProduct;
import com.java.by.shubelko.parsing.entity.Medicine;
import com.java.by.shubelko.parsing.entity.enumsource.Country;
import com.java.by.shubelko.parsing.entity.enumsource.GroupATC;
import com.java.by.shubelko.parsing.entity.enumsource.Pack;
import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.builder.AbstractMedProductBuilder;
import com.java.by.shubelko.parsing.builder.enumsource.MedProductXmlAttribute;
import com.java.by.shubelko.parsing.builder.enumsource.MedProductXmlTag;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Arrays;

public class MedProductStaxBuilder extends AbstractMedProductBuilder {
    private static final Logger logger = LogManager.getLogger();
    private static final String SPACE_SEPARATOR = " ";
    private XMLInputFactory inputFactory;

    public MedProductStaxBuilder() {
        inputFactory = XMLInputFactory.newInstance();
    }

    @Override
    public void buildSetMedProducts(String xmlPath) throws MedProductException {
        XMLStreamReader reader;
        String name;
        try (FileInputStream inputStream = new FileInputStream(new File(xmlPath))) {
            reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    if (name.equals(MedProductXmlTag.MEDICINE.toString()) || name.equals(MedProductXmlTag.BAA.toString())) {
                        MedProduct medProduct = buildMedProduct(reader);
                        logger.log(Level.INFO,"add to catalog" + medProduct);
                        medProducts.add(medProduct);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.ERROR, "FileNotFoundException during work with file" + xmlPath);
            throw new MedProductException("FileNotFoundException during work with file " + xmlPath, e);
        } catch (IOException e) {
            logger.log(Level.ERROR,"IOException during work with file " + xmlPath);
            throw new MedProductException("IOException during work with file " + xmlPath, e);
        } catch (XMLStreamException e) {
            logger.log(Level.ERROR,"XMLStreamException while building" + medProducts);
            throw new MedProductException("XMLStreamException while building", e);
        }
    }

    private MedProduct buildMedProduct(XMLStreamReader reader) throws XMLStreamException {
        MedProduct currentMedProduct = reader.getLocalName().equals(MedProductXmlTag.MEDICINE.toString())
                ? new Medicine()
                : new Baa();

        currentMedProduct.setMedProductId(reader.getAttributeValue(null, MedProductXmlAttribute.ID.toString()));
        String content = reader.getAttributeValue(null, MedProductXmlAttribute.OUT_OF_PRODUCTION.toString());
        if (content != null) {
            currentMedProduct.setOutOfProduction(Boolean.parseBoolean(content));
        } else {
            currentMedProduct.setOutOfProduction(MedProduct.DEFAULT_OUT_OF_PRODUCTION);
        }

        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (MedProductXmlTag.valueOfXmlTag(name)) {
                        case NAME -> currentMedProduct.setName(getXMLText(reader));
                        case PHARMA -> currentMedProduct.setPharma(getXMLText(reader));
                        case GROUP -> currentMedProduct.setGroup(GroupATC.valueOf(getXMLText(reader)));
                        case ANALOGS -> currentMedProduct.setAnalogs(Arrays.asList(getXMLText(reader).split(SPACE_SEPARATOR)));
                        case VERSION -> currentMedProduct.setVersion(getXMLMedProductVersion(reader));
                        case CODE_CAS -> {
                            Medicine temp = (Medicine) currentMedProduct;
                            temp.setCodeCAS(getXMLText(reader));
                            currentMedProduct = temp;
                        }
                        case ACTIVE_SUBSTANCE -> {
                            Medicine temp = (Medicine) currentMedProduct;
                            temp.setActiveSubstance(getXMLText(reader));
                            currentMedProduct = temp;
                        }
                        case NEED_RECIPE -> {
                            Medicine temp = (Medicine) currentMedProduct;
                            temp.setNeedRecipe(Boolean.parseBoolean(getXMLText(reader)));
                            currentMedProduct = temp;
                        }
                        case COMPOSITION -> {
                            Baa temp = (Baa) currentMedProduct;
                            temp.setComposition(Arrays.asList(getXMLText(reader).split(SPACE_SEPARATOR)));
                            currentMedProduct = temp;
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if (name.equals(MedProductXmlTag.MEDICINE.toString()) || name.equals(MedProductXmlTag.BAA.toString())) {
                        return currentMedProduct;
                    }
            }
        }
        throw new XMLStreamException("Unknown element in tag <medicine> or <baa>");
    }

    private MedProduct.Version getXMLMedProductVersion(XMLStreamReader reader) throws XMLStreamException {
        MedProduct.Version version = new Medicine().getVersion();
        String name;

        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (MedProductXmlTag.valueOfXmlTag(name)) {
                        case COUNTRY -> version.setCountry(Country.valueOf(getXMLText(reader)));
                        case CERTIFICATE -> version.setCertificate(getXMLText(reader));
                        case DATA_FROM -> version.setDataFrom(YearMonth.parse(getXMLText(reader)));
                        case DATA_TO -> version.setDataTo(YearMonth.parse(getXMLText(reader)));
                        case PACK -> version.setPack(Pack.valueOf(getXMLText(reader)));
                        case DOSAGE -> version.setDosage(getXMLText(reader));
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if (name.equals(MedProductXmlTag.VERSION.toString())) {
                        return version;
                    }
            }
        }
        throw new XMLStreamException("Unknown element in tag <version>");
    }

    private String getXMLText(XMLStreamReader reader) throws XMLStreamException {
        String text = null;
        if (reader.hasNext()) {
            reader.next();
            text = reader.getText();
        }
        return text;
    }
}
