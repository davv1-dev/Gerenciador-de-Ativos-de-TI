package br.com.reservasti.infra.security;

import br.com.reservasti.domain.usuario.Usuario;
import br.com.reservasti.infra.exceptions.TokenJWTException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(@NotNull Usuario usuario) throws TokenJWTException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("ApiReservasti")
                    .withSubject(usuario.getLogin())
                    .withClaim("id: ",usuario.getId())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new TokenJWTException("Erro ao gerar TokenJWT");
        }
    }
    public String getSubject(String tokenJWT) throws TokenJWTException {
        try {
            Algorithm algoritimo = Algorithm.HMAC256(secret);
            return JWT.require(algoritimo)
                    .withIssuer("ApiReservasti")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new TokenJWTException("TokenJWT invalido ou expirado");
        }
    }
    public String gerarRefreshToken() {
        return UUID.randomUUID().toString();
    }

    private Instant dataExpiracao() {
        return Instant.now().plus(15, ChronoUnit.MINUTES);
    }
}