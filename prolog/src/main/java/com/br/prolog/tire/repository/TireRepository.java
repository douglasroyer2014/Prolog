package com.br.prolog.tire.repository;

import com.br.prolog.tire.entity.TireEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TireRepository extends JpaRepository<TireEntity, UUID> {

    /**
     * Busca todos os pneus que estão no veículo.
     *
     * @param vehicleId identificador único do veículo.
     * @return uma lista de {@link TireProjection}.
     */
    List<TireProjection> findAllByVehicleId(UUID vehicleId);

    /**
     * Altera o veículo e a posição do pneu de acordo com o identificador único do pneu.
     *
     * @param vehicleId identificador único do veículo.
     * @param position posição do pneu.
     * @param id identificador único do pneu.
     * @return quantidade de registro alterado.
     */
    @Modifying
    @Query("UPDATE TireEntity t SET vehicle.id = :vehicleId, position = :position where id = :tireId")
    int updateVehicleAndPosition(@Param("vehicleId") UUID vehicleId, @Param("position") String position, @Param("tireId") UUID id);
}
