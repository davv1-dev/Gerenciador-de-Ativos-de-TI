package br.com.reservasti.domain.equipamento;

import br.com.reservasti.domain.categoria.Categoria;
import br.com.reservasti.domain.categoria.CategoriaRepository;
import br.com.reservasti.domain.equipamento.dto.EditarEquipamentoDTO;
import br.com.reservasti.domain.equipamento.dto.EquipamentoDTO;
import br.com.reservasti.domain.equipamento.dto.EquipamentoReturnDTO;
import br.com.reservasti.domain.equipamento.validacoes.ValidatorEquipamento;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private List<ValidatorEquipamento> validadores;

    @Transactional
    public EquipamentoReturnDTO cadastrarEquipamento(EquipamentoDTO dto) {
        validadores.forEach( validator -> validator.validar(dto));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId()).orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada!"));
        Equipamento equipamento = new Equipamento(dto, categoria);
        equipamentoRepository.save(equipamento);

        return new EquipamentoReturnDTO(equipamento);
    }

    public Page<EquipamentoReturnDTO> listarTodosEquipamentos(Pageable paginacao) {

        return equipamentoRepository.findAll(paginacao)
                .map(EquipamentoReturnDTO::new);
    }

    public EquipamentoReturnDTO buscarPorId(Long id) {

        Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado!"));
        return new EquipamentoReturnDTO(equipamento);
    }

    @Transactional
    public void alterarStatusEquipamento(Long id, StatusEquipamento novoStatus) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado!"));

        equipamento.setStatus(novoStatus);
    }
    @Transactional
    public void desativarEquipamento(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado!"));

        equipamento.setStatus(StatusEquipamento.BAIXADO);
    }

    public Page<EquipamentoReturnDTO> bucarEquipamento(String nome, Long categoriaId, Pageable paginacao) {

        Specification<Equipamento> spec = EquipamentoSpecification.comFiltros(nome, categoriaId);

        return equipamentoRepository.findAll(spec, paginacao)
                .map(EquipamentoReturnDTO::new);
    }
    @Transactional
    public EquipamentoReturnDTO editarEquipamento(Long id, EditarEquipamentoDTO dto) {

        Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado!"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId()).orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada!"));

        equipamento.atualizarInformacoes(dto,categoria);

        return new EquipamentoReturnDTO(equipamento);
    }

}