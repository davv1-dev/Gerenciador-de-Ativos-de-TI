import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../models/chamado';
import { EquipamentoRetornoDTO } from '../models/equipamento';
import { CategoriariaRetornoDTO } from '../models/categoria';

@Injectable({ providedIn: 'root' })
export class EquipamentoService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  listarCatalogo(nome?: string, categoriaId?: number): Observable<Page<EquipamentoRetornoDTO>> {
    let params = new HttpParams();

    if (nome) params = params.append('nome', nome);
    if (categoriaId) params = params.append('categoriaId', categoriaId.toString());

    return this.http.get<Page<EquipamentoRetornoDTO>>(`${this.apiUrl}/equipamentos`,{ params });
  }

  listarCategorias(): Observable<Page<CategoriariaRetornoDTO>> {
    return this.http.get<Page<CategoriariaRetornoDTO>>(`${this.apiUrl}/categorias?size=10`);
  }
}
