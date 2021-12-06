package com.java.by.shubelko.parsing.entity;

import com.java.by.shubelko.parsing.entity.type.Country;
import com.java.by.shubelko.parsing.entity.type.GroupATC;
import com.java.by.shubelko.parsing.entity.type.Pack;

import java.time.YearMonth;
import java.util.List;

public abstract class AbstractMedProduct {

    private String medProductId;
    private boolean outOfProduction;
    private String name;
    private String pharma;
    private GroupATC group;
    private List<String> analogs;
    private Version version = new Version();

    public AbstractMedProduct() {

    }

    public AbstractMedProduct(String medProductID, boolean outOfProduction, String name, String pharma, GroupATC group,
                              List<String> analogs, Version version) {
        this.medProductId = medProductID;
        this.outOfProduction = outOfProduction;
        this.name = name;
        this.pharma = pharma;
        this.group = group;
        this.analogs = analogs;
        this.version = version;
    }

    public String getMedProductId() {
        return medProductId;
    }

    public void setMedProductId(String medicineId) {
        this.medProductId = medicineId;
    }

    public boolean isOutOfProduction() {
        return outOfProduction;
    }

    public void setOutOfProduction(boolean outOfProduction) {
        this.outOfProduction = outOfProduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPharma() {
        return pharma;
    }

    public void setPharma(String pharma) {
        this.pharma = pharma;
    }

    public GroupATC getGroup() {
        return group;
    }

    public void setGroup(GroupATC group) {
        this.group = group;
    }

    public List<String> getAnalogs() {
        return analogs;
    }

    public void setAnalogs(List<String> analogs) {
        this.analogs = analogs;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((analogs == null) ? 0 : analogs.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((medProductId == null) ? 0 : medProductId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (outOfProduction ? 1231 : 1237);
        result = prime * result + ((pharma == null) ? 0 : pharma.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMedProduct that = (AbstractMedProduct) o;
        return outOfProduction == that.outOfProduction &&
                medProductId.equals(that.medProductId) &&
                name.equals(that.name) &&
                pharma.equals(that.pharma) &&
                group == that.group &&
                analogs.equals(that.analogs) &&
                version.equals(that.version);
    }

    @Override
    public String toString() {
        StringBuilder information = new StringBuilder();
        information.append("MedProduct [medProductId=")
                .append(medProductId)
                .append(", outOfProduction=")
                .append(outOfProduction)
                .append(", name=")
                .append(name)
                .append(", pharma=")
                .append(pharma)
                .append(", group=")
                .append(group)
                .append(", analogs=")
                .append(analogs)
                .append(", version=")
                .append(version)
                .append("]");
        return information.toString();
    }

    public static class Version {

        private Country country;
        private String certificate;
        private YearMonth dataFrom;
        private YearMonth dataTo;
        private Pack pack;
        private String dosage;

        public Version() {

        }

        public Version(Country country, String certificate, YearMonth dataFrom, YearMonth dataTo, Pack pack,
                       String dosage) {
            this.country = country;
            this.certificate = certificate;
            this.dataFrom = dataFrom;
            this.dataTo = dataTo;
            this.pack = pack;
            this.dosage = dosage;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public YearMonth getDataFrom() {
            return dataFrom;
        }

        public void setDataFrom(YearMonth dataFrom) {
            this.dataFrom = dataFrom;
        }

        public YearMonth getDataTo() {
            return dataTo;
        }

        public void setDataTo(YearMonth dataTo) {
            this.dataTo = dataTo;
        }

        public Pack getPack() {
            return pack;
        }

        public void setPack(Pack pack) {
            this.pack = pack;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((certificate == null) ? 0 : certificate.hashCode());
            result = prime * result + ((country == null) ? 0 : country.hashCode());
            result = prime * result + ((dataFrom == null) ? 0 : dataFrom.hashCode());
            result = prime * result + ((dataTo == null) ? 0 : dataTo.hashCode());
            result = prime * result + ((dosage == null) ? 0 : dosage.hashCode());
            result = prime * result + ((pack == null) ? 0 : pack.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Version version)) return false;
            return country == version.country
                    && certificate.equals(version.certificate)
                    && dataFrom.equals(version.dataFrom)
                    && dataTo.equals(version.dataTo)
                    && pack == version.pack
                    && dosage.equals(version.dosage);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("Version [country=")
                    .append(country)
                    .append(", certificate=")
                    .append(certificate)
                    .append(", dataFrom=")
                    .append(dataFrom)
                    .append(", dataTo=")
                    .append(dataTo)
                    .append(", pack=")
                    .append(pack)
                    .append(", dosage=")
                    .append(dosage)
                    .append("]")
                    .toString();
        }
    }
}
