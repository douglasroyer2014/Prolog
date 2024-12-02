package com.br.prolog.tire.repository;

import com.br.prolog.tire.enumeration.TireStatusType;

import java.util.UUID;

/**
 * Projeção para mostrar os pneus no veículo.
 */
public interface TireProjection {
    UUID getId();
    int getPositionNumber();
    String getPosition();
    String getMark();
    int getPressure();
    TireStatusType getStatusType();
}
