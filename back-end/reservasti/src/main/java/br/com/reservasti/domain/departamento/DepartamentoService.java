package br.com.reservasti.domain.departamento;

import br.com.reservasti.domain.departamento.dto.*;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    @Autowired
    private FuncionarioRepository funcionarioRepository; // Para validar exclusão

    @Transactional
    public DepartamentoRetornoDTO cadastrar(DepartamentoDTO dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Nome de departamento já cadastrado.");
        }
        if (repository.existsByCentroDeCusto(dto.centroDeCusto())) {
            throw new IllegalArgumentException("Centro de custo já cadastrado.");
        }

        Departamento departamento = new Departamento(dto);
        repository.save(departamento);
        return new DepartamentoRetornoDTO(departamento);
    }

    public Page<DepartamentoRetornoDTO> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(DepartamentoRetornoDTO::new);
    }

    public DepartamentoRetornoDTO buscarPorId(Long id) {
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));
        return new DepartamentoRetornoDTO(departamento);
    }

    @Transactional
    public DepartamentoRetornoDTO atualizar(Long id, DepartamentoAtualizacaoDTO dto) {
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));

        departamento.atualizarInformacoes(dto);
        return new DepartamentoRetornoDTO(departamento);
    }

    @Transactional
    public void excluir(Long id) {
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));

        // Proteção de Integridade:
        if (funcionarioRepository.existsByDepartamentoId(id)) {
            throw new IllegalStateException("Não é possível excluir: existem funcionários neste departamento.");
        }

        repository.delete(departamento);
    }
}
