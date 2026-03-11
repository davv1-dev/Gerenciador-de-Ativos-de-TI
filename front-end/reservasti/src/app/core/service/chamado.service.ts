import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page, ResumoChamadoDTO } from '../models/chamado';

@Injectable({
  providedIn: 'root'
})
export class ChamadoService {

  private apiUrl = `${environment.apiUrl}/chamados`;

  // URL base para as notificações (ajuste se seu controller Java for diferente)
  private sseUrlBase = `${environment.apiUrl}/notificacoes`;

  constructor(private http: HttpClient) { }

  listarFilaGlobal(page: number = 0, size: number = 10): Observable<Page<ResumoChamadoDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/fila-global`, { params });
  }

  listarFilaPessoal(tecnicoId: number, page: number = 0, size: number = 10): Observable<Page<ResumoChamadoDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/fila-pessoal/${tecnicoId}`, { params });
  }

  escutarPosicaoFila(solicitanteId: number): Observable<number> {
    return new Observable((subscriber) => {
      const sseUrl = `${this.sseUrlBase}/solicitantes/${solicitanteId}/stream`;
      const eventSource = new EventSource(sseUrl);

      eventSource.addEventListener('ATUALIZACAO_FILA', (event: MessageEvent) => {
        const dados = JSON.parse(event.data);
        subscriber.next(dados.posicaoFila);
      });

      eventSource.onerror = (error) => {
        console.error('Conexão SSE de Solicitante perdida, tentando reconectar...', error);
      };

      return () => eventSource.close();
    });
  }

  escutarFilaGlobalAoVivo(tecnicoId: number): Observable<ResumoChamadoDTO> {
    return new Observable((subscriber) => {

      const sseUrl = `${this.sseUrlBase}/stream/${tecnicoId}`;

      const eventSource = new EventSource(sseUrl);

      eventSource.addEventListener('NOVO_CHAMADO_GLOBAL', (event: MessageEvent) => {
        const novoChamado: ResumoChamadoDTO = JSON.parse(event.data);
        subscriber.next(novoChamado);
      });

      eventSource.onerror = (error) => {
        console.error('Conexão SSE da Fila Global perdida, tentando reconectar...', error);
      };

      return () => eventSource.close();
    });
  }
}
