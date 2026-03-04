package br.com.reservasti.domain.usuario;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private UsuarioRepository repository;

    public Long salvarUsuario(String login,String senha){
        Usuario novoUsu = new Usuario(login,senha,TipoUsuario.ADMIN);
        repository.save(novoUsu);
        return novoUsu.getId();
    }
}
