package com.br.prolog.tire.service;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.repository.TireProjection;
import com.br.prolog.tire.repository.TireRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TireService {

    TireRepository tireRepository;

    /**
     * Busca todos os pneus disponivel de acordo com a página e tamanho por página.
     *
     * @param page pagina da consulta.
     * @param size tamanho da consulta.
     * @return uma lista de {@link TireEntity}.
     */
    public Iterable<TireEntity> getAll(int page, int size) {
        return tireRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Grava as informações do pneu.
     *
     * @param tireDTO informações do pneu.
     */
    public void save(TireDTO tireDTO) {
        tireRepository.save(tireDTO.convertToEntity());
    }

    /**
     * Válida se o pneu existe, caso exista altera as informações do pneu.
     *
     * @param tireDTO informções do pneu.
     * @param id idenficador único do pneu.
     * @return a resposta indicando se foi sucesso ou falha.
     */
    public ResponseEntity<String> update(TireDTO tireDTO, UUID id) {
        Optional<TireEntity> tireEntityOptional = tireRepository.findById(id);
        if (tireEntityOptional.isEmpty()) {
            return new ResponseEntity<>("Pneu não encontrado!", HttpStatus.NOT_FOUND);
        }
        TireEntity entity = tireDTO.convertToEntity();
        entity.setId(id);
        entity.setVehicle(tireEntityOptional.get().getVehicle());
        tireRepository.save(entity);
        return ResponseEntity.ok("Pneu alterado com sucesso!");
    }

    /**
     * Remove pneu de acordo com o identificador único.
     *
     * @param id identificador único.
     */
    public void delete(UUID id) {
        tireRepository.deleteById(id);
    }

    /**
     * Busca todos os pneus que estão no veículo.
     *
     * @param vehicleId identificador único do veículo.
     * @return uma lista de informações dos pneus vínculado ao veículo.
     */
    public List<TireProjection> getAllByVehicleId(UUID vehicleId) {
        return tireRepository.findAllByVehicleId(vehicleId);
    }

    /**
     * Busca todos os pneus de acordo com a lista de identificadores únicos dos pneus.
     *
     * @param tireIdList lista de identificadore únicos de pneu.
     * @return uma lista de {@link TireEntity}.
     */
    public List<TireEntity> getAllById(List<UUID> tireIdList) {
        return tireRepository.findAllById(tireIdList);
    }

    /**
     * Altera o veículo e a posição do pneu de acordo com o identificador único do pneu.
     *
     * @param vehicleId identificador único do veículo.
     * @param position posição do pneu.
     * @param id identificador único do pneu.
     */
    @Transactional
    public void updateVehicleAndPosition(UUID vehicleId, String position, UUID id) {
        tireRepository.updateVehicleAndPosition(vehicleId, position, id);
    }
}
