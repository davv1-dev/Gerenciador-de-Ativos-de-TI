import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RelatorioService } from '../../../core/service/relatorio.service';
import { RelatorioGeralDTO } from '../../../core/models/relatorio';
import { ToastService } from 'src/app/core/service/toast.service';
import { FuncionarioRetornoDTO } from 'src/app/core/models/funcionario';
import { FuncionarioService } from 'src/app/core/service/funcionario.service';
import { Page } from 'src/app/core/models/chamado';
import { ConfirmDialogService } from 'src/app/core/service/confirm-dialog.service';

@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.css']
})
export class HomeAdminComponent implements OnInit {

  nomeAdmin: string = 'Administrador';
  carregandoRelatorio: boolean = true;

  relatorioGeral?: RelatorioGeralDTO;

  abaSolicitacoes: 'pendentes' | 'historico' = 'pendentes';

  solicitacoesPendentes: FuncionarioRetornoDTO[] = [];
  historicoSolicitacoes: FuncionarioRetornoDTO[] = [];

  carregandoPendentes: boolean = true;
  carregandoHistorico: boolean = true;

  constructor(
    private router: Router,
    private relatorioService: RelatorioService,
    private funcionarioService: FuncionarioService,
    private toastService: ToastService,
    private confirmDialogService: ConfirmDialogService
  ) {}

  ngOnInit(): void {
    // 👇 LEÃO DE CHÁCARA: Apenas Admins entram aqui!
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario !== 'ADMIN') {
      this.toastService.mostrar('Acesso negado. Área restrita à diretoria.', 'erro');

      if (tipoUsuario === 'TECNICO') {
        this.router.navigate(['/home-tecnico']);
      } else {
        this.router.navigate(['/home']);
      }
      return;
    }

    // 👇 Puxa o nome real do Admin para exibir na tela
    const nomeSalvo = sessionStorage.getItem('nomeUsuario');
    if (nomeSalvo) {
      this.nomeAdmin = nomeSalvo.split(' ')[0];
    }

    this.carregarDashboardGeral();
    this.carregarSolicitacoesPendentes();
    this.carregarHistorico();
  }

  alternarAbaSolicitacoes(aba: 'pendentes' | 'historico'): void {
    this.abaSolicitacoes = aba;
  }

  carregarDashboardGeral(): void {
    this.carregandoRelatorio = true;

    this.relatorioService.gerarRelatorioGeral().subscribe({
      next: (dados) => {
        this.relatorioGeral = dados;
        this.carregandoRelatorio = false;
      },
      error: (erro) => {
        console.error('Erro ao carregar relatório geral', erro);
        this.toastService.mostrar('Falha ao carregar métricas. Verifique a conexão.', 'info');
        this.carregandoRelatorio = false;
      }
    });
  }

  carregarSolicitacoesPendentes(): void {
    this.carregandoPendentes = true;
    this.funcionarioService.listarPendentes(0).subscribe({
      next: (pagina: Page<FuncionarioRetornoDTO>) => {
        this.solicitacoesPendentes = pagina.content;
        this.carregandoPendentes = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar pendentes:', erro);
        this.toastService.mostrar('Erro ao carregar solicitações pendentes', 'erro');
        this.carregandoPendentes = false;
      }
    });
  }

  carregarHistorico(): void {
    this.carregandoHistorico = true;
    this.funcionarioService.listarHistorico(0).subscribe({
      next: (pagina: Page<FuncionarioRetornoDTO>) => {
        this.historicoSolicitacoes = pagina.content;
        this.carregandoHistorico = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar histórico:', erro);
        this.toastService.mostrar('Erro ao carregar histórico de acessos', 'erro');
        this.carregandoHistorico = false;
      }
    });
  }

  async aprovarAcesso(id: number): Promise<void> {
    const confirmado = await this.confirmDialogService.confirmar(
      'Aprovar Acesso',
      'Tem certeza que deseja APROVAR este usuário?'
    );

    if (confirmado) {
      this.funcionarioService.aprovarAcesso(id).subscribe({
        next: () => {
          this.toastService.mostrar('Acesso aprovado com sucesso!', 'sucesso');
          this.carregarSolicitacoesPendentes();
          this.carregarHistorico();
        },
        error: (erro) => {
          console.error(erro);
          this.toastService.mostrar('Erro ao aprovar usuário.', 'erro');
        }
      });
    }
  }

  async negarAcesso(id: number): Promise<void> {
    const confirmado = await this.confirmDialogService.confirmar(
      'Negar Acesso',
      'Tem certeza que deseja NEGAR o acesso deste usuário?'
    );

    if (confirmado) {
      this.funcionarioService.negarAcesso(id).subscribe({
        next: () => {
          this.toastService.mostrar('Acesso negado.', 'info');
          this.carregarSolicitacoesPendentes();
          this.carregarHistorico();
        },
        error: (erro) => {
          console.error(erro);
          this.toastService.mostrar('Erro ao negar usuário.', 'erro');
        }
      });
    }
  }

  irParaGerenciarEquipamentos(): void { this.router.navigate(['/admin/equipamentos']); }
  irParaAlocacaoEquipamentos(): void { this.router.navigate(['/admin/alocacao']); }
  irParaGerenciarDepartamentos(): void { this.router.navigate(['/admin/departamentos']); }
  irParaRelatorios(): void { this.router.navigate(['/admin/relatorios']); }
  irParaGerenciarUsuarios(): void { this.router.navigate(['/admin/usuarios']); }
  irParaNovaReserva(): void { this.router.navigate(['/novareserva']); }
  irParaFazerChamado(): void { this.router.navigate(['/fazerchamado']); }
}
