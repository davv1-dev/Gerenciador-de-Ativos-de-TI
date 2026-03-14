import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { FuncionarioRetornoDTO } from '../models/funcionario';
import { Page } from '../models/chamado';

@Injectable({
  providedIn: 'root'
})
export class TecnicoService {

  private apiUrl = `${environment.apiUrl}/tecnicos`;

  constructor(private http: HttpClient) { }

  listarTecnicosOnline(): Observable<Page<FuncionarioRetornoDTO>> {
    return this.http.get<Page<FuncionarioRetornoDTO>>(`${this.apiUrl}`);
  }
}
