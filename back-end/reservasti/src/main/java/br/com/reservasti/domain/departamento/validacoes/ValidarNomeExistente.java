package br.com.reservasti.domain.departamento.validacoes;

import br.com.reservasti.domain.departamento.DepartamentoRepository;
import br.com.reservasti.domain.departamento.dto.DepartamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarNomeExistente implements IValidatorDepartamento{
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public void validar(DepartamentoDTO dto) {
        if (departamentoRepository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Nome de departamento já cadastrado.");
        }
    }
}
