package com.br.prolog.tire.dto;

import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.enumeration.TireStatusType;
import lombok.Data;

import java.util.UUID;

@Data
public class TireDTO {

    /**
     * Identificador único
     */
    UUID id;
    /**
     * Número de fogo
     */
    int positionNumber;
    /**
     * Posição
     */
    String position;
    /**
     * Marca
     */
    String mark;
    /**
     * Pressão atual
     */
    int pressure;
    /**
     * Status
     */
    TireStatusType statusType;

    /**
     * Converte dto para um {@link TireEntity}.
     *
     * @return um {@link TireEntity}.
     */
    public TireEntity convertToEntity() {
        TireEntity entity = new TireEntity();
        entity.setPositionNumber(this.getPositionNumber());
        entity.setMark(this.getMark());
        entity.setPressure(this.getPressure());
        entity.setStatusType(this.getStatusType());

        return entity;
    }
}
