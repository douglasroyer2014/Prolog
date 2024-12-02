package com.br.prolog.vehicle.service;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.repository.TireProjection;
import com.br.prolog.tire.service.TireService;
import com.br.prolog.vehicle.dto.VehicleDTO;
import com.br.prolog.vehicle.entity.VehicleEntity;
import com.br.prolog.vehicle.enumeration.VehicleStatusType;
import com.br.prolog.vehicle.repository.VehicleRepository;
import com.br.prolog.vehicletype.entity.VehicleTypeEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private TireService tireService;

    @Spy
    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<VehicleEntity> vehicleList = Arrays.asList(
                new VehicleEntity("ABC-1234", "Fiat", 15000, VehicleStatusType.AVAILABLE, new VehicleTypeEntity()),
                new VehicleEntity("DEF-5678", "Ford", 20000, VehicleStatusType.AWAITING_REPAIRS, new VehicleTypeEntity())
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<VehicleEntity> page = new PageImpl<>(vehicleList, pageable, vehicleList.size());

        when(vehicleRepository.findAll(pageable)).thenReturn(page);

        Iterable<VehicleEntity> result = vehicleService.getAll(0, 10);

        assertNotNull(result);
        assertTrue(((Page<VehicleEntity>) result).getContent().size() > 0);
        verify(vehicleRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSave() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setPlate("XYZ-9999");
        vehicleDTO.setMark("Chevrolet");
        vehicleDTO.setMileage(25000);
        vehicleDTO.setVehicleType(new VehicleTypeEntity());
        vehicleDTO.setStatusType(VehicleStatusType.DEFECTIVE);

        VehicleEntity vehicleEntity = vehicleDTO.convertToEntity();

        vehicleService.save(vehicleDTO);

        verify(vehicleRepository, times(1)).save(vehicleEntity);
    }

    @Test
    void testUpdateVehicleFound() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setPlate("ABC-1234");
        vehicleDTO.setMark("Fiat");
        vehicleDTO.setMileage(15000);
        vehicleDTO.setVehicleType(new VehicleTypeEntity());
        vehicleDTO.setStatusType(VehicleStatusType.AWAITING_REPAIRS);
        UUID vehicleId = UUID.randomUUID();

        VehicleEntity existingVehicle = new VehicleEntity("ABC-1234", "Fiat", 15000, VehicleStatusType.AVAILABLE, new VehicleTypeEntity());
        existingVehicle.setId(vehicleId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        ResponseEntity<String> response = vehicleService.update(vehicleDTO, vehicleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Veículo alterado com sucesso!", response.getBody());

        verify(vehicleRepository, times(1)).save(any(VehicleEntity.class));
    }

    @Test
    void testUpdateVehicleNotFound() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setPlate("ABC-1234");
        vehicleDTO.setMark("Fiat");
        vehicleDTO.setMileage(15000);
        vehicleDTO.setVehicleType(new VehicleTypeEntity());
        vehicleDTO.setStatusType(VehicleStatusType.DEFECTIVE);
        UUID vehicleId = UUID.randomUUID();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = vehicleService.update(vehicleDTO, vehicleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Veículo não encontrado!", response.getBody());

        verify(vehicleRepository, times(0)).save(any(VehicleEntity.class));
    }

    @Test
    void testDeleteVehicleFound() {
        UUID vehicleId = UUID.randomUUID();

        vehicleService.delete(vehicleId);

        verify(vehicleRepository, times(1)).deleteById(vehicleId);
    }

    @Test
    void testGetVehicleFound() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setId(vehicleId);
        vehicleEntity.setPlate("ABC-1234");
        vehicleEntity.setMark("Marca Teste");
        vehicleEntity.setMileage(10000);
        vehicleEntity.setVehicleType(new VehicleTypeEntity());
        vehicleEntity.setStatusType(VehicleStatusType.AVAILABLE);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicleEntity));

        List<TireProjection> tireList = new ArrayList<>();
        tireList.add(mock(TireProjection.class));

        when(tireService.getAllByVehicleId(vehicleId)).thenReturn(tireList);

        ResponseEntity<?> response = vehicleService.get(vehicleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof VehicleDTO);

        VehicleDTO vehicleDTO = (VehicleDTO) response.getBody();
        assertEquals(tireList, vehicleDTO.getTireList());
    }

    @Test
    void testGetVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = vehicleService.get(vehicleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Veículo não encontrado!", response.getBody());
    }

    @Test
    void testValidAndUpdateTireVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();
        List<TireDTO> tireDTOList = new ArrayList<>();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = vehicleService.validAndUpdateTire(tireDTOList, vehicleId);

        assertEquals("Veículo não encontrado!", response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testValidAndUpdateTireInvalidPosition() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setPosition("Front Left");
        TireDTO tireDTO2 = new TireDTO();
        tireDTO2.setPosition("Front Left");
        List<TireDTO> tireDTOList = List.of(tireDTO1, tireDTO2);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        doReturn(true).when(vehicleService).validPosition(tireDTOList);

        ResponseEntity<String> response = vehicleService.validAndUpdateTire(tireDTOList, vehicleId);

        assertEquals("Posições informadas estão inconsistente, validar se não possui nenhuma posição vazia e não está sendo reptido a posição!", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testValidAndUpdateTireTiresNotFound() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        TireDTO tireDTO = new TireDTO();
        tireDTO.setPosition("Front Left");
        tireDTO.setId(UUID.randomUUID());
        List<TireDTO> tireDTOList = List.of(tireDTO);

        when(tireService.getAllById(anyList())).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = vehicleService.validAndUpdateTire(tireDTOList, vehicleId);

        assertTrue(response.getBody().contains("Não foi encontrado registros para o(s) seguinte(s) identificador(es) único(s):"));
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testValidAndUpdateTirePositionAlreadyExists() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        VehicleTypeEntity vehicleTypeEntity = new VehicleTypeEntity();
        vehicleTypeEntity.setWheelsCount((short) 4);
        vehicle.setVehicleType(vehicleTypeEntity);
        UUID tireId = UUID.randomUUID();
        TireDTO tireDTO = new TireDTO();
        tireDTO.setPosition("Front Left");
        tireDTO.setId(tireId);
        List<TireDTO> tireDTOList = List.of(tireDTO);
        TireProjection tireProjection = mock(TireProjection.class);
        List<TireProjection> existingTires = List.of(tireProjection);
        TireEntity tireEntity = new TireEntity();
        tireEntity.setId(tireId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(tireService.getAllById(tireDTOList.stream().map(TireDTO::getId).collect(Collectors.toList()))).thenReturn(List.of(tireEntity));
        when(tireService.getAllByVehicleId(vehicleId)).thenReturn(existingTires);
        doReturn(true).when(vehicleService).validPositionValid(tireDTOList, existingTires);

        ResponseEntity<String> response = vehicleService.validAndUpdateTire(tireDTOList, vehicleId);

        assertEquals("Posições informadas já foram cadastrada!", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testValidAndUpdateTireExceedsWheelLimit() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        VehicleTypeEntity vehicleTypeEntity = new VehicleTypeEntity();
        vehicleTypeEntity.setWheelsCount((short) 2);
        vehicle.setVehicleType(vehicleTypeEntity);
        UUID tireId1 = UUID.randomUUID();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setPosition("Front Left");
        tireDTO1.setId(tireId1);
        UUID tireId2 = UUID.randomUUID();
        TireDTO tireDTO2 = new TireDTO();
        tireDTO2.setPosition("Front Left");
        tireDTO2.setId(tireId2);
        List<TireDTO> tireDTOList = List.of(tireDTO1, tireDTO2);
        TireProjection tireProjection1 = mock(TireProjection.class);
        TireProjection tireProjection2 = mock(TireProjection.class);
        List<TireProjection> existingTires = List.of(tireProjection1, tireProjection2);
        TireEntity tireEntity1 = new TireEntity();
        tireEntity1.setId(tireId1);
        TireEntity tireEntity2 = new TireEntity();
        tireEntity2.setId(tireId2);

        when(tireProjection1.getPosition()).thenReturn("Back Left");
        when(tireProjection2.getPosition()).thenReturn("Back Right");
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(tireService.getAllById(tireDTOList.stream().map(TireDTO::getId).collect(Collectors.toList()))).thenReturn(List.of(tireEntity1, tireEntity2));
        when(tireService.getAllByVehicleId(vehicleId)).thenReturn(existingTires);
        doReturn(false).when(vehicleService).validPosition(tireDTOList);

        ResponseEntity<String> response = vehicleService.validAndUpdateTire(tireDTOList, vehicleId);

        assertEquals("Quantidade de pneus cadastrado passou do limite!", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testValidAndUpdateTireSuccess() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        VehicleTypeEntity vehicleTypeEntity = new VehicleTypeEntity();
        vehicleTypeEntity.setWheelsCount((short) 4);
        vehicle.setVehicleType(vehicleTypeEntity);
        UUID tireId = UUID.randomUUID();
        TireDTO tireDTO = new TireDTO();
        tireDTO.setPosition("Front Left");
        tireDTO.setId(tireId);
        List<TireDTO> tireDTOList = List.of(tireDTO);
        List<TireProjection> existingTires = List.of();
        TireEntity tireEntity = new TireEntity();
        tireEntity.setId(tireId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(tireService.getAllById(tireDTOList.stream().map(TireDTO::getId).collect(Collectors.toList()))).thenReturn(List.of(tireEntity));
        when(tireService.getAllByVehicleId(vehicleId)).thenReturn(existingTires);
        doNothing().when(vehicleService).updateTire(tireDTOList, vehicleId);

        ResponseEntity<String> response = vehicleService.validAndUpdateTire(tireDTOList, vehicleId);

        assertEquals("Pneu(s) vínculado(s) ao(s) veículo(s)!", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateTire() {
        UUID vehicleId = UUID.randomUUID();
        List<TireDTO> tireDTOList = new ArrayList<>();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setId(UUID.randomUUID());
        tireDTO1.setPosition("Front Left");
        TireDTO tireDTO2 = new TireDTO();
        tireDTO2.setId(UUID.randomUUID());
        tireDTO2.setPosition("Front Right");
        tireDTOList.add(tireDTO1);
        tireDTOList.add(tireDTO2);

        vehicleService.updateTire(tireDTOList, vehicleId);

        for (TireDTO tireDTO : tireDTOList) {
            verify(tireService, times(1)).updateVehicleAndPosition(vehicleId, tireDTO.getPosition(), tireDTO.getId());
        }
    }

    @Test
    void testUpdateTireWithEmptyList() {
        List<TireDTO> tireDTOList = new ArrayList<>();
        UUID vehicleId = UUID.randomUUID();

        vehicleService.updateTire(tireDTOList, vehicleId);

        verify(tireService, never()).updateVehicleAndPosition(any(UUID.class), anyString(), any(UUID.class));
    }

    @Test
    void testValidPositionValid() {
        List<TireDTO> tireDTOList = new ArrayList<>();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setPosition("Front Left");
        TireDTO tireDTO2 = new TireDTO();
        tireDTO2.setPosition("Front Right");
        tireDTOList.add(tireDTO1);
        tireDTOList.add(tireDTO2);
        List<TireProjection> tireProjectionList = new ArrayList<>();
        TireProjection tireProjection1 = mock(TireProjection.class);
        TireProjection tireProjection2 = mock(TireProjection.class);
        tireProjectionList.add(tireProjection1);
        tireProjectionList.add(tireProjection2);

        when(tireProjection1.getPosition()).thenReturn("Front Left");
        when(tireProjection2.getPosition()).thenReturn("Rear Left");

        boolean result = vehicleService.validPositionValid(tireDTOList, tireProjectionList);

        assertTrue(result);
    }

    @Test
    void testValidPositionValidNoDuplicates() {
        List<TireDTO> tireDTOList = new ArrayList<>();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setPosition("Front Left");
        TireDTO tireDTO2 = new TireDTO();
        tireDTO2.setPosition("Front Right");
        tireDTOList.add(tireDTO1);
        tireDTOList.add(tireDTO2);
        List<TireProjection> tireProjectionList = new ArrayList<>();
        TireProjection tireProjection1 = mock(TireProjection.class);
        TireProjection tireProjection2 = mock(TireProjection.class);
        tireProjectionList.add(tireProjection1);
        tireProjectionList.add(tireProjection2);

        when(tireProjection1.getPosition()).thenReturn("Rear Left");
        when(tireProjection2.getPosition()).thenReturn("Rear Right");

        boolean result = vehicleService.validPositionValid(tireDTOList, tireProjectionList);

        assertFalse(result);
    }

    @Test
    void testValidPositionValidEmptyList() {
        List<TireDTO> tireDTOList = new ArrayList<>();
        List<TireProjection> tireProjectionList = new ArrayList<>();

        boolean result = vehicleService.validPositionValid(tireDTOList, tireProjectionList);

        assertFalse(result);
    }

    @Test
    void testValidPositionBlankPosition() {
        List<TireDTO> tireDTOList = new ArrayList<>();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setPosition("");  // Posição em branco
        tireDTOList.add(tireDTO1);

        boolean result = vehicleService.validPosition(tireDTOList);

        assertTrue(result);
    }

    @Test
    void testValidPositionDuplicatePosition() {
        List<TireDTO> tireDTOList = new ArrayList<>();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setPosition("Front Left");
        TireDTO tireDTO2 = new TireDTO();
        tireDTO2.setPosition("Front Left");
        tireDTOList.add(tireDTO1);
        tireDTOList.add(tireDTO2);

        boolean result = vehicleService.validPosition(tireDTOList);

        assertTrue(result);
    }

    @Test
    void testValidPositionUniquePositions() {
        List<TireDTO> tireDTOList = new ArrayList<>();
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setPosition("Front Left");
        TireDTO tireDTO2 = new TireDTO();
        tireDTO2.setPosition("Front Right");
        tireDTOList.add(tireDTO1);
        tireDTOList.add(tireDTO2);

        boolean result = vehicleService.validPosition(tireDTOList);

        assertFalse(result);
    }

    @Test
    void testValidPositionEmptyList() {
        List<TireDTO> tireDTOList = new ArrayList<>();

        boolean result = vehicleService.validPosition(tireDTOList);

        assertFalse(result);
    }
}