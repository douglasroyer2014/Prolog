package com.br.prolog.tire.controller;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.service.TireService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tire")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TireController {

    TireService tireService;

    @GetMapping
    public Iterable<TireEntity> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return tireService.getAll(page, size);
    }

    @PostMapping
    public ResponseEntity<String> save(@Valid @RequestBody TireDTO tireDTO) {
        tireService.save(tireDTO);
        return ResponseEntity.ok("Pneu cadastrado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@Valid @RequestBody TireDTO tireDTO, @PathVariable UUID id) {
        return tireService.update(tireDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        tireService.delete(id);
        return ResponseEntity.ok("Pneu deletado com sucesso!");
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> removeVehicleAndPosition(@PathVariable UUID id) {
        tireService.updateVehicleAndPosition(null, null, id);
        return ResponseEntity.ok("Pneu desvinculado com o veículo!");
    }
}
