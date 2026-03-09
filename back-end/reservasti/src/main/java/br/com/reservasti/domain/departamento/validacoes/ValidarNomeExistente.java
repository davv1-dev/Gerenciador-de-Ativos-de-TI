package br.com.reservasti.domain.departamento.validacoes;

import br.com.reservasti.domain.departamento.DepartamentoRepository;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarNomeExistente implements IValidatorDepartamento{
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public void validar(DepartamentoValidacaoContext context) {
        if (departamentoRepository.existsByNome(context.dto().nome())) {
            throw new ValidacaoException("Nome de departamento já cadastrado.");
        }
    }
}
