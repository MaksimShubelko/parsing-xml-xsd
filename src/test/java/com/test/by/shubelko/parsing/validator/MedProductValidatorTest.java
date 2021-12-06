package com.test.by.shubelko.parsing.validator;

import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.validator.MedProductValidator;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import static org.testng.Assert.*;

public class MedProductValidatorTest {

    @Test
    public void testMedProductValidatorValid() throws MedProductException, URISyntaxException {
        boolean isValid;
        MedProductValidator medProductValidator = new MedProductValidator();
        isValid = medProductValidator.validateXml("data/medProductsValid.xml", "data/medProductsSchema.xsd");
        assertTrue(isValid);
    }

    @Test
    public void testMedProductValidatorNotValid() throws MedProductException, URISyntaxException {
        boolean isValid;
        MedProductValidator medProductValidator = new MedProductValidator();
        isValid = medProductValidator.validateXml("data/medProductsNotValid.xml", "data/medProductsSchema.xsd");
        assertFalse(isValid);
    }

    @Test(expectedExceptions = MedProductException.class)
    public void testMedProductValidatorWrongPath() throws MedProductException, URISyntaxException {
        boolean isValid;
        MedProductValidator medProductValidator = new MedProductValidator();
        isValid = medProductValidator.validateXml("java is great!", "data/medProductsSchema.xsd");
    }
}