package com.java.by.shubelko.parsing.builder;

import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.handler.MedProductErrorHandler;
import com.java.by.shubelko.parsing.handler.MedProductHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MedProductSaxBuilder extends AbstractMedProductBuilder {
	private static final Logger logger = LogManager.getLogger();
	private final MedProductHandler handler = new MedProductHandler();
	private XMLReader reader;

	public MedProductSaxBuilder() throws MedProductException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			reader = saxParser.getXMLReader();
		} catch (ParserConfigurationException e) {
			logger.log(Level.ERROR, "ParserConfigurationException while configuring the parser", e);
			throw new MedProductException("ParserConfigurationException while configuring the parser", e);
		} catch (SAXException e) {
			logger.log(Level.ERROR, "SAXException while configuring the parser", e);
			throw new MedProductException("SAXException while configuring the parser", e);
		}
		reader.setErrorHandler(new MedProductErrorHandler());
		reader.setContentHandler(handler);
	}

	@Override
	public void buildSetMedProducts(String xmlPath) throws MedProductException {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			classLoader.getResourceAsStream(xmlPath);
			URL resourceXml = classLoader.getResource(xmlPath);
			if (resourceXml == null) {
				throw new MedProductException("Path is null or incorrect! " + xmlPath);
			}
			reader.parse(resourceXml.getPath());
		} catch (IOException e) {
			logger.log(Level.ERROR,"IOException during work with files " + xmlPath, e);
			throw new MedProductException("IOException during work with files " + xmlPath, e);
		} catch (SAXException e) {
			logger.log(Level.ERROR,"SAXException while building Set<MedProduct>", e);
			throw new MedProductException("SAXException while building Set<MedProduct>", e);
		}
		medProducts = handler.getMedCatalog();
	}
}
