package br.com.reservasti.domain.equipamento;

import br.com.reservasti.domain.categoria.Categoria;
import br.com.reservasti.domain.categoria.CategoriaRepository;
import br.com.reservasti.domain.departamento.Departamento;
import br.com.reservasti.domain.departamento.DepartamentoRepository;
import br.com.reservasti.domain.equipamento.dto.*;
import br.com.reservasti.domain.equipamento.validacoes.IValidatorEquipamento;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private List<IValidatorEquipamento> validadores;

    @Transactional
    public EquipamentoRetornoDTO cadastrarEquipamento(EquipamentoDTO dto) {
        validadores.forEach( validator -> validator.validar(dto));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId()).orElseThrow(() -> new IdNaoEncontradoException("Categoria não encontrada!"));
        Equipamento equipamento = new Equipamento(dto, categoria);
        equipamentoRepository.save(equipamento);

        return new EquipamentoRetornoDTO(equipamento);
    }

    public EquipamentoRetornoDTO buscarPorId(Long id) {

        Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado!"));
        return new EquipamentoRetornoDTO(equipamento);
    }

    @Transactional
    public void alterarStatusEquipamento(Long id, StatusEquipamento status) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado!"));

        equipamento.setStatus(status);
        equipamentoRepository.save(equipamento);
    }
    @Transactional
    public void desativarEquipamento(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado!"));

        equipamento.setStatus(StatusEquipamento.BAIXADO);
    }

    public Page<EquipamentoRetornoDTO> bucarEquipamento(String nome, Long categoriaId, Pageable paginacao) {

        Specification<Equipamento> spec = EquipamentoSpecification.comFiltros(nome, categoriaId);

        return equipamentoRepository.findAll(spec, paginacao)
                .map(EquipamentoRetornoDTO::new);
    }
    @Transactional
    public EquipamentoRetornoDTO editarEquipamento(Long id, EditarEquipamentoDTO dto) {

        Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado!"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId()).orElseThrow(() -> new IdNaoEncontradoException("Categoria não encontrada!"));

        equipamento.atualizarInformacoes(dto,categoria);

        return new EquipamentoRetornoDTO(equipamento);
    }
    @Transactional
    public void alocarEquipamentoAoDepartamento(AlocarEquipamentoDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.idEquipamento())
                .orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado"));

        Departamento departamento = departamentoRepository.findById(dto.idDepartamento())
                .orElseThrow(() -> new IdNaoEncontradoException("Departamento não encontrado"));

        equipamento.alocarAoDepartamento(departamento);

    }
    public ResultadoSimulacaoDTO simularExpansao(SimulacaoEquipamentosDTO dto) {
        List<EquipamentoResultadoSimulacaoDTO> detalhes = new ArrayList<>();
        boolean tudoViavel = true;

        for (EquipamentoSimulacaoDTO item : dto.itens()) {
            String nomeCategoria = categoriaRepository.findById(item.categoriaId())
                    .map(Categoria::getNome)
                    .orElse("Categoria Desconhecida");

            long disponivel = equipamentoRepository.countByCategoriaIdAndDepartamentoIsNullAndStatus(
                    item.categoriaId(), StatusEquipamento.DISPONIVEL);

            int faltante = item.quantidadeNecessaria() - (int) disponivel;
            if (faltante < 0) faltante = 0; // Se sobrou, faltante é zero

            boolean itemViavel = disponivel >= item.quantidadeNecessaria();
            if (!itemViavel) {
                tudoViavel = false; // Se um falhar, a expansão geral fica bloqueada (ou com alerta)
            }

            detalhes.add(new EquipamentoResultadoSimulacaoDTO(
                    item.categoriaId(),
                    nomeCategoria,
                    item.quantidadeNecessaria(),
                    (int)disponivel,
                    faltante,
                    itemViavel
            ));
        }

        return new ResultadoSimulacaoDTO(tudoViavel, detalhes);
    }

}