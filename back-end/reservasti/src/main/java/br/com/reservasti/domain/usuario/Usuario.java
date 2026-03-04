package br.com.reservasti.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario;

    public Usuario(String login,String senha,TipoUsuario tipo){
        this.login=login;
        this.senha= senha;
        this.tipoUsuario=tipo;
    }
}
