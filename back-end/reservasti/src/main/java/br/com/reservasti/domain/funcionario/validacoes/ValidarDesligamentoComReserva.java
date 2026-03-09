package br.com.reservasti.domain.funcionario.validacoes;

import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.StatusReserva;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarDesligamentoComReserva implements IValidatorFuncionario {
    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public void validar(FuncionarioContext context) {
        if(reservaRepository.existsByFuncionarioIdAndStatus(context.id(), StatusReserva.ATIVA)){
             throw new ValidacaoException("Este funcionario não pode ser desligado ele possui uma reserva ativa");
        }
    }
}
