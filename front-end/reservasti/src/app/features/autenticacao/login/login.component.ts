import { UsuarioEntradaDTO } from './../../../core/models/usuario';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/service/auth.service'; // Ajuste o caminho se necessário
import { ToastService } from 'src/app/core/service/toast.service';
import { TokenResponseDTO } from 'src/app/core/models/token';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  credenciais: UsuarioEntradaDTO= {
    login: '',
    senha: ''
  };

  constructor(
    private router: Router,
    private authService: AuthService,
    private toastService:ToastService
  ) { }

  ngOnInit(): void {
  }

  fazerLogin(): void {
    this.authService.efetuarLogin(this.credenciais).subscribe({

      next: (resposta: TokenResponseDTO) => {

        sessionStorage.setItem('tipoUsuario', resposta.tipoUsuario);

        if (resposta.tipoUsuario === 'ADMIN') {
          this.router.navigate(['/home-admin']);

        } else if (resposta.tipoUsuario === 'TECNICO') {
          this.router.navigate(['/home-tecnico']);

        } else {
          this.router.navigate(['/home']);
        }
      },
      error: (erro) => {
        console.error('Falha no login', erro);
        this.toastService.mostrar('Login ou senha incorretos. Tente novamente!','erro');
      }
    });
  }
}
