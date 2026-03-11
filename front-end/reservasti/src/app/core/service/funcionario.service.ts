import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { FuncionarioDTO } from '../models/funcionario';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {

  private apiUrl = `${environment.apiUrl}/funcionarios`;

  constructor(private http: HttpClient) { }

  cadastrar(dto: FuncionarioDTO): Observable<any> {
    return this.http.post<any>(this.apiUrl, dto);
  }
}
