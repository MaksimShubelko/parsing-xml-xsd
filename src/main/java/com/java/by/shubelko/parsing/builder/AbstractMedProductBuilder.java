package com.java.by.shubelko.parsing.builder;

import com.java.by.shubelko.parsing.entity.MedProduct;
import com.java.by.shubelko.parsing.exception.MedProductException;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMedProductBuilder {
    protected Set<MedProduct> medProducts;

    public AbstractMedProductBuilder() {
        medProducts = new HashSet<MedProduct>();
    }

    public AbstractMedProductBuilder(Set<MedProduct> medProducts) {
        this.medProducts = medProducts;
    }

    public Set<MedProduct> getMedProducts() {
        return medProducts;
    }

    abstract public void buildSetMedProducts(String fileName) throws MedProductException;
}
