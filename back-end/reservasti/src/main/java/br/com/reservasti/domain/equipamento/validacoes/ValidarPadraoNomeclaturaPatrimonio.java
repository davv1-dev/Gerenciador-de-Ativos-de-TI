package br.com.reservasti.domain.equipamento.validacoes;

import br.com.reservasti.domain.equipamento.dto.EquipamentoDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidarPadraoNomeclaturaPatrimonio implements IValidatorEquipamento {
    @Override
    public void validar(EquipamentoDTO dto) {

        String regexPadrao = "^[A-Z]{2,4}-\\d{4,6}$";

        if (!dto.numeroPatrimonio().matches(regexPadrao)) {
            throw new IllegalArgumentException("O número de patrimônio deve seguir o padrão da empresa (Ex: TI-1234). Valor recebido: " + dto.numeroPatrimonio()
            );
        }
    }
}
