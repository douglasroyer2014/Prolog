package com.br.prolog.vehicletype.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "vehicle_type")
public class VehicleTypeEntity {

    @Id
    UUID id;

    String name;
    short wheelsCount;
}
