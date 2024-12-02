package com.br.prolog.vehicle.dto;

import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.repository.TireProjection;
import com.br.prolog.vehicle.entity.VehicleEntity;
import com.br.prolog.vehicle.enumeration.VehicleStatusType;
import com.br.prolog.vehicletype.entity.VehicleTypeEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VehicleDTO {
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
    VehicleStatusType statusType;
    /**
     * Tipo do veículo
     */
    @NotNull(message = "O tipo do veículo é obrigatório!")
    VehicleTypeEntity vehicleType;
    /**
     * Lista de pneus
     */
    List<TireProjection> tireList;

    /**
     * Converte o DTO para um {@link VehicleEntity}.
     *
     * @return um {@link VehicleEntity}.
     */
    public VehicleEntity convertToEntity() {
        VehicleEntity entity = new VehicleEntity();
        entity.setPlate(this.getPlate());
        entity.setMark(this.getMark());
        entity.setMileage(this.getMileage());
        entity.setStatusType(this.getStatusType());
        entity.setVehicleType(this.getVehicleType());

        return entity;
    }

    /**
     * Converte a entidade para um DTO.
     *
     * @param entity informações da entidade.
     */
    public VehicleDTO(VehicleEntity entity) {
        this.setPlate(entity.getPlate());
        this.setMark(entity.getMark());
        this.setMileage(entity.getMileage());
        this.setVehicleType(entity.getVehicleType());
        this.setStatusType(entity.getStatusType());
    }

    public VehicleDTO() {
    }
}
