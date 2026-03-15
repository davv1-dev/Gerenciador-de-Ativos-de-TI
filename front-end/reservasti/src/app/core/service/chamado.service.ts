import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page, ResumoChamadoDTO, AberturaChamadoDTO, DetalhamentoChamadoDTO } from '../models/chamado';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ChamadoService {

  private apiUrl = `${environment.apiUrl}/chamados`;
  private sseUrlBase = `${environment.apiUrl}/notificacoes`;

  constructor(private http: HttpClient,
      private router:Router
  ) { }

  listarFilaGlobal(page: number = 0, size: number = 10): Observable<Page<ResumoChamadoDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/fila-global`, { params });
  }

  // 👇 CORREÇÃO 1: No Back-end, a rota é `/fila-pessoal/{tecnicoId}`, mas o back ignora o PathVariable
  // e usa o @AuthenticationPrincipal. Manteremos a URL como está no Controller, mas entenda que
  // o Back-end vai usar o token de quem fez a requisição.
  listarFilaPessoal(page: number = 0, size: number = 10): Observable<Page<ResumoChamadoDTO>> {
  const params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

  // 👇 Agora a requisição vai limpa, sem ID na URL. O Token faz o resto!
  return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/fila-pessoal`, { params });
}

  escutarPosicaoFila(solicitanteId: number): Observable<number> {
    return new Observable((subscriber) => {
      // 👇 Pegando o token
      const token = sessionStorage.getItem('token');

      // 👇 Passando o token na URL para o Solicitante também!
      const sseUrl = `${this.sseUrlBase}/solicitantes/${solicitanteId}/stream?token=${token}`;

      const eventSource = new EventSource(sseUrl);

      eventSource.addEventListener('ATUALIZACAO_FILA', (event: MessageEvent) => {
        const dados = JSON.parse(event.data);
        subscriber.next(dados.posicaoFila);
      });

      eventSource.onerror = (error) => {
        console.error('Conexão SSE de Solicitante perdida, tentando reconectar...', error);
        eventSource.close();
      };

      return () => eventSource.close();
    });
  }

  abrirChamado(dto: AberturaChamadoDTO): Observable<DetalhamentoChamadoDTO> {
    return this.http.post<DetalhamentoChamadoDTO>(this.apiUrl, dto);
  }

  escutarFilaGlobalAoVivo(): Observable<ResumoChamadoDTO> {
    return new Observable((subscriber) => {
      const token = sessionStorage.getItem('token');
      const sseUrl = `${this.sseUrlBase}/stream?token=${token}`;
      const eventSource = new EventSource(sseUrl);

      // 1. Escuta se o Java enviar com o nome 'NOVO_CHAMADO'
      eventSource.addEventListener('NOVO_CHAMADO', (event: MessageEvent) => {
        console.log('SERVICE: Chegou evento com nome NOVO_CHAMADO:', event.data);
        const novoChamado: ResumoChamadoDTO = JSON.parse(event.data);
        subscriber.next(novoChamado);
      });

      // 2. Escuta se o Java enviar um evento genérico (sem nome específico)
      eventSource.onmessage = (event: MessageEvent) => {
        const novoChamado: ResumoChamadoDTO = JSON.parse(event.data);
        subscriber.next(novoChamado);
      };

      eventSource.onerror = (error) => {
        console.error('Falha na conexão SSE:', error);
        eventSource.close();
      };

      eventSource.onerror = (error) => {
        console.error('Falha na conexão SSE. O token pode ter expirado.', error);
        eventSource.close();

        // 👇 NOVO: Se o SSE morrer de vez, também desloga o usuário por segurança
        sessionStorage.clear();
        this.router.navigate(['/login']);
      };

      return () => eventSource.close();
    });
  }

  escutarMinhaFilaAoVivo(): Observable<ResumoChamadoDTO> {
    return new Observable((subscriber) => {
      // 👇 Pegando o token
      const token = sessionStorage.getItem('token');

      // 👇 Passando o token na URL igual na Fila Global
      const sseUrl = `${this.sseUrlBase}/stream?token=${token}`;

      const eventSource = new EventSource(sseUrl);

      eventSource.addEventListener('NOVO_CHAMADO', (event: MessageEvent) => {
        const novoChamado: ResumoChamadoDTO = JSON.parse(event.data);
        subscriber.next(novoChamado);
      });

          eventSource.onerror = (error) => {
      console.error('Falha na conexão SSE. Token pode ter expirado.', error);
      eventSource.close();

      // Como o interceptor não pega o SSE, a gente força o logout aqui!
      sessionStorage.clear();
      this.router.navigate(['/login']);
    };

      return () => eventSource.close();
    });
  }

  // 👇 CORREÇÃO 2: A rota no Back-end é GET `/meus-chamados` e não usa {solicitanteId} na URL,
  // pois usa @AuthenticationPrincipal para pegar do token.
  listarMeusChamados(): Observable<Page<ResumoChamadoDTO>> {
    // Note que removi o solicitanteId daqui.
    return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/meus-chamados`);
  }

  resolverChamado(id: number): Observable<DetalhamentoChamadoDTO> {
    return this.http.patch<DetalhamentoChamadoDTO>(`${this.apiUrl}/${id}/resolver`, {});
  }

  // 👇 CORREÇÃO 3: No Back-end, a rota é PATCH `/{idChamado}/assumir`.
  // O @AuthenticationPrincipal no Controller pega o idTecnico.
  // Portanto, NÃO precisamos enviar o idTecnico no body. Enviar {} (body vazio) é o correto.
  assumirChamado(idChamado: number): Observable<DetalhamentoChamadoDTO> {
    return this.http.patch<DetalhamentoChamadoDTO>(`${this.apiUrl}/${idChamado}/assumir`, {});
  }

 pingTecnico(): Observable<void> {
    return this.http.patch<void>(`${environment.apiUrl}/tecnicos/ping`, {});
  }
  // 👇 CORREÇÃO 4: A rota no Back-end é `/historico/{tecnicoId}`.
  listarHistoricoTecnico(periodo: string, page: number = 0, size: number = 5): Observable<Page<ResumoChamadoDTO>> {
    const params = new HttpParams()
      .set('periodo', periodo)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/historico`, { params });
  }
}
