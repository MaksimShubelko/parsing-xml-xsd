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

public class MedProductValidator {

    private static final  Logger logger = LogManager.getLogger();
    private static final MedProductValidator instance = new MedProductValidator();

    public MedProductValidator() {
    }

    public static MedProductValidator getInstance() {
        return instance;
    }

    public boolean validateXml(String xmlPath, String schemaPath) throws MedProductException {
        boolean isXmlFileValid = false;
        logger.log(Level.INFO, "Try to validate...");
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        File schemaLocation = new File(schemaPath);
        try {
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xmlPath);
            MedProductErrorHandler errorHandler = new MedProductErrorHandler();
            validator.setErrorHandler(errorHandler);
            validator.validate(source);
            logger.log(Level.ERROR, "Xml file " + xmlPath + " is not valid.");
            isXmlFileValid = true;
        } catch (IOException ioException) {
            logger.log(Level.ERROR, "IO exception during work with files " +
                    xmlPath + "; " + schemaPath, ioException.getMessage());
            throw new MedProductException("IO exception during work with files");
        } catch (SAXException saxException) {
            logger.log(Level.ERROR, "Xml file " + xmlPath + " is not valid." + saxException.getMessage());
        }

        logger.log(Level.INFO, "Validation has been finished successfully!");
        return isXmlFileValid;
    }
}