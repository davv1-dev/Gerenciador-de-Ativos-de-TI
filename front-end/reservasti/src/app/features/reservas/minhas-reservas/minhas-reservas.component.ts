import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaService } from '../../../core/service/reserva.service';
import { ReservaRetornoDTO } from '../../../core/models/reserva';
import { ToastService } from 'src/app/core/service/toast.service';

@Component({
  selector: 'app-minhas-reservas',
  templateUrl: './minhas-reservas.component.html',
  styleUrls: ['./minhas-reservas.component.css']
})
export class MinhasReservasComponent implements OnInit {

  reservasAtivas: ReservaRetornoDTO[] = [];
  carregando = true;

  constructor(
    private reservaService: ReservaService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarReservas();
  }

  carregarReservas(): void {
    this.carregando = true;
    this.reservaService.listarMinhasReservasAtivas().subscribe({
      next: (pagina) => {
        this.reservasAtivas = pagina.content;
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao buscar reservas ativas', err);
        this.toastService.mostrar('Erro ao buscar suas reservas.', 'erro');
        this.carregando = false;
      }
    });
  }

  retirar(idReserva: number): void {
    this.reservaService.retirarEquipamento(idReserva).subscribe({
      next: () => {
        this.toastService.mostrar('Equipamento retirado com sucesso!', 'sucesso');
        this.carregarReservas();
      },
      error: (err) => this.toastService.mostrar('Erro ao retirar: ' + err.error.mensagem, 'erro')
    });
  }

  devolver(idReserva: number): void {
    this.reservaService.devolverEquipamento(idReserva).subscribe({
      next: () => {
        this.toastService.mostrar('Equipamento devolvido com sucesso!', 'sucesso');
        this.carregarReservas();
      },
      error: (err) => this.toastService.mostrar('Erro ao devolver: ' + err.error.mensagem, 'erro')
    });
  }

  cancelar(idReserva: number): void {
    if (confirm('Tem certeza que deseja cancelar esta reserva?')) {
      this.reservaService.cancelarReserva(idReserva).subscribe({
        next: () => {
          this.toastService.mostrar('Reserva cancelada com sucesso.', 'sucesso');
          this.carregarReservas();
        },
        error: (err) => this.toastService.mostrar('Erro ao cancelar: ' + err.error.mensagem, 'erro')
      });
    }
  }
}
