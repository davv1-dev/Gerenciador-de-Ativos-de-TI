import { Page, ResumoChamadoDTO } from '../../../core/api/models/chamado';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaService } from '../../../core/api/service/reserva.service';
import { ReservaRetornoDTO } from '../../../core/api/models/reserva';
import { ChamadoService } from 'src/app/core/api/service/chamado.service';
import { ToastService } from 'src/app/core/api/service/toast.service';

@Component({
  selector: 'app-home-funcionario',
  templateUrl: './home-funcionario.component.html',
  styleUrls: ['./home-funcionario.component.css']
})
export class HomeFuncionarioComponent implements OnInit {

  nomeUsuario:string = sessionStorage.getItem('nomeUsuario') || 'Colaborador';
  minhasReservas: ReservaRetornoDTO[] = [];
  historicoReservas: ReservaRetornoDTO[] = [];
  meusChamados: ResumoChamadoDTO[] = [];

  carregandoReservas = true;
  carregandoHistorico = true;
  carregandoChamados = true;

  paginaAtualChamados: number = 0;
  tamanhoPaginaChamados: number = 10;
  totalPaginasChamados: number = 0;
  isUltimaPaginaChamados: boolean = true;
  isPrimeiraPaginaChamados: boolean = true;

  constructor(
    private reservaService: ReservaService,
    private router: Router,
    private chamadoService: ChamadoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    //Verificacao
    const tipoUsuario:string = sessionStorage.getItem('tipoUsuario') || '';
    if (tipoUsuario === 'ADMIN') {
      this.router.navigate(['/home-admin']);
      return;
    } else if (tipoUsuario === 'TECNICO') {
      this.router.navigate(['/home-tecnico']);
      return;
    }

    const nomeSalvo:string = sessionStorage.getItem('nomeUsuario') || '';
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
        this.carregandoReservas= false;
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
    this.carregarMeusChamados();
  }

  carregarMeusChamados(pagina: number = 0): void {
    this.carregandoChamados = true;
    this.paginaAtualChamados = pagina;

    this.chamadoService.listarMeusChamados(this.paginaAtualChamados, this.tamanhoPaginaChamados)
      .subscribe({
        next: (pagina) => {
          this.meusChamados = pagina.content;


          this.totalPaginasChamados = pagina.totalPages;

          this.isPrimeiraPaginaChamados = (this.paginaAtualChamados === 0) ;
          this.isUltimaPaginaChamados = (this.totalPaginasChamados === 0) || (this.paginaAtualChamados >= this.totalPaginasChamados - 1);

          this.carregandoChamados = false;
        },
        error: (erro) => {
          console.error('Erro ao carregar meus chamados', erro);
          this.carregandoChamados = false;
        }
      });
  }

  navegarParaMinhasReservas():void{
    this.router.navigate(['/minhasreservas']);
  }

  navegarParaNovaReserva(): void {
    this.router.navigate(['/novareserva']);
  }

  navegarParaNovoChamado(): void {
    this.router.navigate(['/fazerchamado']);
  }

  mudarPaginaChamados(novaPagina: number): void {
    if (novaPagina >= 0 && novaPagina < this.totalPaginasChamados) {
      this.carregarMeusChamados(novaPagina);
    }
  }
}
