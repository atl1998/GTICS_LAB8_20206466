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
import java.util.Optional;

@RestController
@RequestMapping("/reserva")
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @PostMapping(value="/crear")
    public ResponseEntity<HashMap<String,Object>> reservarEvento(@RequestParam Integer idevento,
                                           @RequestParam String nombrePersona,
                                           @RequestParam String correoPersona,
                                           @RequestParam int numeroCupos) {
        HashMap<String, Object> responseJson = new HashMap<>();

        // Buscar el evento por ID
        Optional<Evento> optionalEvento = eventoRepository.findById(idevento);
        if (optionalEvento.isEmpty()) {
            responseJson.put("error", "Evento no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseJson);
        }

        Evento evento = optionalEvento.get();

        // Validar disponibilidad de cupos
        Integer cuposDisponibles;
        cuposDisponibles = evento.getCapacidadmaxima() - evento.getReservasactuales();
        if (numeroCupos > cuposDisponibles) {
            responseJson.put("error", "No hay suficientes cupos disponibles para este evento.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
        }

        // Crear la nueva reserva
        Reserva reserva = new Reserva();
        reserva.setEvento(evento);
        reserva.setNombre(nombrePersona);
        reserva.setCorreo(correoPersona);
        reserva.setNumerocupos(numeroCupos);

        // Actualizar la cantidad de reservas del evento
        evento.setReservasactuales(evento.getReservasactuales() + numeroCupos);
        reservaRepository.save(reserva);
        eventoRepository.save(evento);

        responseJson.put("mensaje", "Reserva realizada con éxito.");
        responseJson.put("reserva", reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @DeleteMapping(value = "/borrar/{id}")
    public ResponseEntity<HashMap<String, Object>> borrarReserva(@PathVariable("id") Integer id) {
        HashMap<String, Object> responseMap = new HashMap<>();

        // Verificar si la reserva existe
        if (reservaRepository.existsById(id)) {
            try {
                reservaRepository.deleteById(id);
                responseMap.put("estado", "borrado");
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
    }


}
