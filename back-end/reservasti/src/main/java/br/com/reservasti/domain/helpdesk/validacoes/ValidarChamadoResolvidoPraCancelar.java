package br.com.reservasti.domain.helpdesk.validacoes;

import br.com.reservasti.domain.helpdesk.Chamado;
import br.com.reservasti.domain.helpdesk.ChamadoRepository;
import br.com.reservasti.domain.helpdesk.StatusChamado;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidarChamadoResolvidoPraCancelar implements IValidatorChamado{
    @Autowired
    private ChamadoRepository chamadoRepository;

    @Override
    public void validar(ChamadoContext context) {
        Chamado chamado = chamadoRepository.getReferenceById(context.id());

        if (chamado.getStatus() == StatusChamado.RESOLVIDO || chamado.getStatus() == StatusChamado.CANCELADO) {
            throw new ValidacaoException("Este chamado já está encerrado e não pode ser cancelado.");
        }
    }
}
