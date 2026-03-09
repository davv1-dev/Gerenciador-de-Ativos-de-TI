package br.com.reservasti.domain.equipamento.validacoes;

import br.com.reservasti.domain.equipamento.dto.EquipamentoDTO;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.infra.exceptions.ConcorrenciaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarPatrimonioDuplicado implements IValidatorEquipamento {
    @Autowired
    private EquipamentoRepository repository;

    @Override
    public void validar(EquipamentoDTO dto) {
        if (repository.existsByNumeroPatrimonio(dto.numeroPatrimonio())) {
            throw new ConcorrenciaException("Já existe um equipamento cadastrado com este número de patrimônio!");
        }
    }
}
