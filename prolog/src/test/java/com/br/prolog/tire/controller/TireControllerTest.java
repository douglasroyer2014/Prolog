package com.br.prolog.tire.controller;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.tire.service.TireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TireControllerTest {

    @Mock
    private TireService tireService;

    @InjectMocks
    private TireController tireController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        tireController.getAll(0, 10);

        verify(tireService, times(1)).getAll(0, 10);
    }

    @Test
    void testSave() {
        TireDTO tireDTO = new TireDTO();

        tireController.save(tireDTO);

        verify(tireService, times(1)).save(tireDTO);
    }

    @Test
    void testUpdate() {
        TireDTO tireDTO = new TireDTO();
        UUID id = UUID.randomUUID();

        when(tireService.update(tireDTO, id)).thenReturn(ResponseEntity.ok("Pneu atualizado com sucesso!"));

        tireController.update(tireDTO, id);

        verify(tireService, times(1)).update(tireDTO, id);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        tireController.delete(id);

        verify(tireService, times(1)).delete(id);
    }

    @Test
    void testRemoveVehicleAndPosition() {
        UUID id = UUID.randomUUID();

        tireController.removeVehicleAndPosition(id);

        verify(tireService, times(1)).updateVehicleAndPosition(null, null, id);
    }
}
