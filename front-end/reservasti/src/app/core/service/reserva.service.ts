import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReservaRetornoDTO } from '../models/reserva';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private apiUrl = `${environment.apiUrl}/reservas`;

  constructor(private http: HttpClient) { }

  listarMinhasReservasAtivas(funcionarioId: number): Observable<ReservaRetornoDTO[]> {
    return this.http.get<ReservaRetornoDTO[]>(`${this.apiUrl}/funcionario/${funcionarioId}/ativas`);
  }
}
