import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = sessionStorage.getItem('token');
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');

    if (token) {
      const rotaDestino = state.url;

      if (rotaDestino.includes('admin') && tipoUsuario !== 'ADMIN') {
        this.redirecionarParaHomeCorreta(tipoUsuario);
        return false;
      }

      if (rotaDestino.includes('tecnico') && tipoUsuario !== 'TECNICO') {
        this.redirecionarParaHomeCorreta(tipoUsuario);
        return false;
      }

      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }

  private redirecionarParaHomeCorreta(tipoUsuario: string | null): void {
    if (tipoUsuario === 'ADMIN') {
      this.router.navigate(['/home-admin']);
    } else if (tipoUsuario === 'TECNICO') {
      this.router.navigate(['/home-tecnico']);
    } else {
      this.router.navigate(['/home']);
    }
  }
}
