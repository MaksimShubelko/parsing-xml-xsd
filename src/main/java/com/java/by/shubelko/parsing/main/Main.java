package com.java.by.shubelko.parsing.main;

import com.java.by.shubelko.parsing.builder.AbstractMedProductBuilder;
import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.factory.MedProductBuilderFactory;

public class Main {

    public static void main(String[] args) throws MedProductException {
        AbstractMedProductBuilder builder = MedProductBuilderFactory.newBuilder(MedProductBuilderFactory.TypeParser.SAX);
        builder.buildSetMedProducts("data/medProducts.xml");
        builder.getMedProducts().forEach(System.out::println);
        builder = MedProductBuilderFactory.newBuilder(MedProductBuilderFactory.TypeParser.StAX);
        builder.buildSetMedProducts("data/medProducts.xml");
        builder.getMedProducts().forEach(System.out::println);
        builder = MedProductBuilderFactory.newBuilder(MedProductBuilderFactory.TypeParser.SAX);
        builder.buildSetMedProducts("data/medProducts.xml");
        builder.getMedProducts().forEach(System.out::println);
    }
}
