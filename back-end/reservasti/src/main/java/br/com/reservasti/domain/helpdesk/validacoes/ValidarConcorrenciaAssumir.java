package br.com.reservasti.domain.helpdesk.validacoes;

import br.com.reservasti.domain.helpdesk.Chamado;
import br.com.reservasti.domain.helpdesk.ChamadoRepository;
import br.com.reservasti.domain.helpdesk.StatusChamado;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidarConcorrenciaAssumir implements IValidatorChamado{
    @Autowired
    private ChamadoRepository chamadoRepository;

    @Override
    public void validar(ChamadoContext context) {
        Chamado chamado = chamadoRepository.getReferenceById(context.id());
        if (chamado.getStatus() != StatusChamado.NA_FILA) {
            throw new ValidacaoException("Este chamado já foi assumido por outro técnico ou não está na Fila Global.");
        }
    }
}
