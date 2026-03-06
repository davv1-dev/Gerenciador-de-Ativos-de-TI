package br.com.reservasti.domain.categoria.validacoes;

import br.com.reservasti.domain.categoria.CategoriaRepository;
import br.com.reservasti.infra.IdNaoEncontradoExeception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarExistenciaCategoria implements IValidatorCategoria{
    @Autowired
    private CategoriaRepository repository;
    @Override
    public void validar(CategoriaValidacaoContext context) {
        repository.findById(context.id()).orElseThrow(()-> new IdNaoEncontradoExeception("Categoria não encontrada"));
    }
}
