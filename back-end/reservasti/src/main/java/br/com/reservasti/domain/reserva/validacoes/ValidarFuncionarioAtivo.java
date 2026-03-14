package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarFuncionarioAtivo implements IValidatorReservaAbertura {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public void validar(ReservaContext context) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(context.dto().funcionarioId());
        if (!funcionario.getAtivo()) {
            throw new ValidacaoException("Funcionários inativos não podem fazer reservas.");
        }
    }
}
