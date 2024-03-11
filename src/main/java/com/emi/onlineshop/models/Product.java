package com.emi.onlineshop.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {


    @Id
    @SequenceGenerator(name = "product_seq_generator", sequenceName = "product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_generator")
    private long id;

    private String name;

    private String code;

    private String description;

    private Double price;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<TechnicalDetails> technicalDetails = new ArrayList<>();

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<TechnicalDetails> getTechnicalDetails() {
        return technicalDetails;
    }

    public void setTechnicalDetails(List<TechnicalDetails> technicalDetails) {
        this.technicalDetails = technicalDetails;
    }

    public void addTechnicalDetail(TechnicalDetails technicalDetail) {
        technicalDetails.add(technicalDetail);
        technicalDetail.setProduct(this);
    }

    public void removeTechnicalDetail(TechnicalDetails technicalDetail) {
        technicalDetails.remove(technicalDetail);
        technicalDetail.setProduct(null);
    }
}
