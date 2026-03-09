package br.com.reservasti.domain.departamento;

import br.com.reservasti.domain.departamento.dto.*;
import br.com.reservasti.domain.departamento.validacoes.DepartamentoValidacaoContext;
import br.com.reservasti.domain.departamento.validacoes.IValidatorDepartamento;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private List<IValidatorDepartamento> validadores;

    @Transactional
    public DepartamentoRetornoDTO cadastrarDepartamento(DepartamentoDTO dto) {
        validadores.forEach(v->v.validar(new DepartamentoValidacaoContext(null,dto)));

        Departamento departamento = new Departamento(dto);
        repository.save(departamento);
        return new DepartamentoRetornoDTO(departamento);
    }

    public Page<DepartamentoRetornoDTO> listarDepartamentos(Pageable paginacao) {
        return repository.findAll(paginacao).map(DepartamentoRetornoDTO::new);
    }

    public DepartamentoRetornoDTO buscarPorIdDepartamento(Long id) {
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Departamento não encontrado."));
        return new DepartamentoRetornoDTO(departamento);
    }

    @Transactional
    public DepartamentoRetornoDTO atualizarDepartamento(Long id, DepartamentoAtualizacaoDTO dto) {
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Departamento não encontrado."));

        departamento.atualizarInformacoes(dto);
        return new DepartamentoRetornoDTO(departamento);
    }

    @Transactional
    public void excluirDepartamento(Long id) {
        validadores.forEach(v->v.validar(new DepartamentoValidacaoContext(id,null)));
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Departamento não encontrado."));

        repository.delete(departamento);
    }
}
