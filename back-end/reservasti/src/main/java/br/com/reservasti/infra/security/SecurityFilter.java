package br.com.reservasti.infra.security;

import br.com.reservasti.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository repository;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (uri.contains("/login") || uri.contains("/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        var tokenJWT = retornarToken(request);
        if(tokenJWT !=null){
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByloginIgnoreCase(subject).get();
            var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }
    private String retornarToken(HttpServletRequest request) {
        // 1. Tenta pegar do Header padrão
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        // 2. Se não achar no Header, tenta pegar da URL (Para o SSE funcionar!)
        var tokenNaUrl = request.getParameter("token");
        if (tokenNaUrl != null) {
            return tokenNaUrl;
        }

        return null;
    }
}
