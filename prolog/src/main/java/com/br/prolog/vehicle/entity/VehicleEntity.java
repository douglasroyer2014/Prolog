package com.br.prolog.vehicle.entity;

import com.br.prolog.vehicle.enumeration.VehicleStatusType;
import com.br.prolog.vehicletype.entity.VehicleTypeEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "vehicle")
public class VehicleEntity {

    /**
     * Identificador único
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    /**
     * Placa
     */
    String plate;
    /**
     * Marca
     */
    String mark;
    /**
     * QUILOMETRAGEM (KM)
     */
    int mileage;
    /**
     * Status
     */
    @Enumerated(EnumType.STRING)
    VehicleStatusType statusType;
    /**
     * Tipo do veículo
     */
    @ManyToOne
    @JoinColumn(name = "vehicle_type")
    VehicleTypeEntity vehicleType;

    public VehicleEntity(String plate, String mark, int mileage, VehicleStatusType statusType, VehicleTypeEntity vehicleType) {
        this.plate = plate;
        this.mark = mark;
        this.mileage = mileage;
        this.statusType = statusType;
        this.vehicleType = vehicleType;
    }

    public VehicleEntity() {
    }
}
