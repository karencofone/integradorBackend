package com.example.IntegradorBackend.controllers;

import com.example.IntegradorBackend.dto.TurnoDTO;
import com.example.IntegradorBackend.entidades.Turno;
import com.example.IntegradorBackend.exception.RequestException;
import com.example.IntegradorBackend.service.OdontologoService;
import com.example.IntegradorBackend.service.PacienteService;
import com.example.IntegradorBackend.service.TurnoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/turnos")
public class TurnosController {
    TurnoService turnoService;
    PacienteService pacienteService;
    OdontologoService odontologoService;

    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody TurnoDTO turnoDTO){
        var id_odontologo = turnoDTO.getIdOdontologo();
        var id_paciente = turnoDTO.getIdPaciente();
        var fecha = turnoDTO.getFecha();

        var odontologo = odontologoService.buscarPorId(id_odontologo);
        var paciente = pacienteService.buscarPorId(id_paciente);

        if (odontologo.isEmpty()){
            throw new RequestException("400 Bad Request","Id "+ id_odontologo + " no corresponde a ningún odontólogo");
        } else if (paciente.isEmpty()){
            throw new RequestException("400 Bad Request","Id "+ id_paciente + " no corresponde a ningún paciente");
        } else if (fecha != null){
            turnoService.agregarDTO(turnoDTO);
            return new ResponseEntity<>("El turno fue guardado con éxito", HttpStatus.CREATED);
        } else{
            throw new RequestException("400 Bad Request","Sintaxis inválida");
        }
    }

    @GetMapping("/listar")
    public List<Turno> listar(){
        return turnoService.listar();
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> modificar(@RequestBody TurnoDTO turnoDTO, @PathVariable Long id){

        var id_odontologo = turnoDTO.getIdOdontologo();
        var id_paciente = turnoDTO.getIdPaciente();
        var fecha = turnoDTO.getFecha();
        var odontologo = odontologoService.buscarPorId(id_odontologo);
        var paciente = pacienteService.buscarPorId(id_paciente);

        if (turnoService.buscarPorId(id).isPresent()){
            if ( odontologo.isEmpty()){
                throw new RequestException("400 Bad Request","Id "+ id_odontologo + " no corresponde a ningún odontólogo");
            }
            if ( paciente.isEmpty()){
                throw new RequestException("400 Bad Request","Id "+ id_paciente + " no corresponde a ningún paciente");
            }
            if ( fecha != null){
                turnoService.modificar(turnoDTO, id);
                return new ResponseEntity<>("El turno fue actualizado con éxito", null, HttpStatus.CREATED);
            } else{
                throw new RequestException("400 Bad Request","Sintaxis inválida");
            }
        } else{
            throw new RequestException("400 Bad Request","No existe ningún turno para el id "+id);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){

        var getId = odontologoService.buscarPorId(id);
        turnoService.eliminar(id);
        return new ResponseEntity<>(("El turno con id "+id+" fue eliminado con éxito"), null, HttpStatus.CREATED);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        ResponseEntity<?> respuestaHttp = null;
        var getId = turnoService.buscarPorId(id);

        if (getId.isEmpty()) {
            throw new RequestException("400 Bad Request","Id: "+ id + " no corresponde a ningún turno");
        }
        else {
            respuestaHttp = ResponseEntity.ok(getId);
        }
        return respuestaHttp;
    }

    @GetMapping("/buscarOdontologo/{id}")
    public ResponseEntity<?> buscarOdontologo(@PathVariable Long id){

        ResponseEntity<?> respuestaHttp = null;
        var getOdontologo = turnoService.buscarOdontologo(id);

        if (getOdontologo.isEmpty()) {
            throw new RequestException("400 Bad Request","El odontólogo con id "+ id + " no tiene asignado ningún turno");
        }
        else {
            respuestaHttp = ResponseEntity.ok(getOdontologo);
        }
        return respuestaHttp;
    }

    @GetMapping("/buscarPaciente/{id}")
    public ResponseEntity<?> buscarPaciente(@PathVariable Long id){

        ResponseEntity<?> respuestaHttp = null;
        var getPaciente = turnoService.buscarPaciente(id);

        if (getPaciente.isEmpty()) {
            throw new RequestException("400 Bad Request","El paciente con id "+ id + " no tiene asignado ningún turno");
        }
        else {
            respuestaHttp = ResponseEntity.ok(getPaciente);
        }
        return respuestaHttp;
    }

    @DeleteMapping("/eliminarOdontologo/{id}")
    public ResponseEntity<?> eliminarOdontologo(@PathVariable Long id){

        String respuesta = "";
        ResponseEntity<?> respuestaHttp;

        if (turnoService.buscarOdontologo(id).isEmpty()) {
            throw new RequestException("400 Bad Request","El odontólogo con Id: "+ id + " no tiene asignado ningún turno");
        }
        else {
            turnoService.deleteOdontologo(id);
            return new ResponseEntity<>(("Los turnos del odontólogo con Id: "+id+" se eliminaron  con éxito"), null, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/eliminarPaciente/{id}")
    public ResponseEntity<?> eliminarPaciente(@PathVariable Long id){

        String respuesta = "";
        ResponseEntity<?> respuestaHttp;

        if (turnoService.buscarPaciente(id).isEmpty()) {
            throw new RequestException("400 Bad Request","El paciente con id "+ id + " no tiene asignado ningún turno");
        }
        else {
            turnoService.deletePaciente(id);
            return new ResponseEntity<>(("Los turnos del paciente con id "+id+" fueron eliminados con éxito"), null, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/eliminarRegistros")
    public ResponseEntity<?> eliminarTodos(){
        turnoService.borrarTodos();
        String respuesta ="\n"+"Se eliminaron correctamente todos los registros de turno";
        ResponseEntity<?> respuestaHttp = ResponseEntity.ok(respuesta);;
        return respuestaHttp;
    }
}