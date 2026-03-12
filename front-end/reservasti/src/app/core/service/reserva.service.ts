import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReservaRetornoDTO,ReservaDTO } from '../models/reserva';
import { Page } from '../models/chamado';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private apiUrl = `${environment.apiUrl}/reservas`;

  constructor(private http: HttpClient) { }

  listarMinhasReservasAtivas(funcionarioId: number): Observable<Page<ReservaRetornoDTO>> {
    return this.http.get<Page<ReservaRetornoDTO>>(`${this.apiUrl}/funcionario/${funcionarioId}/ativas`);
  }
  listarHistoricoReservas(funcionarioId: number): Observable<Page<ReservaRetornoDTO>> {
    return this.http.get<Page<ReservaRetornoDTO>>(`${this.apiUrl}/funcionario/${funcionarioId}/historico`);
  }
  abrirReserva(dto: ReservaDTO): Observable<ReservaRetornoDTO> {
    return this.http.post<ReservaRetornoDTO>(this.apiUrl, dto);
  }
}
