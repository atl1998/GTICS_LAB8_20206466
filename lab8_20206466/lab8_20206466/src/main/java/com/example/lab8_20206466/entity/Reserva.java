package com.example.lab8_20206466.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity(name="reserva")
@Getter
@Setter
public class Reserva{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreserva")
    private Integer idreserva;

    @Column
    private String nombre;

    @Column
    private String correo;

    @Column
    private Integer numerocupos;

    @ManyToOne
    @JoinColumn(name = "idevento", nullable = false)
    private Evento evento;
}
