package com.test.by.shubelko.builder;

import com.java.by.shubelko.parsing.builder.MedProductSaxBuilder;
import com.java.by.shubelko.parsing.entity.AbstractMedProduct;
import com.java.by.shubelko.parsing.entity.Baa;
import com.java.by.shubelko.parsing.entity.Medicine;
import com.java.by.shubelko.parsing.entity.type.Country;
import com.java.by.shubelko.parsing.entity.type.GroupATC;
import com.java.by.shubelko.parsing.entity.type.Pack;
import com.java.by.shubelko.parsing.exception.MedProductException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class MedProductSaxBuilderTest {

    private MedProductSaxBuilder saxBuilder;

    @BeforeTest
    public void initialize() throws MedProductException {
        saxBuilder = new MedProductSaxBuilder();
    }

    @Test
    public void testBuildMedCatalogPositive() throws MedProductException {

        Medicine med1 = new Medicine();
        med1.setMedProductId("med1");
        med1.setName("Atomax");
        med1.setPharma("Med Company");
        med1.setGroup(GroupATC.AMINOACIDS);
        med1.setAnalogs(List.of("Taurine"));
        med1.getVersion().setCountry(Country.ISRAEL);
        med1.getVersion().setCertificate("1255.DF54565.1587RE");
        med1.getVersion().setDataFrom(YearMonth.parse("2021-01"));
        med1.getVersion().setDataTo(YearMonth.parse("2022-01"));
        med1.getVersion().setPack(Pack.PILL);
        med1.getVersion().setDosage("50mg");
        med1.setCodeCAS("1235-67-8");
        med1.setActiveSubstance("atorvastatin");
        med1.setNeedRecipe(false);

        Baa med2 = new Baa();
        med2.setMedProductId("med2");
        med2.setName("Vardenafilum");
        med2.setPharma("Med Company");
        med2.setGroup(GroupATC.INTERMEDIATES);
        med2.setAnalogs(Arrays.asList("Gooder", "Weller"));
        med2.getVersion().setCountry(Country.RUSSIA);
        med2.getVersion().setCertificate("1889.589-31.SS157");
        med2.getVersion().setDataFrom(YearMonth.parse("2020-07"));
        med2.getVersion().setDataTo(YearMonth.parse("2023-01"));
        med2.getVersion().setPack(Pack.GEL);
        med2.getVersion().setDosage("100ml");
        med2.setComposition(Arrays.asList("Mentol", "Oil"));
        Set<AbstractMedProduct> expected = new HashSet<AbstractMedProduct>();
        expected.add(med1);
        expected.add(med2);

        String xmlPath = "data/medProductsValid.xml";
        saxBuilder.buildSetMedProducts(xmlPath);
        Set<AbstractMedProduct> actual = saxBuilder.getMedProducts();
        assertEquals(actual, expected);
    }

    @Test (expectedExceptions = MedProductException.class)
    public void testBuildMedCatalogFileNotFound() throws MedProductException {

        String xmlPath = "Java if great!";

        saxBuilder.buildSetMedProducts(xmlPath);
    }

    @AfterTest
    public void close() {
        saxBuilder = null;
    }
}
