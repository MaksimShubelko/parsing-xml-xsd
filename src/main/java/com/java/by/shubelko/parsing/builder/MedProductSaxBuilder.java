package com.java.by.shubelko.parsing.builder;

import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.handler.MedProductErrorHandler;
import com.java.by.shubelko.parsing.handler.MedProductHandler;
import com.java.by.shubelko.parsing.builder.AbstractMedProductBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class MedProductSaxBuilder extends AbstractMedProductBuilder {
	private static final Logger logger = LogManager.getLogger();
	private final MedProductHandler handler = new MedProductHandler();
	private XMLReader reader;

	public MedProductSaxBuilder() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			reader = saxParser.getXMLReader();
		} catch (ParserConfigurationException e) {
			logger.log(Level.ERROR, "ParserConfigurationException while configuring the parser");
		} catch (SAXException e) {
			logger.log(Level.ERROR, "SAXException while configuring the parser");
		}
		reader.setErrorHandler(new MedProductErrorHandler());
		reader.setContentHandler(handler);
	}

	@Override
	public void buildSetMedProducts(String xmlPath) throws MedProductException {
		try {
			reader.parse(xmlPath);
		} catch (IOException e) {
			logger.error("IOException during work with files " + xmlPath);
			throw new MedProductException("IOException during work with files " + xmlPath, e);
		} catch (SAXException e) {
			logger.error("SAXException while building Set<MedProduct>");
			throw new MedProductException("SAXException while building Set<MedProduct>", e);
		}
		medProducts = handler.getMedCatalog();
	}
}
