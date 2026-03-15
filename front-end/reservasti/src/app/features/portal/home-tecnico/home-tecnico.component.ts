import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ChamadoService } from '../../../core/service/chamado.service';
import { ResumoChamadoDTO } from '../../../core/models/chamado';
import { ToastService } from 'src/app/core/service/toast.service';

@Component({
  selector: 'app-home-tecnico',
  templateUrl: './home-tecnico.component.html',
  styleUrls: ['./home-tecnico.component.css']
})
export class HomeTecnicoComponent implements OnInit {

  filtroAtivo: 'hoje' | '7dias' | '30dias' | 'todos' = 'hoje';
  historicoChamados: ResumoChamadoDTO[] = [];
  carregando = false;

  paginaAtual: number = 0;
  tamanhoPagina: number = 5;
  totalPaginas: number = 0;
  isUltimaPagina: boolean = true;
  isPrimeiraPagina: boolean = true;

  // 👇 REMOVIDO: private idTecnicoLogado = 7;

  constructor(
    private router: Router,
    private chamadoService: ChamadoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    // 👇 PROTEÇÃO DE ROTA: Apenas Técnicos entram aqui!
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario !== 'TECNICO') {
      this.toastService.mostrar('Acesso negado. Página restrita a Técnicos.', 'erro');

      // Chuta o intruso para o lugar certo
      if (tipoUsuario === 'ADMIN') {
        this.router.navigate(['/home-admin']);
      } else {
        this.router.navigate(['/home']);
      }
      return;
    }

    this.carregarHistorico(this.filtroAtivo);
  }

  irParaFilaGlobal(): void {
    this.router.navigate(['tecnico/fila']);
  }

  irParaMinhaFila(): void {
    this.router.navigate(['tecnico/minha-fila']);
  }

  irParaChamadoAtual(): void {
    this.router.navigate(['tecnico/chamado-atual']);
  }

  irParaNovaReserva(): void {
    this.router.navigate(['/novareserva']);
  }

  aplicarFiltro(periodo: 'hoje' | '7dias' | '30dias' | 'todos'): void {
    if (this.filtroAtivo === periodo) return;

    this.filtroAtivo = periodo;
    this.paginaAtual = 0;
    this.carregarHistorico(periodo);
  }

  private carregarHistorico(periodo: string): void {
    this.carregando = true;

    this.chamadoService.listarHistoricoTecnico(periodo, this.paginaAtual, this.tamanhoPagina)
      .subscribe({
        next: (pagina) => {
          this.historicoChamados = pagina.content;

          this.paginaAtual = pagina.number;
          this.totalPaginas = pagina.totalPages;
          this.isPrimeiraPagina = (this.paginaAtual === 0) ;
          this.isUltimaPagina = (this.totalPaginas === 0) || (this.paginaAtual >= this.totalPaginas - 1);

          this.carregando = false;
        },
        error: (erro) => {
          console.error('Erro ao buscar o histórico de chamados', erro);
          this.toastService.mostrar('Não foi possível carregar o histórico. Verifique a conexão.', 'info');
          this.carregando = false;
        }
      });
  }

  mudarPagina(novaPagina: number): void {
    this.paginaAtual = novaPagina;
    this.carregarHistorico(this.filtroAtivo);
  }
}
