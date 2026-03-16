import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-esqueci-senha',
  templateUrl: './esqueci-senha.component.html',
  styleUrls: ['./esqueci-senha.component.css'],
})
export class EsqueciSenhaComponent {

  constructor(private router: Router) {}

  voltarParaLogin(): void {
    this.router.navigate(['/login']);
  }
}
