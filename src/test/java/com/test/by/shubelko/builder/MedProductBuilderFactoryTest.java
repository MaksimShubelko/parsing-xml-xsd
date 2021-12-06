package com.test.by.shubelko.builder;

import com.java.by.shubelko.parsing.builder.AbstractMedProductBuilder;
import com.java.by.shubelko.parsing.builder.MedProductDomBuilder;
import com.java.by.shubelko.parsing.builder.MedProductSaxBuilder;
import com.java.by.shubelko.parsing.builder.MedProductStaxBuilder;
import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.factory.MedProductBuilderFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MedProductBuilderFactoryTest {

    @BeforeTest
    public void initEnum() {

    }

    @DataProvider(name = "providerBuilder")
    public Object[][] creatData() {
        return new Object[][]{
                {MedProductBuilderFactory.TypeParser.SAX, MedProductSaxBuilder.class},
                {MedProductBuilderFactory.TypeParser.DOM, MedProductDomBuilder.class},
                {MedProductBuilderFactory.TypeParser.StAX, MedProductStaxBuilder.class},
        };
    }

    @Test(dataProvider = "providerBuilder")
    public void testCreateBuilderPositive(MedProductBuilderFactory.TypeParser typeParser, Class<? extends AbstractMedProductBuilder> expected) throws MedProductException {
        Class<? extends AbstractMedProductBuilder> actual = MedProductBuilderFactory.newBuilder(typeParser).getClass();
        assertEquals(actual, expected);
    }
}
