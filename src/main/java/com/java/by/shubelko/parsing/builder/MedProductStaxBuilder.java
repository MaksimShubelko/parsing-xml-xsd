package com.java.by.shubelko.parsing.builder;

import com.java.by.shubelko.parsing.builder.type.MedProductXmlAttribute;
import com.java.by.shubelko.parsing.builder.type.MedProductXmlTag;
import com.java.by.shubelko.parsing.entity.AbstractMedProduct;
import com.java.by.shubelko.parsing.entity.Baa;
import com.java.by.shubelko.parsing.entity.Medicine;
import com.java.by.shubelko.parsing.entity.type.Country;
import com.java.by.shubelko.parsing.entity.type.GroupATC;
import com.java.by.shubelko.parsing.entity.type.Pack;
import com.java.by.shubelko.parsing.exception.MedProductException;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.time.YearMonth;
import java.util.Arrays;

public class MedProductStaxBuilder extends AbstractMedProductBuilder {
    private static final Logger logger = LogManager.getLogger();
    private XMLInputFactory inputFactory;

    public MedProductStaxBuilder() {
        inputFactory = XMLInputFactory.newInstance();
    }

    @Override
    public void buildSetMedProducts(String xmlPath) throws MedProductException {
        XMLStreamReader reader;
        String name;
        ClassLoader classLoader = getClass().getClassLoader();
        classLoader.getResourceAsStream(xmlPath);
        URL resourceXml = classLoader.getResource(xmlPath);
        if (resourceXml == null) {
            throw new MedProductException("Path is null or incorrect! " + xmlPath);
        }
        try (FileInputStream inputStream = new FileInputStream(new File(resourceXml.toURI()))) {
            reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    if (name.equals(MedProductXmlTag.MEDICINE.toString()) || name.equals(MedProductXmlTag.BAA.toString())) {
                        AbstractMedProduct abstractMedProduct = buildMedProduct(reader);
                        logger.log(Level.INFO, "add to catalog" + abstractMedProduct);
                        medProducts.add(abstractMedProduct);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.ERROR, "FileNotFoundException during work with file" + xmlPath, e);
            throw new MedProductException("FileNotFoundException during work with file " + xmlPath, e);
        } catch (IOException e) {
            logger.log(Level.ERROR, "IOException during work with file " + xmlPath, e);
            throw new MedProductException("IOException during work with file " + xmlPath, e);
        } catch (XMLStreamException | URISyntaxException e) {
            logger.log(Level.ERROR, "XMLStreamException while building set of medical products", e);
            throw new MedProductException("XMLStreamException while building set of medical products", e);
        }
    }

    private AbstractMedProduct buildMedProduct(XMLStreamReader reader) throws XMLStreamException {
        AbstractMedProduct currentAbstractMedProduct = reader.getLocalName().equals(MedProductXmlTag.MEDICINE.toString())
                ? new Medicine()
                : new Baa();

        currentAbstractMedProduct.setMedProductId(reader.getAttributeValue(null, MedProductXmlAttribute.ID.toString()));
        String content = reader.getAttributeValue(null, MedProductXmlAttribute.OUT_OF_PRODUCTION.toString());
        if (content != null) {
            currentAbstractMedProduct.setOutOfProduction(Boolean.parseBoolean(content));
        }

        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (MedProductXmlTag.valueOfXmlTag(name)) {
                        case NAME -> currentAbstractMedProduct.setName(getXMLText(reader));
                        case PHARMA -> currentAbstractMedProduct.setPharma(getXMLText(reader));
                        case GROUP -> currentAbstractMedProduct.setGroup(GroupATC.valueOfXmlTag(getXMLText(reader)));
                        case ANALOGS -> currentAbstractMedProduct.setAnalogs(Arrays.asList(getXMLText(reader).split(SPACE_SEPARATOR)));
                        case VERSION -> currentAbstractMedProduct.setVersion(getXMLMedProductVersion(reader));
                        case CODE_CAS -> {
                            Medicine temp = (Medicine) currentAbstractMedProduct;
                            temp.setCodeCAS(getXMLText(reader));
                            currentAbstractMedProduct = temp;
                        }
                        case ACTIVE_SUBSTANCE -> {
                            Medicine temp = (Medicine) currentAbstractMedProduct;
                            temp.setActiveSubstance(getXMLText(reader));
                            currentAbstractMedProduct = temp;
                        }
                        case NEED_RECIPE -> {
                            Medicine temp = (Medicine) currentAbstractMedProduct;
                            temp.setNeedRecipe(Boolean.parseBoolean(getXMLText(reader)));
                            currentAbstractMedProduct = temp;
                        }
                        case COMPOSITION -> {
                            Baa temp = (Baa) currentAbstractMedProduct;
                            temp.setComposition(Arrays.asList(getXMLText(reader).split(SPACE_SEPARATOR)));
                            currentAbstractMedProduct = temp;
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if (name.equals(MedProductXmlTag.MEDICINE.toString()) || name.equals(MedProductXmlTag.BAA.toString())) {
                        return currentAbstractMedProduct;
                    }
            }
        }
        throw new XMLStreamException("Unknown element in tag <medicine> or <baa>");
    }

    private AbstractMedProduct.Version getXMLMedProductVersion(XMLStreamReader reader) throws XMLStreamException {
        AbstractMedProduct.Version version = new Medicine().getVersion();
        String name;

        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (MedProductXmlTag.valueOfXmlTag(name)) {
                        case COUNTRY -> version.setCountry(Country.valueOfXmlTag(getXMLText(reader)));
                        case CERTIFICATE -> version.setCertificate(getXMLText(reader));
                        case DATA_FROM -> version.setDataFrom(YearMonth.parse(getXMLText(reader)));
                        case DATA_TO -> version.setDataTo(YearMonth.parse(getXMLText(reader)));
                        case PACK -> version.setPack(Pack.valueOfXmlTag(getXMLText(reader)));
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
        if (reader.hasNext() ) {
            reader.next();
            text = reader.getText();
        }
        return text;
    }
}
