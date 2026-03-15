package br.com.reservasti.controller;

import br.com.reservasti.domain.funcionario.FuncionarioService;
import br.com.reservasti.domain.funcionario.dto.FuncionarioRetornoDTO;
import br.com.reservasti.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    @Autowired
    private FuncionarioService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN','COMUM')")
    public ResponseEntity<Page<FuncionarioRetornoDTO>>listarTecnicos(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable page){
        Page<FuncionarioRetornoDTO> tecnicosOnline = service.listarTecnicosOnline(page);

        return ResponseEntity.ok(tecnicosOnline);
    }

    @PatchMapping("/ping")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Void> ping(@AuthenticationPrincipal Usuario usuarioLogado) {
        service.registrarPing(usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }
}
