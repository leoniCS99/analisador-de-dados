package br.com.agi.model;

import java.math.BigDecimal;

public class SaleItem {

    private final int id;
    private final int quantity;
    private final BigDecimal price;

    public SaleItem(int id, int quantity, BigDecimal price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
