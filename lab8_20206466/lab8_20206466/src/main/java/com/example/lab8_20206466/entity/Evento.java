package com.example.lab8_20206466.entity;

import jakarta.persistence.*;
import java.util.Date;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Entity (name="evento")
@Getter
@Setter
public class Evento{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idevento")
    private Integer idevento;

    @Column
    private String nombre;

    @Column
    private Date fecha;

    @Column
    private String categoria;

    @Column
    private Integer capacidadmaxima;

    @Column
    private Integer reservasactuales;


}
