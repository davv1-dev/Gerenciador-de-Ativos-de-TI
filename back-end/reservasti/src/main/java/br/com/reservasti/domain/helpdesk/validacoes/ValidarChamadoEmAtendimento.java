package br.com.reservasti.domain.helpdesk.validacoes;

import br.com.reservasti.domain.helpdesk.Chamado;
import br.com.reservasti.domain.helpdesk.ChamadoRepository;
import br.com.reservasti.domain.helpdesk.StatusChamado;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarChamadoEmAtendimento implements IValidatorChamado{
    @Autowired
    private ChamadoRepository chamadoRepository;

    @Override
    public void validar(ChamadoContext context) {
        Chamado chamado = chamadoRepository.getReferenceById(context.id());

        if (chamado.getStatus() != StatusChamado.EM_ATENDIMENTO) {
            throw new ValidacaoException("Apenas chamados 'EM_ATENDIMENTO' podem ser resolvidos.");
        }
    }
}
