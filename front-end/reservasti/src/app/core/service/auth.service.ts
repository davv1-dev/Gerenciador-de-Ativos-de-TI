import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UsuarioEntradaDTO } from '../models/usuario';
import { TokenResponseDTO } from '../models/token';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}/reservasti` ;

  constructor(private http: HttpClient,
    private router:Router
  ) { }

  efetuarLogin(credenciais: UsuarioEntradaDTO): Observable<TokenResponseDTO> {
    return this.http.post<TokenResponseDTO>(`${this.apiUrl}/login`, credenciais).pipe(
      tap((resposta) => {
        sessionStorage.setItem('token', resposta.tokenJWT);
        sessionStorage.setItem('refreshToken', resposta.refreshToken);
      })
    );
  }

logoutBackend(): Observable<any> {
    return this.http.post(`${environment.apiUrl}/reservasti/logout`, {});
  }

  encerrarSessaoLocal(): void {
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }
}
