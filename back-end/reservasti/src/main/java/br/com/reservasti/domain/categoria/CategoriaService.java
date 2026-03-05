package br.com.reservasti.domain.categoria;

import br.com.reservasti.domain.categoria.dto.*;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private EquipamentoRepository equipamentoRepository; // Para validar exclusão

    @Transactional
    public CategoriaRetornoDTO cadastrar(CategoriaDTO dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome.");
        }
        Categoria categoria = new Categoria(dto);
        repository.save(categoria);
        return new CategoriaRetornoDTO(categoria);
    }

    public Page<CategoriaRetornoDTO> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(CategoriaRetornoDTO::new);
    }

    public CategoriaRetornoDTO buscarPorId(Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));
        return new CategoriaRetornoDTO(categoria);
    }

    @Transactional
    public CategoriaRetornoDTO atualizar(Long id, CategoriaAtualizacaoDTO dto) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));

        categoria.atualizarInformacoes(dto);
        return new CategoriaRetornoDTO(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));

        // Proteção de Integridade:
        if (equipamentoRepository.existsByCategoriaId(id)) {
            throw new IllegalStateException("Não é possível excluir: existem equipamentos vinculados a esta categoria.");
        }

        repository.delete(categoria);
    }
}
