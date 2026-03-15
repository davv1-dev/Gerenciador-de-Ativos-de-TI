package br.com.reservasti.domain.categoria.validacoes;

import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarExclusaoCategoriaComEquipamentos implements IValidatorCategoria{
    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Override
    public void validar(CategoriaValidacaoContext context) {
        if(context.id()!=null){
            if (equipamentoRepository.existsByCategoriaId(context.id())) {
                throw new ValidacaoException("Não é possível excluir: existem equipamentos vinculados a esta categoria.");
            }
        }

    }
}
