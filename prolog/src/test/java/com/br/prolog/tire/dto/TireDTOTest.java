package com.br.prolog.tire.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.enumeration.TireStatusType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TireDTOTest {

    @Test
    void testConvertToEntity() {
        TireDTO tireDTO = new TireDTO();
        tireDTO.setPositionNumber(1);
        tireDTO.setMark("Goodyear");
        tireDTO.setPressure(320);
        tireDTO.setStatusType(TireStatusType.IN_USE);

        TireEntity tireEntity = tireDTO.convertToEntity();

        assertNotNull(tireEntity, "A entidade não deve ser nula");
        assertEquals(tireDTO.getPositionNumber(), tireEntity.getPositionNumber(), "O número da posição deve ser igual");
        assertEquals(tireDTO.getMark(), tireEntity.getMark(), "A marca deve ser igual");
        assertEquals(tireDTO.getPressure(), tireEntity.getPressure(), "A pressão deve ser igual");
        assertEquals(tireDTO.getStatusType(), tireEntity.getStatusType(), "O tipo de status deve ser igual");
    }
}