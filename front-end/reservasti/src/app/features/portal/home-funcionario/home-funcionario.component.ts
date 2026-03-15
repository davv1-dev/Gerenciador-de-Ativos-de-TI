import { Page, ResumoChamadoDTO } from './../../../core/models/chamado';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaService } from '../../../core/service/reserva.service';
import { ReservaRetornoDTO } from '../../../core/models/reserva';
import { ChamadoService } from 'src/app/core/service/chamado.service';
import { ToastService } from 'src/app/core/service/toast.service'; // 👈 Adicionamos para consistência

@Component({
  selector: 'app-home-funcionario',
  templateUrl: './home-funcionario.component.html',
  styleUrls: ['./home-funcionario.component.css']
})
export class HomeFuncionarioComponent implements OnInit {

  nomeUsuario = 'Colaborador';
  minhasReservas: ReservaRetornoDTO[] = [];
  historicoReservas: ReservaRetornoDTO[] = [];
  meusChamados: ResumoChamadoDTO[] = [];

  carregandoReservas = true;
  carregandoHistorico = true;
  carregandoChamados = true;

  constructor(
    private reservaService: ReservaService,
    private router: Router,
    private chamadoService: ChamadoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario === 'ADMIN') {
      this.router.navigate(['/home-admin']);
      return;
    } else if (tipoUsuario === 'TECNICO') {
      this.router.navigate(['/home-tecnico']);
      return;
    }

    const nomeSalvo = sessionStorage.getItem('nomeUsuario');
    if (nomeSalvo) {
      this.nomeUsuario = nomeSalvo.split(' ')[0];
    }

    this.carregarDashboard();
  }

  carregarDashboard(): void {

    this.reservaService.listarMinhasReservasAtivas().subscribe({
      next: (pagina: Page<ReservaRetornoDTO>) => {
        this.minhasReservas = pagina.content;
        this.carregandoReservas = false;
      },
      error: (erro) => {
        console.error('Erro ao carregar reservas', erro);
        this.carregandoReservas = false;
      }
    });

    this.reservaService.listarHistoricoReservas().subscribe({
      next: (pagina: Page<ReservaRetornoDTO>) => {
        this.historicoReservas = pagina.content;
        this.carregandoHistorico = false;
      },
      error: (erro) => {
        console.error('Erro ao carregar histórico de reservas', erro);
        this.carregandoHistorico = false;
      }
    });
  }

  navegarParaNovaReserva(): void {
    this.router.navigate(['/novareserva']);
  }

  navegarParaNovoChamado(): void {
    this.router.navigate(['/fazerchamado']);
  }
}
