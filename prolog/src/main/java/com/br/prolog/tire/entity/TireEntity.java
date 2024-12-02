package com.br.prolog.tire.entity;

import com.br.prolog.tire.enumeration.TireStatusType;
import com.br.prolog.vehicle.entity.VehicleEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "tire")
public class TireEntity {

    /**
     * Identificador único
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    @Enumerated(EnumType.STRING)
    TireStatusType statusType;
    /**
     * Veículo
     */
    @ManyToOne
    @JoinColumn(name = "vehicle")
    VehicleEntity vehicle;
}
