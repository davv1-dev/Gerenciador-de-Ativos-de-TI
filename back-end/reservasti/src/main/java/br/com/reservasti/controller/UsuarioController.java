package br.com.reservasti.controller;

import br.com.reservasti.domain.auth.RefreshTokenDTO;
import br.com.reservasti.domain.usuario.AlterarSenhaDTO;
import br.com.reservasti.domain.usuario.Usuario;
import br.com.reservasti.domain.usuario.UsuarioDTOEntrada;
import br.com.reservasti.domain.usuario.UsuarioService;
import br.com.reservasti.infra.exceptions.TokenJWTException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservasti")
public class UsuarioController {
    @Autowired
    private UsuarioService service;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UsuarioDTOEntrada usuario) throws TokenJWTException {
        var token = new UsernamePasswordAuthenticationToken(usuario.login(),usuario.senha());
        var authentication = manager.authenticate(token);
        var retornoTokens = service.efetuarLogin(usuario,authentication);
        return ResponseEntity.ok().body(retornoTokens);
    }
    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody @Valid RefreshTokenDTO refreshDTO) throws TokenJWTException {
        var retornoTokens = service.gerarNovoToken(refreshDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(retornoTokens);
    }
    @PostMapping("/logout")
    public ResponseEntity logout(Authentication authentication){
        service.fazerLogout(authentication);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/alterar-senha")
    public ResponseEntity alterarSenha(@RequestBody @Valid AlterarSenhaDTO dados, @AuthenticationPrincipal Usuario usuarioLogado){
        service.alterarSenha(dados, usuarioLogado.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Senha alterada com sucesso");
    }
}
