package com.br.prolog.vehicle.controller;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.vehicle.dto.VehicleDTO;
import com.br.prolog.vehicle.entity.VehicleEntity;
import com.br.prolog.vehicle.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVehicles() {
        VehicleEntity vehicleEntity1 = mock(VehicleEntity.class);
        VehicleEntity vehicleEntity2 = mock(VehicleEntity.class);
        List<VehicleEntity> vehicleEntities = List.of(vehicleEntity1, vehicleEntity2);

        when(vehicleService.getAll(0, 10)).thenReturn(vehicleEntities);

        Iterable<VehicleEntity> result = vehicleController.getAll(0, 10);

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        verify(vehicleService, times(1)).getAll(0, 10);
    }

    @Test
    void testSaveVehicle() {
        VehicleDTO vehicleDTO = mock(VehicleDTO.class);

        ResponseEntity<String> response = vehicleController.save(vehicleDTO);

        assertEquals("Veículo cadastrado com sucesso!", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(vehicleService, times(1)).save(vehicleDTO);
    }

    @Test
    void testUpdateVehicle() {
        VehicleDTO vehicleDTO = mock(VehicleDTO.class);
        UUID vehicleId = UUID.randomUUID();

        when(vehicleService.update(vehicleDTO, vehicleId)).thenReturn(ResponseEntity.ok("Veículo alterado com sucesso!"));

        ResponseEntity<String> response = vehicleController.update(vehicleDTO, vehicleId);

        assertEquals("Veículo alterado com sucesso!", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(vehicleService, times(1)).update(vehicleDTO, vehicleId);
    }

    @Test
    void testDeleteVehicle() {
        UUID vehicleId = UUID.randomUUID();

        ResponseEntity<String> response = vehicleController.delete(vehicleId);

        assertEquals("Veículo deletado com sucesso!", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(vehicleService, times(1)).delete(vehicleId);
    }

    @Test
    void testGetVehicle() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicleEntity = mock(VehicleEntity.class);
        ResponseEntity responseEntity = ResponseEntity.ok(vehicleEntity);

        when(vehicleService.get(vehicleId)).thenReturn(responseEntity);

        ResponseEntity<?> response = vehicleController.get(vehicleId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicleEntity, response.getBody());
        verify(vehicleService, times(1)).get(vehicleId);
    }

    @Test
    void testSaveTire() {
        UUID vehicleId = UUID.randomUUID();
        TireDTO tireDTO = mock(TireDTO.class);
        List<TireDTO> tireDTOList = List.of(tireDTO);

        when(vehicleService.validAndUpdateTire(tireDTOList, vehicleId)).thenReturn(ResponseEntity.ok("Pneu(s) vinculado(s) ao veículo com sucesso!"));

        ResponseEntity<String> response = vehicleController.saveTire(tireDTOList, vehicleId);

        assertEquals("Pneu(s) vinculado(s) ao veículo com sucesso!", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(vehicleService, times(1)).validAndUpdateTire(tireDTOList, vehicleId);
    }
}