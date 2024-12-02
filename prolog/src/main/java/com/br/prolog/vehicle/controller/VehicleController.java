package com.br.prolog.vehicle.controller;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.vehicle.dto.VehicleDTO;
import com.br.prolog.vehicle.entity.VehicleEntity;
import com.br.prolog.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vehicle")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleController {

    VehicleService vehicleService;

    @GetMapping
    public Iterable<VehicleEntity> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return vehicleService.getAll(page, size);
    }

    @PostMapping
    public ResponseEntity<String> save(@Valid @RequestBody VehicleDTO vehicleDTO) {
        vehicleService.save(vehicleDTO);
        return ResponseEntity.ok("Veículo cadastrado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@Valid @RequestBody VehicleDTO vehicleDTO, @PathVariable UUID id) {
        return vehicleService.update(vehicleDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        vehicleService.delete(id);
        return ResponseEntity.ok("Veículo deletado com sucesso!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        return vehicleService.get(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> saveTire(@RequestBody List<TireDTO> tireDTOList, @PathVariable UUID id) {
        return vehicleService.validAndUpdateTire(tireDTOList, id);
    }

}
