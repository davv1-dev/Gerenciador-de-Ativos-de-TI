package br.com.reservasti.domain.equipamento.dto;

import br.com.reservasti.domain.auth.RefreshTokenDTO;
import br.com.reservasti.domain.usuario.UsuarioDTOEntrada;
import br.com.reservasti.domain.usuario.UsuarioService;
import br.com.reservasti.infra.exceptions.ErroAoGerarTokenException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservasrti")
public class UsuarioController {
    @Autowired
    private UsuarioService service;
    @Autowired
    private AuthenticationManager manager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UsuarioDTOEntrada usuario) throws ErroAoGerarTokenException {
        var token = new UsernamePasswordAuthenticationToken(usuario.login(),usuario.senha());
        var authentication = manager.authenticate(token);
        var retornoTokens = service.efetuarLogin(usuario,authentication);
        return ResponseEntity.ok().body(retornoTokens);
    }
    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody @Valid RefreshTokenDTO refreshDTO) throws ErroAoGerarTokenException {
        var retornoTokens = service.gerarNovoToken(refreshDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(retornoTokens);
    }
}
