import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../models/chamado';
import { EquipamentoRetornoDTO,EquipamentoDTO,EditarEquipamentoDTO,AlocarEquipamentoDTO,SimulacaoExpansaoDTO,ResultadoSimulacaoDTO } from '../models/equipamento';
import { CategoriariaRetornoDTO } from '../models/categoria';

@Injectable({ providedIn: 'root' })
export class EquipamentoService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}
  cadastrarEquipamento(dto: EquipamentoDTO): Observable<EquipamentoRetornoDTO> {
    return this.http.post<EquipamentoRetornoDTO>(this.apiUrl, dto);
  }
  listarCatalogo(nome?: string, categoriaId?: number): Observable<Page<EquipamentoRetornoDTO>> {
    let params = new HttpParams();

    if (nome) params = params.append('nome', nome);
    if (categoriaId) params = params.append('categoriaId', categoriaId.toString());

    return this.http.get<Page<EquipamentoRetornoDTO>>(`${this.apiUrl}/equipamentos`,{ params });
  }
  editarEquipamento(id: number, dto: EditarEquipamentoDTO): Observable<EquipamentoRetornoDTO> {
    return this.http.put<EquipamentoRetornoDTO>(`${this.apiUrl}/${id}`, dto);
  }
  listarCategorias(): Observable<Page<CategoriariaRetornoDTO>> {
    return this.http.get<Page<CategoriariaRetornoDTO>>(`${this.apiUrl}/categorias?size=10`);
  }
  desativarEquipamento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  alocarAoDepartamento(dto: AlocarEquipamentoDTO): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/alocar-departamento`, dto);
  }
  simularExpansao(dto: SimulacaoExpansaoDTO): Observable<ResultadoSimulacaoDTO> {
    return this.http.post<ResultadoSimulacaoDTO>(`${this.apiUrl}/equipamentos/simular-expansao`, dto);
  }
}
