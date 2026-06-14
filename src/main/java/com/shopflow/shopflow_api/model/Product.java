package com.shopflow.shopflow_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;


/*
@Entity — dice "questa classe è mappata su una tabella, gestiscila tu Hibernate". È quello che la rende un'entità.
@Table(name = "products") — specifica il nome della tabella. È opzionale (di default userebbe il nome della classe), ma essere espliciti è buona pratica.
@Id — segna il campo che è la chiave primaria (l'identificatore univoco della riga).
@GeneratedValue(strategy = GenerationType.IDENTITY) — dice che l'id lo genera il database in automatico (colonna auto-incrementante). Questo è un cambiamento concettuale importante: l'id non lo assegni più tu nel codice, lo decide il DB a ogni inserimento.
@Column(nullable = false) significa "questa colonna non può essere vuota", e precision/scale definiscono quante cifre totali e quante decimali per il prezzo (12 cifre, di cui 2 decimali). Sono buone pratiche a prescindere da Flyway.
*/

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer stock;

    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}