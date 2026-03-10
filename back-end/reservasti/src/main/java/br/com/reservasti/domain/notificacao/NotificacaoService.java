package br.com.reservasti.domain.notificacao;

import br.com.reservasti.domain.helpdesk.dto.DetalhamentoChamadoDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificacaoService {

    private final Map<Long, SseEmitter> emissoresTecnicos = new ConcurrentHashMap<>();

    public SseEmitter conectarTecnico(Long tecnicoId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        emissoresTecnicos.put(tecnicoId, emitter);

        emitter.onCompletion(() -> emissoresTecnicos.remove(tecnicoId));
        emitter.onTimeout(() -> emissoresTecnicos.remove(tecnicoId));
        emitter.onError((e) -> emissoresTecnicos.remove(tecnicoId));

        return emitter;
    }

    public void notificarTecnico(Long tecnicoId, DetalhamentoChamadoDTO dto) {
        SseEmitter emitter = emissoresTecnicos.get(tecnicoId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("NOVO_CHAMADO")
                        .data(dto, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emissoresTecnicos.remove(tecnicoId);
            }
        }
    }
    public void notificarTodosTecnicos(DetalhamentoChamadoDTO dto) {

        emissoresTecnicos.forEach((tecnicoId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("NOVO_CHAMADO_GLOBAL")
                        .data(dto, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emissoresTecnicos.remove(tecnicoId);
            }
        });
    }
}
