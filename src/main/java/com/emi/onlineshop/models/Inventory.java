package com.emi.onlineshop.models;

import jakarta.persistence.*;

@Entity
@Table(name = "inventories")
public class Inventory {
    @Id
    @SequenceGenerator(name = "inventory_seq_generator", sequenceName = "inventory_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_seq_generator")
    private long id;
    private String code;
    private long quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
