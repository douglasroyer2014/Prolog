package com.br.prolog.vehicle.service;

import com.br.prolog.tire.dto.TireDTO;
import com.br.prolog.tire.entity.TireEntity;
import com.br.prolog.tire.repository.TireProjection;
import com.br.prolog.tire.service.TireService;
import com.br.prolog.vehicle.dto.VehicleDTO;
import com.br.prolog.vehicle.entity.VehicleEntity;
import com.br.prolog.vehicle.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleService {

    VehicleRepository vehicleRepository;
    TireService tireService;

    /**
     * Busca todos os veículos.
     *
     * @param page página.
     * @param size tamanho da página.
     * @return uma lista de {@link VehicleEntity}.
     */
    public Iterable<VehicleEntity> getAll(int page, int size) {
        return vehicleRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Grava as informações do veículo.
     *
     * @param vehicleDTO informações do veículo.
     */
    public void save(VehicleDTO vehicleDTO) {
        vehicleRepository.save(vehicleDTO.convertToEntity());
    }

    /**
     * Valida se existe veículo cadastrado, caso exista faz a alteração do veículo.
     *
     * @param vehicleDTO informações do veículo.
     * @param id identificador único do veículo.
     * @return a resposta se deu sucesso ou falha.
     */
    public ResponseEntity<String> update(VehicleDTO vehicleDTO, UUID id) {
        Optional<VehicleEntity> vehicleEntityOptional = vehicleRepository.findById(id);
        if (vehicleEntityOptional.isEmpty()) {
            return new ResponseEntity<>("Veículo não encontrado!", HttpStatus.NOT_FOUND);
        }
        VehicleEntity entity = vehicleDTO.convertToEntity();
        entity.setId(id);
        vehicleRepository.save(entity);

        return ResponseEntity.ok("Veículo alterado com sucesso!");
    }

    /**
     * Remove um veículo.
     *
     * @param id identificador único.
     */
    public void delete(UUID id) {
        vehicleRepository.deleteById(id);
    }

    /**
     * Válida se existe veículo, caso exista busca veículo e seus respectivo pneus.
     *
     * @param id identificador único.
     * @return informações do veículo e dos pneus.
     */
    public ResponseEntity<?> get(UUID id) {
        Optional<VehicleEntity> vehicleEntityOptional = vehicleRepository.findById(id);
        if (vehicleEntityOptional.isEmpty()) {
            return new ResponseEntity<>("Veículo não encontrado!", HttpStatus.NOT_FOUND);
        }
        List<TireProjection> tireList = tireService.getAllByVehicleId(id);
        VehicleDTO vehicleDTO = new VehicleDTO(vehicleEntityOptional.get());
        vehicleDTO.setTireList(tireList);

        return new ResponseEntity<>(vehicleDTO, HttpStatus.OK);
    }

    /**
     * Valida se existe veículo, valida se as posições informadas na requisição está vazia ou se repete, valida se os pneus informados existem,
     * valida se as posições informadas já foram cadastrada, valida se a quantidade de pneus cadastrado no veículo é maior que o limite e vincula o pneu com o veículo e posição.
     *
     * @param tireDTOList informações do pneu.
     * @param id identificador único do veículo.
     * @return a resposta de sucesso ou falha.
     */
    public ResponseEntity<String> validAndUpdateTire(List<TireDTO> tireDTOList, UUID id) {
        Optional<VehicleEntity> vehicleEntityOptional = vehicleRepository.findById(id);
        if (vehicleEntityOptional.isEmpty()) {
            return new ResponseEntity<>("Veículo não encontrado!", HttpStatus.NOT_FOUND);
        }

        if (validPosition(tireDTOList)) {
            return new ResponseEntity<>("Posições informadas estão inconsistente, validar se não possui nenhuma posição vazia e não está sendo reptido a posição!", HttpStatus.BAD_REQUEST);
        }

        List<UUID> tireIdList = tireDTOList.stream().map(TireDTO::getId).collect(Collectors.toList());
        List<TireEntity> tireList = tireService.getAllById(tireIdList);
        tireIdList.removeAll(tireList.stream().map(TireEntity::getId).toList());
        if (!tireIdList.isEmpty()) {
            return new ResponseEntity<>("Não foi encontrado registros para o(s) seguinte(s) identificador(es) único(s): "
                    + tireIdList.stream().map(UUID::toString).collect(Collectors.joining(",")), HttpStatus.BAD_REQUEST);
        }

        List<TireProjection> tireProjectionList = tireService.getAllByVehicleId(id);
        if (validPositionValid(tireDTOList, tireProjectionList)) {
            return new ResponseEntity<>("Posições informadas já foram cadastrada!", HttpStatus.BAD_REQUEST);
        }

        if (tireDTOList.size() + tireProjectionList.size() > vehicleEntityOptional.get().getVehicleType().getWheelsCount()) {
            return new ResponseEntity<>("Quantidade de pneus cadastrado passou do limite!", HttpStatus.BAD_REQUEST);
        }

        updateTire(tireDTOList, id);
        return ResponseEntity.ok("Pneu(s) vínculado(s) ao(s) veículo(s)!");
    }

    /**
     * Altera o pneu com a posição informada e com o veículo informado.
     *
     * @param tireDTOList informação do pneu.
     * @param vehicleId identificador único do veículo.
     */
    void updateTire(List<TireDTO> tireDTOList, UUID vehicleId) {
        for (TireDTO tireDTO: tireDTOList) {
            tireService.updateVehicleAndPosition(vehicleId, tireDTO.getPosition(), tireDTO.getId());
        }
    }

    /**
     * Valida se a posição informada já foi cadastrada anteriormente.
     *
     * @param tireDTOList informações do corpo da requisição dos pneus.
     * @param tireProjectionList lista de pneus gravado.
     * @return se as posições informada já foram cadastrada.
     */
    boolean validPositionValid(List<TireDTO> tireDTOList, List<TireProjection> tireProjectionList) {
        List<String> positionList = tireProjectionList.stream().map(TireProjection::getPosition).toList();
        for (TireDTO tireDTO : tireDTOList) {
            if (positionList.contains(tireDTO.getPosition())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Valida se a posição informada está vazio e valida se foi passado a mesma posição.
     *
     * @param tireDTOList informações do pneu.
     * @return se as informações dos pneus estão valida de acordo com a posição.
     */
    boolean validPosition(List<TireDTO> tireDTOList) {
        Set<String> position = new HashSet<>();
        for (TireDTO tireDTO : tireDTOList) {
            if (tireDTO.getPosition().isBlank()) {
                return true;
            }
            position.add(tireDTO.getPosition());
        }

        return position.size() != tireDTOList.size();
    }
}
