package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarFuncionarioAtivo implements IValidatorReserva{
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public void validar(ReservaDTO dto) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(dto.funcionarioId());
        if (!funcionario.getAtivo()) {
            throw new ValidacaoException("Funcionários inativos não podem fazer reservas.");
        }
    }
}
