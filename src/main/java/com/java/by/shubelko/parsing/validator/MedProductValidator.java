package com.java.by.shubelko.parsing.validator;


import com.java.by.shubelko.parsing.handler.MedProductErrorHandler;
import com.java.by.shubelko.parsing.exception.MedProductException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MedProductValidator {

    private static final  Logger logger = LogManager.getLogger();
    private static final MedProductValidator instance = new MedProductValidator();

    public MedProductValidator() {
    }

    public static MedProductValidator getInstance() {
        return instance;
    }

    public boolean validateXml(String xmlPath, String xsdPath) throws MedProductException, URISyntaxException {
        boolean isValid = true;
        logger.log(Level.INFO, "Try to validate...");
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            classLoader.getResourceAsStream(xmlPath);
            URL resourceXml = classLoader.getResource(xmlPath);
            classLoader.getResourceAsStream(xsdPath);
            URL resourceXsd = classLoader.getResource(xsdPath);
            if (resourceXsd == null || resourceXml == null) {
                throw new MedProductException("Paths are null or incorrect!" + xmlPath + " " + xsdPath);
            }
            File schemaLocation = new File(resourceXsd.toURI());
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(resourceXml.getPath());
            MedProductErrorHandler errorHandler = new MedProductErrorHandler();
            validator.setErrorHandler(errorHandler);
            validator.validate(source);
            if (errorHandler.isErrorHappened()) {
                isValid = false;
            }
        } catch (IOException | SAXException ioException) {
            logger.log(Level.ERROR, "IO exception during work with files " +
                    xmlPath + "; " + xsdPath, ioException);
            throw new MedProductException("IO exception during work with files");
        }
        logger.log(Level.INFO, "Validation has been finished successfully!");
        return isValid;
    }
}