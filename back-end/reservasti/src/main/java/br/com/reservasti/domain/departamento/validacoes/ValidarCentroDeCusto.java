package br.com.reservasti.domain.departamento.validacoes;

import br.com.reservasti.domain.departamento.DepartamentoRepository;
import br.com.reservasti.infra.exceptions.ConcorrenciaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCentroDeCusto implements IValidatorDepartamento{
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public void validar(DepartamentoValidacaoContext context) {
        if (departamentoRepository.existsByCentroDeCusto(context.dto().centroDeCusto())) {
            throw new ConcorrenciaException("Centro de custo já cadastrado.");
        }
    }
}
