import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import {
  PrevisaoDemandaDTO,
  RelatorioAtrasoDTO,
  RelatorioDeFalhaPorMarcaDTO,
  RelatorioDepartamentoDTO,
  RelatorioGeralDTO,
  RelatorioInativosDTO
} from '../models/relatorio';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {
  private apiUrl = `${environment.apiUrl}/relatorios`;

  constructor(private http: HttpClient) {}

  gerarResumoPorDepartamento(id: number): Observable<RelatorioDepartamentoDTO> {
    return this.http.get<RelatorioDepartamentoDTO>(`${this.apiUrl}/departamentos/${id}`);
  }

  gerarRelatorioGeral(): Observable<RelatorioGeralDTO> {
    return this.http.get<RelatorioGeralDTO>(`${this.apiUrl}/geral`);
  }

  relatorioAtrasos(): Observable<RelatorioAtrasoDTO[]> {
    return this.http.get<RelatorioAtrasoDTO[]>(`${this.apiUrl}/atrasos`);
  }

  relatorioFalhas(): Observable<RelatorioDeFalhaPorMarcaDTO[]> {
    return this.http.get<RelatorioDeFalhaPorMarcaDTO[]>(`${this.apiUrl}/falhas`);
  }

  previsaoDemanda(inicio: string, fim: string): Observable<PrevisaoDemandaDTO[]> {
    let params = new HttpParams().set('inicio', inicio).set('fim', fim);
    return this.http.get<PrevisaoDemandaDTO[]>(`${this.apiUrl}/demanda`, { params });
  }

  relatorioOciosidade(dias: number = 90): Observable<RelatorioInativosDTO[]> {
    let params = new HttpParams().set('dias', dias.toString());
    return this.http.get<RelatorioInativosDTO[]>(`${this.apiUrl}/inativos`, { params });
  }
  relatorioGarantiasVencendo(pagina: number = 0, tamanho: number = 10): Observable<any> {
    let params = new HttpParams()
      .set('page', pagina.toString())
      .set('size', tamanho.toString());

    return this.http.get<any>(`${this.apiUrl}/garantias-vencendo`, { params });
  }
}
