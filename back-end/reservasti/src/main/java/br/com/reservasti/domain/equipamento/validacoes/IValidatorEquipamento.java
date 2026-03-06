package br.com.reservasti.domain.equipamento.validacoes;

import br.com.reservasti.domain.equipamento.dto.EquipamentoDTO;
import org.springframework.stereotype.Component;

@Component
public interface IValidatorEquipamento {
    void validar(EquipamentoDTO dto);
}
