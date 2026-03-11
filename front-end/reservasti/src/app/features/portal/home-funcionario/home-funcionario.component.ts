import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaService } from '../../../core/service/reserva.service';
import { ReservaRetornoDTO } from '../../../core/models/reserva';

@Component({
  selector: 'app-home-funcionario',
  templateUrl: './home-funcionario.component.html',
  styleUrls: ['./home-funcionario.component.css']
})
export class HomeFuncionarioComponent implements OnInit {

  nomeUsuario = 'Colaborador';
  minhasReservas: ReservaRetornoDTO[] = [];
  carregando = true;

  constructor(
    private reservaService: ReservaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarDashboard();
  }

  carregarDashboard(): void {
    // 🚧 Mock: ID 1 forçado até termos o sistema de Login
    const funcionarioIdMock = 1;

    this.reservaService.listarMinhasReservasAtivas(funcionarioIdMock).subscribe({
      next: (dados) => {
        this.minhasReservas = dados;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar reservas, backend pode estar desligado/ausente', erro);
        this.carregando = false;
      }
    });
  }

  navegarParaNovaReserva(): void {
    this.router.navigate(['/reservas/nova']);
  }

  navegarParaNovoChamado(): void {
    this.router.navigate(['/chamados/abrir']);
  }
}
