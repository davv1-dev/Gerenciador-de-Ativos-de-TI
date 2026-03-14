import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DepartamentoDTO, DepartamentoRetornoDTO, DepartamentoAtualizacaoDTO } from '../models/departamento';
import { Page } from '../models/chamado';

@Injectable({
  providedIn: 'root'
})
export class DepartamentoService {

  private apiUrl = `${environment.apiUrl}/departamentos`;

  constructor(private http: HttpClient) { }

  listarDepartamentos(page: number = 0, size: number = 10): Observable<Page<DepartamentoRetornoDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<DepartamentoRetornoDTO>>(this.apiUrl, { params });
  }

  buscarPorId(id: number): Observable<DepartamentoRetornoDTO> {
    return this.http.get<DepartamentoRetornoDTO>(`${this.apiUrl}/${id}`);
  }

  cadastrarDepartamento(dto: DepartamentoDTO): Observable<DepartamentoRetornoDTO> {
    return this.http.post<DepartamentoRetornoDTO>(this.apiUrl, dto);
  }

  atualizarDepartamento(id: number, dto: DepartamentoAtualizacaoDTO): Observable<DepartamentoRetornoDTO> {
    return this.http.put<DepartamentoRetornoDTO>(`${this.apiUrl}/${id}`, dto);
  }

  excluirDepartamento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
