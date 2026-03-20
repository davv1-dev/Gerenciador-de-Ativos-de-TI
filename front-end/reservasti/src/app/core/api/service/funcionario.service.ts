import { Injectable } from '@angular/core';
import { HttpClient,HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { FuncionarioDTO,FuncionarioRetornoDTO,FuncionarioAtualizacaoDTO } from '../models/funcionario';
import { Page } from '../models/chamado';


@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {

  private apiUrl = `${environment.apiUrl}/funcionarios`;

  constructor(private http: HttpClient) { }

  cadastrar(dto: FuncionarioDTO): Observable<any> {
    return this.http.post<any>(this.apiUrl, dto);
  }
  listar(pagina: number = 0, nome?: string, departamentoId?: number): Observable<Page<FuncionarioRetornoDTO>> {
    let params = new HttpParams().set('page', pagina.toString())


    if (nome) params = params.set('nome', nome);

    if (departamentoId) params = params.set('departamentoid', departamentoId.toString());

    return this.http.get<Page<FuncionarioRetornoDTO>>(this.apiUrl, { params });
  }

  detalhar(id: number): Observable<FuncionarioRetornoDTO> {
    return this.http.get<FuncionarioRetornoDTO>(`${this.apiUrl}/${id}`);
  }

  atualizar(id: number, dto: FuncionarioAtualizacaoDTO): Observable<FuncionarioRetornoDTO> {
    return this.http.put<FuncionarioRetornoDTO>(`${this.apiUrl}/${id}`, dto);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  listarPendentes(pagina: number = 0): Observable<Page<FuncionarioRetornoDTO>> {
    const params = new HttpParams().set('page', pagina.toString());
    return this.http.get<Page<FuncionarioRetornoDTO>>(`${this.apiUrl}/pendentes`, { params });
  }

  listarHistorico(pagina: number = 0): Observable<Page<FuncionarioRetornoDTO>> {
    const params = new HttpParams().set('page', pagina.toString());
    return this.http.get<Page<FuncionarioRetornoDTO>>(`${this.apiUrl}/solicitacoes/historico`, { params });
  }

  aprovarAcesso(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/aprovar`, {});
  }

  negarAcesso(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/negar`, {});
  }
}
