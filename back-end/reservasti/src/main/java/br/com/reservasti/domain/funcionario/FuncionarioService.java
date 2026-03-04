package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.departamento.Departamento;
import br.com.reservasti.domain.departamento.DepartamentoRepository;
import br.com.reservasti.domain.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public FuncionarioRetornoDTO cadastrarFuncionario(FuncionarioDTO novoFunc){
        Long id = usuarioService.salvarUsuario(novoFunc.email(), novoFunc.cpf());
        Departamento dep = departamentoRepository.findById(novoFunc.departamentoId()).orElseThrow(()-> new RuntimeException("Departamento não encontrado"));
        Funcionario funcionarioNovo = new Funcionario(id,novoFunc,dep);
        funcionarioRepository.save(funcionarioNovo);
        FuncionarioRetornoDTO retorno = new FuncionarioRetornoDTO(funcionarioNovo.getNome());
        return retorno;
    }

}
