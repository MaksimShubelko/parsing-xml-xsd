package com.java.by.shubelko.parsing.builder;

import com.java.by.shubelko.parsing.entity.AbstractMedProduct;
import com.java.by.shubelko.parsing.exception.MedProductException;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMedProductBuilder {
    protected final String SPACE_SEPARATOR = "\\s";
    protected Set<AbstractMedProduct> medProducts;

    public AbstractMedProductBuilder() {
        medProducts = new HashSet<AbstractMedProduct>();
    }

    public AbstractMedProductBuilder(Set<AbstractMedProduct> abstractMedProducts) {
        this.medProducts = abstractMedProducts;
    }

    public Set<AbstractMedProduct> getMedProducts() {
        return medProducts;
    }

    abstract public void buildSetMedProducts(String fileName) throws MedProductException;
}
