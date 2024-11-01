package com.example.lab8_20206466.controller;

import com.example.lab8_20206466.entity.*;
import com.example.lab8_20206466.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/evento")
public class EventoController {
    @Autowired
    private EventoRepository eventoRepository;

    /*Listado de eventos (4 puntos):
   Un endpoint para obtener todos los eventos disponibles
   que permita filtrar por fecha. La respuesta debe ser
   una lista ordenada por fecha del evento y contener los siguientes atributos:*/
    @GetMapping(value={"/list",""})
    public List<Evento> listaEventos(){
        return eventoRepository.findAll();
    }

    /*Crear un nuevo evento en el sistema
    con los atributos mencionados anteriormente.
    Validar que la fecha del evento sea en el futuro.
    Al agregar, el evento debe iniciar con cero reservas.*/
    // 2. Crear un nuevo evento
    @PostMapping
    public ResponseEntity<HashMap<String,Object>> crearEvento(@RequestBody Evento evento) {
        HashMap<String, Object> responseJson = new HashMap<>();

        // Validar que la fecha del evento sea en el futuro
        Date fechaActual = new Date();
        if (evento.getFecha().before(fechaActual)) {
            responseJson.put("error", "La fecha del evento debe ser en el futuro.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
        }

        // Iniciar el evento con cero reservas actuales
        evento.setReservasactuales(0);
        eventoRepository.save(evento);

        responseJson.put("mensaje", "Evento creado correctamente.");
        responseJson.put("evento", evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    /*Reservar un Lugar en un Evento (6 puntos):
Un endpoint para registrar una reserva en un evento,
asegurándose de que haya cupos disponibles
(Validación con el número de reservas actuales
y la capacidad máxima).
Para reservar el evento se deben enviar los siguientes atributos:*/





}
