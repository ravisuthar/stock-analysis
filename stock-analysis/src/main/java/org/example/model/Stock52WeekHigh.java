package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "STOCK")
public class Stock52WeekHigh {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "TRADE_DATE")
    private LocalDate tradeDate;

    @Column(name = "HIGH")
    private String high;

    @Column(name = "LOW")
    private String low;

    @Column(name = "LAST_PRICE")
    private String lastPrice;

    @Column(name = "CHANGE")
    private String change;

    @Column(name = "CHANGE_PERCENTAGE")
    private String changePercentage;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "INDEX", referencedColumnName = "INDEX")
    private String index;

    public Stock52WeekHigh() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(String changePercentage) {
        this.changePercentage = changePercentage;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
