package br.com.reservasti.controller;


import br.com.reservasti.domain.notificacao.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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


    @GetMapping(value = "/stream/{tecnicoId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter conectarSse(@PathVariable Long tecnicoId) {
        return sseService.conectarTecnico(tecnicoId);
    }
}
