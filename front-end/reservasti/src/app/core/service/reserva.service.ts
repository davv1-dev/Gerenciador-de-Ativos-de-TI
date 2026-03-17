import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReservaRetornoDTO, ReservaDTO } from '../models/reserva';
import { Page } from '../models/chamado';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private apiUrl = `${environment.apiUrl}/reservas`;

  constructor(private http: HttpClient) { }

  abrirReserva(dto: ReservaDTO): Observable<ReservaRetornoDTO> {
    return this.http.post<ReservaRetornoDTO>(this.apiUrl, dto);
  }

  retirarEquipamento(idReserva: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/retirar`, idReserva);
  }

  devolverEquipamento(idReserva: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/devolver`, idReserva);
  }

  cancelarReserva(idReserva: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/cancelar`, idReserva);
  }

  listarMinhasReservasAtivas(): Observable<Page<ReservaRetornoDTO>> {
    return this.http.get<Page<ReservaRetornoDTO>>(`${this.apiUrl}/minhas/ativas`);
  }

  listarHistoricoReservas(): Observable<Page<ReservaRetornoDTO>> {
    return this.http.get<Page<ReservaRetornoDTO>>(`${this.apiUrl}/minhas/historico`);
  }

  listarHistoricoAdmin(funcionarioId: number): Observable<Page<ReservaRetornoDTO>> {
    return this.http.get<Page<ReservaRetornoDTO>>(`${this.apiUrl}/historico/funcionarios/${funcionarioId}`);
  }
}
