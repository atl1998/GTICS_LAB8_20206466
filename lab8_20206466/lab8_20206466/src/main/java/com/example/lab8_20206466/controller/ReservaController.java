package com.example.lab8_20206466.controller;
import com.example.lab8_20206466.entity.*;
import com.example.lab8_20206466.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reserva")
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @PostMapping(value="/crear")
    public ResponseEntity<?> reservarEvento(
            @RequestParam Integer idevento,
            @RequestParam String nombrePersona,
            @RequestParam String correoPersona,
            @RequestParam int numeroCupos) {

        // Validar que el número de cupos sea positivo
        if (numeroCupos <= 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "El número de cupos debe ser mayor que cero."));
        }

        HashMap<String, Object> responseJson = new HashMap<>();

        // Buscar el evento por ID
        Optional<Evento> optionalEvento = eventoRepository.findById(idevento);
        if (optionalEvento.isEmpty()) {
            responseJson.put("error", "Evento no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseJson);
        }

        Evento evento = optionalEvento.get();

        // Validar disponibilidad de cupos
        Integer cuposDisponibles = evento.getCapacidadmaxima() - evento.getReservasactuales();
        if (numeroCupos > cuposDisponibles) {
            responseJson.put("error", "No hay suficientes cupos disponibles para este evento.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
        }

        // Crear la nueva reserva
        Reserva reserva = new Reserva();
        reserva.setEvento(evento);
        reserva.setNombrepersona(nombrePersona);
        reserva.setCorreopersona(correoPersona);
        reserva.setNumerocupos(numeroCupos);

        // Actualizar la cantidad de reservas del evento
        evento.setReservasactuales(evento.getReservasactuales() + numeroCupos);
        reservaRepository.save(reserva); // Guardar reserva
        eventoRepository.save(evento); // Actualizar evento

        responseJson.put("mensaje", "Reserva realizada con éxito.");
        responseJson.put("reserva", reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @DeleteMapping(value = "/borrar/{id}")
    public ResponseEntity<HashMap<String, Object>> borrarReserva(@PathVariable("id") String id) {
        HashMap<String, Object> responseMap = new HashMap<>();

        // Validar si el ID es un número
        try {
            Integer idNumber = Integer.parseInt(id);

            // Buscar la reserva por ID
            Optional<Reserva> reservaOptional = reservaRepository.findById(idNumber);

            if (reservaOptional.isPresent()) {
                Reserva reserva = reservaOptional.get();
                Evento evento = reserva.getEvento();

                // Sumamos los cupos de la reserva eliminada a las reservas actuales del evento
                evento.setReservasactuales(evento.getReservasactuales() - reserva.getNumerocupos());

                try {
                    // Guardar cambios en el evento y luego borrar la reserva
                    eventoRepository.save(evento);
                    reservaRepository.deleteById(idNumber);

                    responseMap.put("estado", "borrado");
                    responseMap.put("msg", "Reserva eliminada y cupos liberados.");
                    return ResponseEntity.ok(responseMap);
                } catch (Exception ex) {
                    responseMap.put("estado", "error");
                    responseMap.put("msg", "Error al intentar borrar la reserva: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
                }
            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "No se encontró la reserva con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Formato de ID incorrecto. El ID debe ser un número.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }
    }


}
