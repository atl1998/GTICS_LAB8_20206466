package com.example.lab8_20206466.controller;

import com.example.lab8_20206466.entity.*;
import com.example.lab8_20206466.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.text.ParseException;

@RestController
@RequestMapping("/evento")
public class EventoController {
    @Autowired
    private EventoRepository eventoRepository;

    /*Listado de eventos (4 puntos):
   Un endpoint para obtener todos los eventos disponibles
   que permita filtrar por fecha. La respuesta debe ser
   una lista ordenada por fecha del evento y contener los siguientes atributos:*/
    @GetMapping(value = {"/list", ""})
    public List<Evento> listaEventos(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") String fechaTexto) {

        // Obtener todos los eventos de la base de datos
        List<Evento> eventos = eventoRepository.findAll();

        // Filtrar por fecha solo si en caso se indica
        if (fechaTexto != null && !fechaTexto.isEmpty()) {
            try {
                // Convertir texto a Date
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd-MM-yyyy");
                Date fechaEntrada = formatoEntrada.parse(fechaTexto);

                // Convertir la fecha de entrada al formato que está en la BD (yyyy-MM-dd)
                SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = formatoSalida.format(fechaEntrada);

                // Filtrar eventos que coinciden con la fecha
                eventos = eventos.stream()
                        .filter(evento -> formatoSalida.format(evento.getFecha()).equals(fechaFormateada))
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                e.printStackTrace(); // Manejo de excepciones si el formato es incorrecto
            }
        }

        // Ordenar los eventos por fecha
        eventos.sort(Comparator.comparing(Evento::getFecha));

        return eventos;
    }


    /*Crear un nuevo evento en el sistema
    con los atributos mencionados anteriormente.
    Validar que la fecha del evento sea en el futuro.
    Al agregar, el evento debe iniciar con cero reservas.*/
    // 2. Crear un nuevo evento
    @PostMapping(value = {"/crear", ""})
    public ResponseEntity<HashMap<String, Object>> crearEvento(@RequestBody Evento evento) {
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
        responseJson.put("estado", "creado");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    /*Reservar un Lugar en un Evento (6 puntos):
Un endpoint para registrar una reserva en un evento,
asegurándose de que haya cupos disponibles
(Validación con el número de reservas actuales
y la capacidad máxima).
Para reservar el evento se deben enviar los siguientes atributos:*/





}
