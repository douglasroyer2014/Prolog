package com.br.prolog.tire.service;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.repository.TireProjection;
import com.br.prolog.tire.repository.TireRepository;
import com.br.prolog.vehicle.entity.VehicleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TireServiceTest {
    @Mock
    private TireRepository tireRepository;

    @InjectMocks
    private TireService tireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        int page = 0;
        int size = 10;
        Page<TireEntity> tireEntityPage = mock(Page.class);

        Mockito.when(tireRepository.findAll(PageRequest.of(page, size))).thenReturn(tireEntityPage);

        tireService.getAll(page, size);

        Mockito.verify(tireRepository, Mockito.times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    void testSave() {
        TireDTO tireDTO = Mockito.mock(TireDTO.class);

        TireEntity tireEntity = new TireEntity();
        Mockito.when(tireDTO.convertToEntity()).thenReturn(tireEntity);

        tireService.save(tireDTO);

        Mockito.verify(tireDTO, Mockito.times(1)).convertToEntity();
        Mockito.verify(tireRepository, Mockito.times(1)).save(tireEntity);
    }

    @Test
    void testUpdateTireNotFound() {
        UUID id = UUID.randomUUID();
        TireDTO tireDTO = mock(TireDTO.class);
        when(tireRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<String> response = tireService.update(tireDTO, id);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pneu não encontrado!", response.getBody());
        verify(tireRepository, never()).save(any());
    }

    @Test
    void testUpdateTireSuccess() {
        UUID id = UUID.randomUUID();
        TireDTO tireDTO = mock(TireDTO.class);
        TireEntity existingEntity = new TireEntity();
        existingEntity.setVehicle(new VehicleEntity());
        when(tireRepository.findById(id)).thenReturn(Optional.of(existingEntity));

        TireEntity updatedEntity = new TireEntity();
        when(tireDTO.convertToEntity()).thenReturn(updatedEntity);

        ResponseEntity<String> response = tireService.update(tireDTO, id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pneu alterado com sucesso!", response.getBody());
        assertEquals(id, updatedEntity.getId());
        assertEquals(existingEntity.getVehicle(), updatedEntity.getVehicle());
        verify(tireRepository, times(1)).save(updatedEntity);
    }

    @Test
    void testDeleteTire() {
        UUID id = UUID.randomUUID();

        tireService.delete(id);

        verify(tireRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllByVehicleId() {
        UUID vehicleId = UUID.randomUUID();
        TireProjection tireProjection1 = mock(TireProjection.class);
        TireProjection tireProjection2 = mock(TireProjection.class);
        List<TireProjection> tireProjections = List.of(tireProjection1, tireProjection2);

        when(tireRepository.findAllByVehicleId(vehicleId)).thenReturn(tireProjections);

        List<TireProjection> result = tireService.getAllByVehicleId(vehicleId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(tireProjection1));
        assertTrue(result.contains(tireProjection2));
        verify(tireRepository, times(1)).findAllByVehicleId(vehicleId);
    }

    @Test
    void testGetAllById() {
        UUID tireId1 = UUID.randomUUID();
        UUID tireId2 = UUID.randomUUID();
        TireEntity tireEntity1 = mock(TireEntity.class);
        TireEntity tireEntity2 = mock(TireEntity.class);
        List<TireEntity> tireEntities = List.of(tireEntity1, tireEntity2);

        when(tireRepository.findAllById(List.of(tireId1, tireId2))).thenReturn(tireEntities);

        List<TireEntity> result = tireService.getAllById(List.of(tireId1, tireId2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(tireEntity1));
        assertTrue(result.contains(tireEntity2));
        verify(tireRepository, times(1)).findAllById(List.of(tireId1, tireId2));
    }

    @Test
    void testUpdateVehicleAndPosition() {
        UUID vehicleId = UUID.randomUUID();
        String position = "Front Left";
        UUID tireId = UUID.randomUUID();

        tireService.updateVehicleAndPosition(vehicleId, position, tireId);

        verify(tireRepository, times(1)).updateVehicleAndPosition(vehicleId, position, tireId);
    }
}