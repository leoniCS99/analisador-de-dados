package br.com.agi.model;

import java.math.BigDecimal;
import java.util.List;

public class Sale {

    private final String id;
    private final List<SaleItem> items;
    private final String salesmanName;

    public Sale(String id,
                List<SaleItem> items,
                String salesmanName) {

        this.id = id;
        this.items = items;
        this.salesmanName = salesmanName;
    }

    public String getId() {
        return id;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(SaleItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
