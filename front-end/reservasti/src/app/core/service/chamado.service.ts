import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page, ResumoChamadoDTO,AberturaChamadoDTO,DetalhamentoChamadoDTO} from '../models/chamado';

@Injectable({
  providedIn: 'root'
})
export class ChamadoService {

  private apiUrl = `${environment.apiUrl}/chamados`;

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

  abrirChamado(dto: AberturaChamadoDTO): Observable<DetalhamentoChamadoDTO> {
    return this.http.post<DetalhamentoChamadoDTO>(this.apiUrl, dto);
    }

  escutarFilaGlobalAoVivo(tecnicoId: number): Observable<ResumoChamadoDTO> {
    return new Observable((subscriber) => {
      const sseUrl = `${this.sseUrlBase}/stream/${tecnicoId}`;
      const eventSource = new EventSource(sseUrl);

      eventSource.addEventListener('NOVO_CHAMADO_GLOBAL', (event: MessageEvent) => {
        const novoChamado: ResumoChamadoDTO = JSON.parse(event.data);
        subscriber.next(novoChamado);
      });

      return () => eventSource.close();
    });
  }
  escutarMinhaFilaAoVivo(tecnicoId: number): Observable<ResumoChamadoDTO> {
    return new Observable((subscriber) => {
      const sseUrl = `${this.sseUrlBase}/stream/${tecnicoId}`;
      const eventSource = new EventSource(sseUrl);

      eventSource.addEventListener('NOVO_CHAMADO', (event: MessageEvent) => {
        const novoChamado: ResumoChamadoDTO = JSON.parse(event.data);
        subscriber.next(novoChamado);
      });

      return () => eventSource.close();
    });
  }
  listarMeusChamados(solicitanteId: number): Observable<Page<ResumoChamadoDTO>> {
    return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/solicitante/${solicitanteId}`);
  }

  resolverChamado(id: number): Observable<DetalhamentoChamadoDTO> {
    return this.http.patch<DetalhamentoChamadoDTO>(`${this.apiUrl}/${id}/resolver`, {});
  }

  assumirChamado(idChamado: number, idTecnico: number): Observable<DetalhamentoChamadoDTO> {
    return this.http.patch<DetalhamentoChamadoDTO>(`${this.apiUrl}/${idChamado}/assumir`, idTecnico);
  }

  pingTecnico(tecnicoId: number): Observable<void> {
  return this.http.patch<void>(`${environment.apiUrl}/tecnicos/${tecnicoId}/ping`, {});
  }
  listarHistoricoTecnico(tecnicoId: number, periodo: string, page: number = 0, size: number = 5): Observable<Page<ResumoChamadoDTO>> {
    const params = new HttpParams().set('periodo', periodo).set('page', page.toString()).set('size', size.toString());

    return this.http.get<Page<ResumoChamadoDTO>>(`${this.apiUrl}/historico/${tecnicoId}`, { params });
  }
}
