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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Arrays;

public class MedProductDomBuilder extends AbstractMedProductBuilder {
    private static final Logger logger = LogManager.getLogger();
    private static final String SPACE_SEPARATOR = " ";
    private DocumentBuilder docBuilder;

    public MedProductDomBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.log(Level.ERROR,"ParserConfigurationException while configuring the parser");
        }
    }

    @Override
    public void buildSetMedProducts(String xmlPath) throws MedProductException {
        Document doc;
        try {
            doc = docBuilder.parse(xmlPath);
            Element root = doc.getDocumentElement();
            NodeList medProductList;
            medProductList = root.getElementsByTagName(MedProductXmlTag.MEDICINE.toString());
            for (int i = 0; i < medProductList.getLength(); i++) {
                Element medProductElement = (Element) medProductList.item(i);
                MedProduct newMedProduct = buildMedProduct(medProductElement);
                logger.log(Level.INFO, "add to set " + newMedProduct);
                medProducts.add(newMedProduct);
            }

            medProductList = root.getElementsByTagName(MedProductXmlTag.BAA.toString());
            for (int i = 0; i < medProductList.getLength(); i++) {
                Element medicineElement = (Element) medProductList.item(i);
                MedProduct newMedicine = buildMedProduct(medicineElement);
                logger.log(Level.INFO, "add to set " + newMedicine);
                medProducts.add(newMedicine);
            }

        } catch (IOException e) {
            logger.log(Level.ERROR,"IOException during work with files " + xmlPath);
            throw new MedProductException("IOException during work with files " + xmlPath, e);
        } catch (SAXException e) {
            logger.log(Level.ERROR,"SAXException while building" + medProducts);
            throw new MedProductException("SAXException while building" + medProducts, e);
        }

    }

    private MedProduct buildMedProduct(Element MedProductElement) {
        MedProduct currentMedProduct = MedProductElement.getTagName().equals(MedProductXmlTag.MEDICINE.toString())
                ? new Medicine()
                : new Baa();
        String content;

        content = MedProductElement.getAttribute(MedProductXmlAttribute.ID.toString());
        currentMedProduct.setMedProductId(content);
        content = MedProductElement.getAttribute(MedProductXmlAttribute.OUT_OF_PRODUCTION.toString());
        if (!content.isEmpty()) {
            currentMedProduct.setOutOfProduction(Boolean.parseBoolean(content));
        } else {
            currentMedProduct.setOutOfProduction(MedProduct.DEFAULT_OUT_OF_PRODUCTION);
        }

        content = getElementTextContent(MedProductElement, MedProductXmlTag.NAME.toString());
        currentMedProduct.setName(content);
        content = getElementTextContent(MedProductElement, MedProductXmlTag.PHARMA.toString());
        currentMedProduct.setPharma(content);
        content = getElementTextContent(MedProductElement, MedProductXmlTag.GROUP.toString());
        currentMedProduct.setGroup(GroupATC.valueOf(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.ANALOGS.toString());
        currentMedProduct.setAnalogs(Arrays.asList(content.split(SPACE_SEPARATOR)));
        MedProduct.Version currentVersion = currentMedProduct.getVersion();
        content = getElementTextContent(MedProductElement, MedProductXmlTag.COUNTRY.toString());
        currentVersion.setCountry(Country.valueOfXmlContent(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.CERTIFICATE.toString());
        currentVersion.setCertificate(content);
        content = getElementTextContent(MedProductElement, MedProductXmlTag.DATA_FROM.toString());
        currentVersion.setDataFrom(YearMonth.parse(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.DATA_TO.toString());
        currentVersion.setDataTo(YearMonth.parse(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.PACK.toString());
        currentVersion.setPack(Pack.valueOfXmlContent(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.DOSAGE.toString());
        currentVersion.setDosage(content);

        if (currentMedProduct instanceof Medicine) {
            Medicine temp = (Medicine) currentMedProduct;
            content = getElementTextContent(MedProductElement, MedProductXmlTag.CODE_CAS.toString());
            temp.setCodeCAS(content);
            content = getElementTextContent(MedProductElement, MedProductXmlTag.ACTIVE_SUBSTANCE.toString());
            temp.setActiveSubstance(content);
            content = getElementTextContent(MedProductElement, MedProductXmlTag.NEED_RECIPE.toString());
            temp.setNeedRecipe(Boolean.parseBoolean(content));

        } else {
            Baa temp = (Baa) currentMedProduct;
            content = getElementTextContent(MedProductElement, MedProductXmlTag.COMPOSITION.toString());
            temp.setComposition(Arrays.asList(content.split(SPACE_SEPARATOR)));

        }

        return currentMedProduct;
    }

    private static String getElementTextContent(Element element, String elementName) {
        NodeList nList = element.getElementsByTagName(elementName);
        Node node = nList.item(0);
        String text = node.getTextContent();
        return text;
    }

}