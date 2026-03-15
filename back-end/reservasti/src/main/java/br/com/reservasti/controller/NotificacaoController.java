package br.com.reservasti.controller;


import br.com.reservasti.domain.notificacao.NotificacaoService;
import br.com.reservasti.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {
    @Autowired
    private NotificacaoService sseService;


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('TECNICO')")
    public SseEmitter conectarSse(@AuthenticationPrincipal Usuario usuarioLogado) {
        Long tecnicoId = usuarioLogado.getId();
        return sseService.conectarTecnico(tecnicoId);
    }
}
