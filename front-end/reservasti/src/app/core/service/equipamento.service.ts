import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../models/chamado';
import { EquipamentoRetornoDTO, EquipamentoDTO, EditarEquipamentoDTO, AlocarEquipamentoDTO, SimulacaoEquipamentosDTO, ResultadoSimulacaoDTO,AlterarStatusDTO } from '../models/equipamento';
import { CategoriaDTO, CategoriariaRetornoDTO } from '../models/categoria';

@Injectable({ providedIn: 'root' })
export class EquipamentoService {

  private apiUrl = `${environment.apiUrl}/equipamentos`;
  private categoriasUrl = `${environment.apiUrl}/categorias`;

  constructor(private http: HttpClient) {}

  cadastrarEquipamento(dto: EquipamentoDTO): Observable<EquipamentoRetornoDTO> {
    return this.http.post<EquipamentoRetornoDTO>(this.apiUrl, dto);
  }

  listarCatalogo(nome?: string, categoriaId?: number, page: number = 0, size: number = 10): Observable<Page<EquipamentoRetornoDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (nome) params = params.append('nome', nome);
    if (categoriaId) params = params.append('categoriaId', categoriaId.toString());

    return this.http.get<Page<EquipamentoRetornoDTO>>(this.apiUrl, { params });
  }

  editarEquipamento(id: number, dto: EditarEquipamentoDTO): Observable<EquipamentoRetornoDTO> {
    return this.http.put<EquipamentoRetornoDTO>(`${this.apiUrl}/${id}`, dto);
  }

  listarCategorias(): Observable<Page<CategoriariaRetornoDTO>> {
    return this.http.get<Page<CategoriariaRetornoDTO>>(`${this.categoriasUrl}?size=50`);
  }

  desativarEquipamento(id: number, quantidade?: number): Observable<void> {
    let params = new HttpParams();

    if (quantidade && quantidade > 0) {
      params = params.append('quantidade', quantidade.toString());
    }

    return this.http.delete<void>(`${this.apiUrl}/${id}`, { params });
  }

  alocarAoDepartamento(dto: AlocarEquipamentoDTO): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/alocar-departamento`, dto);
  }

  simularExpansao(dto: SimulacaoEquipamentosDTO): Observable<ResultadoSimulacaoDTO> {
    return this.http.post<ResultadoSimulacaoDTO>(`${this.apiUrl}/simular-expansao`, dto);
  }
  cadastrarCategoria(dto: CategoriaDTO): Observable<CategoriaDTO> {
    return this.http.post<CategoriaDTO>(this.categoriasUrl, dto);
  }
  alterarStatus(id: number, dto: AlterarStatusDTO): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/status`, dto);
  }
}
