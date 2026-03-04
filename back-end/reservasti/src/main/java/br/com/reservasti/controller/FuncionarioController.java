package br.com.reservasti.controller;

import br.com.reservasti.domain.funcionario.FuncionarioDTO;
import br.com.reservasti.domain.funcionario.FuncionarioRetornoDTO;
import br.com.reservasti.domain.funcionario.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    private FuncionarioService service;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarFuncionario(@RequestBody @Valid FuncionarioDTO novoFunc){
        FuncionarioRetornoDTO response = service.cadastrarFuncionario(novoFunc);
        return ResponseEntity.status(HttpStatus.CREATED).body("Funcionario: "+response.nome()+" foi cadastrado com sucesso.");
    }
}
