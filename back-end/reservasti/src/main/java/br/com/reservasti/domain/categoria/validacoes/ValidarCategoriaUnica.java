package br.com.reservasti.domain.categoria.validacoes;

import br.com.reservasti.domain.categoria.CategoriaRepository;
import br.com.reservasti.domain.categoria.dto.CategoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCategoriaUnica implements IValidatorCategoria{
    @Autowired
    private CategoriaRepository repository;

    @Override
    public void validar(CategoriaValidacaoContext context) {
        if (repository.existsByNome(context.dto().nome())) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome.");
        }
    }
}
