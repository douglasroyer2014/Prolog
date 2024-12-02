package com.br.prolog.vehicle.dto;

import com.br.prolog.vehicle.entity.VehicleEntity;
import com.br.prolog.vehicle.enumeration.VehicleStatusType;
import com.br.prolog.vehicletype.entity.VehicleTypeEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehicleDTOTest {

    @Test
    void testConvertToEntity() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setPlate("ABC-1234");
        vehicleDTO.setMark("Fiat");
        vehicleDTO.setMileage(15000);
        vehicleDTO.setStatusType(VehicleStatusType.AVAILABLE);
        vehicleDTO.setVehicleType(new VehicleTypeEntity());

        VehicleEntity entity = vehicleDTO.convertToEntity();

        assertNotNull(entity); // Certifique-se de que a entidade não é null
        assertEquals("ABC-1234", entity.getPlate());
        assertEquals("Fiat", entity.getMark());
        assertEquals(15000, entity.getMileage());
        assertEquals(VehicleStatusType.AVAILABLE, entity.getStatusType());
        assertEquals(new VehicleTypeEntity(), entity.getVehicleType());
    }

    @Test
    void testConstructorFromEntity() {
        // Criação de uma instância de VehicleEntity
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setPlate("ABC-1234");
        vehicleEntity.setMark("Fiat");
        vehicleEntity.setMileage(15000);
        vehicleEntity.setStatusType(VehicleStatusType.AWAITING_REPAIRS);
        vehicleEntity.setVehicleType(new VehicleTypeEntity());

        // Criação do VehicleDTO a partir da entidade
        VehicleDTO vehicleDTO = new VehicleDTO(vehicleEntity);

        // Verificação dos valores do DTO
        assertNotNull(vehicleDTO);
        assertEquals("ABC-1234", vehicleDTO.getPlate());
        assertEquals("Fiat", vehicleDTO.getMark());
        assertEquals(15000, vehicleDTO.getMileage());
        assertEquals(VehicleStatusType.AWAITING_REPAIRS, vehicleDTO.getStatusType());
        assertEquals(new VehicleTypeEntity(), vehicleDTO.getVehicleType());
    }
}