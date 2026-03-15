package br.com.reservasti.domain.categoria.validacoes;

import br.com.reservasti.domain.categoria.CategoriaRepository;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarExistenciaCategoria implements IValidatorCategoria{
    @Autowired
    private CategoriaRepository repository;
    @Override
    public void validar(CategoriaValidacaoContext context) {
        if (context.id() != null) {
            repository.findById(context.id()).orElseThrow(()-> new IdNaoEncontradoException("Categoria não encontrada"));
        }

    }
}
