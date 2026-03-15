package br.com.reservasti.domain.usuario;

import br.com.reservasti.domain.auth.RefreshToken;
import br.com.reservasti.domain.auth.RefreshTokenDTO;
import br.com.reservasti.domain.auth.RefreshTokenRepository;
import br.com.reservasti.infra.exceptions.AlteracaoDeSenhaException;
import br.com.reservasti.infra.exceptions.TokenJWTException;
import br.com.reservasti.infra.exceptions.RefreshTokenException;
import br.com.reservasti.infra.security.TokenResponseDTO;
import br.com.reservasti.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails usuario = repository.findByLogin(username);
        return usuario;

    }
    @Transactional
    public Long salvarUsuario(String login,String senha){
        var senhaCriptografada = passwordEncoder.encode(senha);
        Usuario novoUsu = new Usuario(login,senhaCriptografada,TipoUsuario.COMUM);
        repository.save(novoUsu);
        return novoUsu.getId();
    }
    @Transactional
    public TokenResponseDTO efetuarLogin(@Valid UsuarioDTOEntrada dados, Authentication authentication) throws TokenJWTException {
        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

        refreshTokenRepository.deleteByUsuario(usuarioAutenticado);

        var tokenJWT = tokenService.gerarToken(usuarioAutenticado);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenService.gerarRefreshToken());
        refreshToken.setUsuario(usuarioAutenticado);
        refreshToken.setDataExpiracao(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshToken);
        return new TokenResponseDTO(tokenJWT,refreshToken.getToken(),usuarioAutenticado.getTipoUsuario());
    }
    @Transactional
    public void fazerLogout(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        refreshTokenRepository.deleteByUsuario(usuario);
    }
    @Transactional
    public void alterarSenha(AlterarSenhaDTO dados,Long id){
        Usuario usuario = repository.getReferenceById(id);
        var senhaAtualCriptografada = passwordEncoder.encode(dados.senhaAtual());
        if(!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())){
            throw new AlteracaoDeSenhaException("A senha atual não é essa");
        }
        if(!dados.novaSenha().equals(dados.novaSenhaConfirmacao())){
            throw new AlteracaoDeSenhaException("A senha digitada deve ser identica");
        }
        var novaSenhaCriptografada= passwordEncoder.encode(dados.novaSenha());
        usuario.alterarSenha(novaSenhaCriptografada);
    }
    @Transactional
    public TokenResponseDTO gerarNovoToken(@Valid RefreshTokenDTO refreshDTO) throws TokenJWTException {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshDTO.refreshtoken()).orElseThrow(()-> new RefreshTokenException("O token Informado não Existe."));
        if(refreshToken.getDataExpiracao().isBefore(Instant.now())){
            throw new RefreshTokenException("RefreshToken expirado");
        }
        Usuario usuario = refreshToken.getUsuario();
        var novoTokenJWT = tokenService.gerarToken(usuario);
        return new TokenResponseDTO(novoTokenJWT, refreshToken.getToken(),null);
    }
}


