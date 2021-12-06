package com.java.by.shubelko.parsing.entity;

import com.java.by.shubelko.parsing.entity.type.GroupATC;

import java.util.List;
import java.util.Objects;

public class Medicine extends AbstractMedProduct {

    private String codeCAS;
    private String activeSubstance;
    private boolean needRecipe;

    public Medicine() {
        super();
    }

    public Medicine(String medProductId, boolean outOfProduction, String name, String pharma, GroupATC group,
                    List<String> analogs, Version version, String codeCAS, String activeSubstance, boolean needRecipe) {
        super(medProductId, outOfProduction, name, pharma, group, analogs, version);
        this.codeCAS = codeCAS;
        this.activeSubstance = activeSubstance;
        this.needRecipe = needRecipe;
    }

    public String getCodeCAS() {
        return codeCAS;
    }

    public void setCodeCAS(String codeCAS) {
        this.codeCAS = codeCAS;
    }

    public String getActiveSubstance() {
        return activeSubstance;
    }

    public void setActiveSubstance(String activeSubstance) {
        this.activeSubstance = activeSubstance;
    }

    public boolean isNeedRecipe() {
        return needRecipe;
    }

    public void setNeedRecipe(boolean needRecipe) {
        this.needRecipe = needRecipe;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((activeSubstance == null) ? 0 : activeSubstance.hashCode());
        result = prime * result + ((codeCAS == null) ? 0 : codeCAS.hashCode());
        result = prime * result + (needRecipe ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;
        if (!super.equals(o)) return false;
        Medicine medicine = (Medicine) o;
        return needRecipe == medicine.needRecipe
                && Objects.equals(codeCAS, medicine.codeCAS)
                && Objects.equals(activeSubstance, medicine.activeSubstance);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Medicine [")
                .append(super.toString())
                .append(" codeCAS=")
                .append(codeCAS)
                .append(", activeSubstance=")
                .append(activeSubstance)
                .append(", needRecipe=")
                .append(needRecipe)
                .append("]")
                .toString();
    }
}