import { Page, ResumoChamadoDTO } from './../../../core/models/chamado';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaService } from '../../../core/service/reserva.service';
import { ReservaRetornoDTO } from '../../../core/models/reserva';
import { ChamadoService } from 'src/app/core/service/chamado.service';

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
    private chamadoService: ChamadoService
  ) {}

  ngOnInit(): void {
    this.carregarDashboard();
  }

  carregarDashboard(): void {
    const funcionarioIdMock = 6;

    this.reservaService.listarMinhasReservasAtivas(funcionarioIdMock).subscribe({
      next: (pagina: Page<ReservaRetornoDTO>) => {
        this.minhasReservas = pagina.content;
        this.carregandoReservas = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar reservas, backend pode estar desligado/ausente', erro);
        this.carregandoReservas = false;
      }
    });

    this.reservaService.listarHistoricoReservas(funcionarioIdMock).subscribe({
      next: (pagina: Page<ReservaRetornoDTO>) => {
        this.historicoReservas = pagina.content;
        this.carregandoHistorico = false;
      },
      error: (erro) => {
        console.error('Erro histórico reservas', erro);
        this.carregandoHistorico = false;
      }
    });

    this.chamadoService.listarMeusChamados(funcionarioIdMock).subscribe({
      next: (pagina: Page<ResumoChamadoDTO>) => {
        this.meusChamados = pagina.content;
        this.carregandoChamados = false;
      },
      error: (erro) => {
        console.error('Erro chamados', erro);
        this.carregandoChamados = false;
      }
    });

  }

  navegarParaNovaReserva(): void {
    this.router.navigate(['/novareserva']);
  }

  navegarParaNovoChamado(): void {
    this.router.navigate(['/chamados/abrir']);
  }
}
