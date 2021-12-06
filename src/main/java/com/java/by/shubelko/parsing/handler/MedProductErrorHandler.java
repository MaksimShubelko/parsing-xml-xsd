package com.java.by.shubelko.parsing.handler;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MedProductErrorHandler implements ErrorHandler {
	
	static Logger logger = LogManager.getLogger();
	private boolean errorHappened = false;

	public void warning(SAXParseException exception) throws SAXException {
		logger.log(Level.WARN,exception.getLineNumber() + " : " + exception.getColumnNumber() +" - " + exception.getMessage());
	}

	public void error(SAXParseException exception) throws SAXException {
		logger.log(Level.ERROR,exception.getLineNumber() + " : " + exception.getColumnNumber() +" - " + exception.getMessage());
		errorHappened = true;
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		logger.fatal(exception.getLineNumber() + " : " + exception.getColumnNumber() +" - " + exception.getMessage());
	}

	public boolean isErrorHappened() {
		return errorHappened;
	}
	
}
