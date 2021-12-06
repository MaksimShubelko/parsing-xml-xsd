package com.java.by.shubelko.parsing.entity;

import com.java.by.shubelko.parsing.entity.type.GroupATC;

import java.util.List;
import java.util.Objects;

public class Baa extends AbstractMedProduct {

    private List<String> composition;

    public Baa() {
        super();
    }

    public Baa(String medProductId, boolean outOfProduction, String name, String pharma, GroupATC group,
               List<String> analogs, Version version, List<String> composition) {
        super(medProductId, outOfProduction, name, pharma, group, analogs, version);
        this.composition = composition;
    }

    public List<String> getComposition() {
        return composition;
    }

    public void setComposition(List<String> composition) {
        this.composition = composition;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((composition == null) ? 0 : composition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Baa)) return false;
        if (!super.equals(o)) return false;
        Baa baa = (Baa) o;
        return Objects.equals(composition, baa.composition);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("BAA [")
                .append(super.toString())
                .append(" composition=")
                .append(composition)
                .append("]")
                .toString();
    }

}
