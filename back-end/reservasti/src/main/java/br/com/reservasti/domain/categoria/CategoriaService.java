package br.com.reservasti.domain.categoria;

import br.com.reservasti.domain.categoria.dto.*;
import br.com.reservasti.domain.categoria.validacoes.CategoriaValidacaoContext;
import br.com.reservasti.domain.categoria.validacoes.IValidatorCategoria;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;
    @Autowired
    private List<IValidatorCategoria> validadores;

    @Transactional
    public CategoriaRetornoDTO cadastrar(CategoriaDTO dto) {
        validadores.forEach(v->v.validar(new CategoriaValidacaoContext(null,dto)));

        Categoria categoria = new Categoria(dto);

        repository.save(categoria);
        System.out.println(categoria.getId());
        return new CategoriaRetornoDTO(categoria);

    }

    public Page<CategoriaRetornoDTO> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(CategoriaRetornoDTO::new);
    }

    public CategoriaRetornoDTO buscarPorId(Long id) {
        validadores.forEach(v->v.validar(new CategoriaValidacaoContext(id,null)));

        Categoria categoria = repository.findById(id).get();
        return new CategoriaRetornoDTO(categoria);
    }

    @Transactional
    public CategoriaRetornoDTO atualizar(Long id, CategoriaAtualizacaoDTO dto) {
        validadores.forEach(v->v.validar(new CategoriaValidacaoContext(id,null)));
        Categoria categoria = repository.findById(id).get();

        categoria.atualizarInformacoes(dto);
        return new CategoriaRetornoDTO(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        validadores.forEach(v->v.validar(new CategoriaValidacaoContext(id,null)));
        Categoria categoria = repository.findById(id).get();

        repository.delete(categoria);
    }
}
