package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "STOCK_TYPE")
public class StockType {

    @Id
    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "INDEX")
    private String index;

    public StockType() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockType stockType = (StockType) o;
        return Objects.equals(companyName, stockType.companyName) && Objects.equals(index, stockType.index);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(companyName);
        result = 31 * result + Objects.hashCode(index);
        return result;
    }

    @Override
    public String toString() {
        return "StockType{" +
                "companyName='" + companyName + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}
