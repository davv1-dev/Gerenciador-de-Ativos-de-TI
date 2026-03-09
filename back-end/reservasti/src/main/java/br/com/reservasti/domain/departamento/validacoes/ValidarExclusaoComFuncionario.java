package br.com.reservasti.domain.departamento.validacoes;

import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidarExclusaoComFuncionario implements IValidatorDepartamento{
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public void validar(DepartamentoValidacaoContext context) {
        if (funcionarioRepository.existsByDepartamentoId(context.id())) {
            throw new ValidacaoException("Não é possível excluir: existem funcionários neste departamento.");
        }
    }
}
