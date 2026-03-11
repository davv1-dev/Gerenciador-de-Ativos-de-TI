import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface DepartamentoDTO {
  id: number;
  nome: string;
}

@Injectable({
  providedIn: 'root'
})
export class DepartamentoService {

  private apiUrl = `${environment.apiUrl}/departamentos`;

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<DepartamentoDTO[]> {
    return this.http.get<DepartamentoDTO[]>(this.apiUrl);
  }
}
