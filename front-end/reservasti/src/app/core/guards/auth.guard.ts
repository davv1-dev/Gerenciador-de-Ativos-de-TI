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

      // 1. Se tentar acessar tela de ADMIN sem ser ADMIN
      if (rotaDestino.includes('admin') && tipoUsuario !== 'ADMIN') {
        this.redirecionarParaHomeCorreta(tipoUsuario);
        return false;
      }

      // 2. Se tentar acessar tela de TÉCNICO sem ser TÉCNICO
      if (rotaDestino.includes('tecnico') && tipoUsuario !== 'TECNICO') {
        this.redirecionarParaHomeCorreta(tipoUsuario);
        return false;
      }

      // Se passou por tudo, a porta abre! 🟢
      return true;
    }

    // Se não tem token, manda direto pro login 🔴
    this.router.navigate(['/login']);
    return false;
  }

  // 👇 Método auxiliar que resolve o problema do usuário COMUM
  private redirecionarParaHomeCorreta(tipoUsuario: string | null): void {
    if (tipoUsuario === 'ADMIN') {
      this.router.navigate(['/home-admin']);
    } else if (tipoUsuario === 'TECNICO') {
      this.router.navigate(['/home-tecnico']);
    } else {
      // Se for COMUM (ou se o tipo vier nulo/inválido), manda pra home padrão
      this.router.navigate(['/home']);
    }
  }
}
