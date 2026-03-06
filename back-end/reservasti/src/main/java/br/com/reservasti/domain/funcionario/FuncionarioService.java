package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.departamento.Departamento;
import br.com.reservasti.domain.departamento.DepartamentoRepository;
import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioRetornoDTO;
import br.com.reservasti.domain.funcionario.validacoes.IValidatorFuncionario;
import br.com.reservasti.domain.funcionario.validacoes.ValidarConflitoDeEmail;
import br.com.reservasti.domain.usuario.Usuario;
import br.com.reservasti.domain.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private List<IValidatorFuncionario> validadores;
    @Autowired
    private ValidarConflitoDeEmail validarConflitoDeEmail;


    @Transactional
    public FuncionarioRetornoDTO cadastrarFuncionario(FuncionarioDTO novoFunc){
        validadores.forEach(v -> v.validar(novoFunc));
        Long id = usuarioService.salvarUsuario(novoFunc.email(), novoFunc.cpf());
        Departamento dep = departamentoRepository.findById(novoFunc.departamentoId()).orElseThrow(()-> new RuntimeException("Departamento não encontrado"));
        Funcionario funcionarioNovo = new Funcionario(id,novoFunc,dep);
        funcionarioRepository.save(funcionarioNovo);
        FuncionarioRetornoDTO retorno = new FuncionarioRetornoDTO(funcionarioNovo);
        return retorno;
    }
    public Page<FuncionarioRetornoDTO> buscarFuncionario(Pageable paginacao,String nome,Long departamentoId) {
        Specification<Funcionario> condicoes = FuncionarioEspecification.comFiltrosFuncionario(nome,departamentoId);

        return funcionarioRepository.findAll(condicoes,paginacao)
                .map(FuncionarioRetornoDTO::new);
    }

    public FuncionarioRetornoDTO buscarPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado!"));

        return new FuncionarioRetornoDTO(funcionario);
    }

    @Transactional
    public FuncionarioRetornoDTO atualizarFuncionario(Long id, FuncionarioAtualizacaoDTO dto) {

        validarConflitoDeEmail.validaEmailAtualizacao(dto);
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado!"));

        Departamento departamento = null;
        if (dto.departamentoId() != null) {
            departamento = departamentoRepository.findById(dto.departamentoId()).orElseThrow(() -> new IllegalArgumentException("Departamento não encontrado"));
        }


        funcionario.atualizarInformacoes(dto, departamento);
        return new FuncionarioRetornoDTO(funcionario);
    }

    @Transactional
    public void desativarFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado!"));

        funcionario.inativar();
    }
}

