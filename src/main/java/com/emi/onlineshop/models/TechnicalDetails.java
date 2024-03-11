package com.emi.onlineshop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class TechnicalDetails {

    @Id
    @SequenceGenerator(name = "technical_details_seq_generator", sequenceName = "technical_details_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "technical_details_seq_generator")
    private long id;

    private String name;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product product;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
