import { Injectable } from '@angular/core';
import { HttpClient,HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { FuncionarioRetornoDTO } from '../models/funcionario';
import { Page } from '../models/chamado';

@Injectable({
  providedIn: 'root'
})
export class TecnicoService {

  constructor(private http: HttpClient) { }

  listarTecnicosOnline(page: number = 0, size: number = 10): Observable<Page<FuncionarioRetornoDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<FuncionarioRetornoDTO>>(`${environment.apiUrl}/tecnicos`, { params });
  }
}
