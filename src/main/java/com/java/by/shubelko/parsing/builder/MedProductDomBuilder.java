package com.java.by.shubelko.parsing.builder;

import com.java.by.shubelko.parsing.entity.Baa;
import com.java.by.shubelko.parsing.entity.AbstractMedProduct;
import com.java.by.shubelko.parsing.entity.Medicine;
import com.java.by.shubelko.parsing.entity.type.Country;
import com.java.by.shubelko.parsing.entity.type.GroupATC;
import com.java.by.shubelko.parsing.entity.type.Pack;
import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.builder.type.MedProductXmlAttribute;
import com.java.by.shubelko.parsing.builder.type.MedProductXmlTag;
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
import java.net.URL;
import java.time.YearMonth;
import java.util.Arrays;

public class MedProductDomBuilder extends AbstractMedProductBuilder {
    private static final Logger logger = LogManager.getLogger();
    private DocumentBuilder docBuilder;

    public MedProductDomBuilder() throws MedProductException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.log(Level.ERROR,"ParserConfigurationException while configuring the parser" + e);
            throw new MedProductException("ParserConfigurationException while configuring the parser" + e);
        }
    }

    @Override
    public void buildSetMedProducts(String xmlPath) throws MedProductException {
        Document doc;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            classLoader.getResourceAsStream(xmlPath);
            URL resourceXml = classLoader.getResource(xmlPath);
            if (resourceXml == null) {
                throw new MedProductException("Path is null or incorrect! " + xmlPath);
            }
            doc = docBuilder.parse(resourceXml.getPath());
            Element root = doc.getDocumentElement();
            NodeList medProductList;
            medProductList = root.getElementsByTagName(MedProductXmlTag.MEDICINE.toString());
            for (int i = 0; i < medProductList.getLength(); i++) {
                Element medProductElement = (Element) medProductList.item(i);
                AbstractMedProduct newAbstractMedProduct = buildMedProduct(medProductElement);
                logger.log(Level.INFO, "add to set " + newAbstractMedProduct);
                medProducts.add(newAbstractMedProduct);
            }
            medProductList = root.getElementsByTagName(MedProductXmlTag.BAA.toString());
            for (int i = 0; i < medProductList.getLength(); i++) {
                Element medicineElement = (Element) medProductList.item(i);
                AbstractMedProduct newMedicine = buildMedProduct(medicineElement);
                logger.log(Level.INFO, "add to set " + newMedicine);
                medProducts.add(newMedicine);
            }

        } catch (IOException e) {
            logger.log(Level.ERROR,"IOException during work with files " + xmlPath, e);
            throw new MedProductException("IOException during work with files " + xmlPath, e);
        } catch (SAXException e) {
            logger.log(Level.ERROR,"SAXException while building set of medical products", e);
            throw new MedProductException("SAXException while building set of medical products", e);
        }

    }

    private AbstractMedProduct buildMedProduct(Element MedProductElement) {
        AbstractMedProduct currentAbstractMedProduct = MedProductElement.getTagName().equals(MedProductXmlTag.MEDICINE.toString())
                ? new Medicine()
                : new Baa();
        String content;

        content = MedProductElement.getAttribute(MedProductXmlAttribute.ID.toString());
        currentAbstractMedProduct.setMedProductId(content);
        content = MedProductElement.getAttribute(MedProductXmlAttribute.OUT_OF_PRODUCTION.toString());
        if (!content.isEmpty()) {
            currentAbstractMedProduct.setOutOfProduction(Boolean.parseBoolean(content));
        }

        content = getElementTextContent(MedProductElement, MedProductXmlTag.NAME.toString());
        currentAbstractMedProduct.setName(content);
        content = getElementTextContent(MedProductElement, MedProductXmlTag.PHARMA.toString());
        currentAbstractMedProduct.setPharma(content);
        content = getElementTextContent(MedProductElement, MedProductXmlTag.GROUP.toString());
        currentAbstractMedProduct.setGroup(GroupATC.valueOfXmlTag(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.ANALOGS.toString());
        currentAbstractMedProduct.setAnalogs(Arrays.asList(content.split(SPACE_SEPARATOR)));
        AbstractMedProduct.Version currentVersion = currentAbstractMedProduct.getVersion();
        content = getElementTextContent(MedProductElement, MedProductXmlTag.COUNTRY.toString());
        currentVersion.setCountry(Country.valueOfXmlTag(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.CERTIFICATE.toString());
        currentVersion.setCertificate(content);
        content = getElementTextContent(MedProductElement, MedProductXmlTag.DATA_FROM.toString());
        currentVersion.setDataFrom(YearMonth.parse(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.DATA_TO.toString());
        currentVersion.setDataTo(YearMonth.parse(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.PACK.toString());
        currentVersion.setPack(Pack.valueOfXmlTag(content));
        content = getElementTextContent(MedProductElement, MedProductXmlTag.DOSAGE.toString());
        currentVersion.setDosage(content);

        if (currentAbstractMedProduct instanceof Medicine) {
            Medicine temp = (Medicine) currentAbstractMedProduct;
            content = getElementTextContent(MedProductElement, MedProductXmlTag.CODE_CAS.toString());
            temp.setCodeCAS(content);
            content = getElementTextContent(MedProductElement, MedProductXmlTag.ACTIVE_SUBSTANCE.toString());
            temp.setActiveSubstance(content);
            content = getElementTextContent(MedProductElement, MedProductXmlTag.NEED_RECIPE.toString());
            temp.setNeedRecipe(Boolean.parseBoolean(content));

        } else {
            Baa temp = (Baa) currentAbstractMedProduct;
            content = getElementTextContent(MedProductElement, MedProductXmlTag.COMPOSITION.toString());
            temp.setComposition(Arrays.asList(content.split(SPACE_SEPARATOR)));

        }

        return currentAbstractMedProduct;
    }

    private static String getElementTextContent(Element element, String elementName) {
        NodeList nList = element.getElementsByTagName(elementName);
        Node node = nList.item(0);
        String text = node.getTextContent();
        return text;
    }

}