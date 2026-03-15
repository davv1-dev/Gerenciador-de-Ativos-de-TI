import { Component, OnInit } from '@angular/core';
import { FuncionarioService } from 'src/app/core/service/funcionario.service';
import { FuncionarioRetornoDTO } from 'src/app/core/models/funcionario'; // Ajuste o path se necessário
import { ToastService } from 'src/app/core/service/toast.service';
import { Page } from 'src/app/core/models/chamado';
import { Router } from '@angular/router';
import { ConfirmDialogService } from 'src/app/core/service/confirm-dialog.service';

@Component({
  selector: 'app-gestao-usuarios',
  templateUrl: './gestao-usuarios.component.html',
  styleUrls: ['./gestao-usuarios.component.css']
})
export class GestaoUsuariosComponent implements OnInit {

  // Controle de Abas
  abaAtiva: 'usuarios' | 'pendentes' | 'historico' = 'usuarios';

  // --- Estado: Usuários Ativos ---
  funcionarios: FuncionarioRetornoDTO[] = [];
  carregandoUsuarios: boolean = true;
  paginaAtual: number = 0;
  tamanhoPagina: number = 10;
  totalElementos: number = 0;
  termoBusca: string = '';

  // --- Estado: Solicitações ---
  solicitacoesPendentes: FuncionarioRetornoDTO[] = [];
  historicoSolicitacoes: FuncionarioRetornoDTO[] = [];
  carregandoPendentes: boolean = false;
  carregandoHistorico: boolean = false;

  constructor(
    private funcionarioService: FuncionarioService,
    private toastService: ToastService,
    private router: Router,
    private confirmDialogService: ConfirmDialogService
  ) {}

  ngOnInit(): void {
    // 👇 LEÃO DE CHÁCARA: Apenas Admins passam da porta!
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario !== 'ADMIN') {
      this.toastService.mostrar('Acesso negado. Área restrita à administração.', 'erro');

      if (tipoUsuario === 'TECNICO') {
        this.router.navigate(['/home-tecnico']);
      } else {
        this.router.navigate(['/home']);
      }
      return; // Para a execução aqui!
    }

    // Se é Admin, carrega os dados normalmente
    this.carregarUsuarios();
    this.carregarSolicitacoesPendentes(); // Já carrega em background para mostrar o contador
  }

  mudarAba(aba: 'usuarios' | 'pendentes' | 'historico'): void {
    this.abaAtiva = aba;
    if (aba === 'usuarios' && this.funcionarios.length === 0) this.carregarUsuarios();
    if (aba === 'pendentes' && this.solicitacoesPendentes.length === 0) this.carregarSolicitacoesPendentes();
    if (aba === 'historico' && this.historicoSolicitacoes.length === 0) this.carregarHistorico();
  }

  // ==========================================
  // ABA 1: USUÁRIOS ATIVOS
  // ==========================================
  carregarUsuarios(): void {
    this.carregandoUsuarios = true;
    this.funcionarioService.listar(this.paginaAtual, this.termoBusca).subscribe({
      next: (pagina: Page<FuncionarioRetornoDTO>) => {
        this.funcionarios = pagina.content;
        this.totalElementos = pagina.totalElements;
        this.carregandoUsuarios = false;
      },
      error: (erro) => {
        console.error(erro);
        this.toastService.mostrar('Erro ao carregar lista de usuários.', 'erro');
        this.carregandoUsuarios = false;
      }
    });
  }

  buscar(): void {
    this.paginaAtual = 0;
    this.carregarUsuarios();
  }

  mudarPagina(novaPagina: number): void {
    this.paginaAtual = novaPagina;
    this.carregarUsuarios();
  }

  novoUsuario(): void {
    this.router.navigate(['cadastro-funcionario']);
  }

  editarUsuario(id: number): void {
    this.router.navigate([`/admin/usuarios/editar/${id}`]);
  }

  async desativarUsuario(id: number, nome: string): Promise<void> {
    const confirmado = await this.confirmDialogService.confirmar('Desativar Usuário', `Tem certeza que deseja desativar o usuário ${nome}?`);
    if (confirmado) {
      this.funcionarioService.excluir(id).subscribe({
        next: () => {
          this.toastService.mostrar('Usuário desativado com sucesso.', 'sucesso');
          this.carregarUsuarios();
        },
        error: () => this.toastService.mostrar('Erro ao desativar usuário.', 'erro')
      });
    }
  }

  // ==========================================
  // ABA 2 e 3: SOLICITAÇÕES DE ACESSO
  // ==========================================
  carregarSolicitacoesPendentes(): void {
    this.carregandoPendentes = true;
    this.funcionarioService.listarPendentes(0).subscribe({
      next: (pagina: Page<FuncionarioRetornoDTO>) => {
        this.solicitacoesPendentes = pagina.content;
        this.carregandoPendentes = false;
      },
      error: () => this.carregandoPendentes = false
    });
  }

  carregarHistorico(): void {
    this.carregandoHistorico = true;
    this.funcionarioService.listarHistorico(0).subscribe({
      next: (pagina: Page<FuncionarioRetornoDTO>) => {
        this.historicoSolicitacoes = pagina.content;
        this.carregandoHistorico = false;
      },
      error: () => this.carregandoHistorico = false
    });
  }

  async aprovarAcesso(id: number): Promise<void> {
    const confirmado = await this.confirmDialogService.confirmar('Aprovar Acesso', 'Tem certeza que deseja APROVAR este usuário?');
    if (confirmado) {
      this.funcionarioService.aprovarAcesso(id).subscribe({
        next: () => {
          this.toastService.mostrar('Acesso aprovado com sucesso!', 'sucesso');
          this.carregarSolicitacoesPendentes();
          this.carregarHistorico();
          this.carregarUsuarios(); // Atualiza a lista principal de ativos também!
        },
        error: () => this.toastService.mostrar('Erro ao aprovar usuário.', 'erro')
      });
    }
  }

  async negarAcesso(id: number): Promise<void> {
    const confirmado = await this.confirmDialogService.confirmar('Negar Acesso', 'Tem certeza que deseja NEGAR o acesso deste usuário?');
    if (confirmado) {
      this.funcionarioService.negarAcesso(id).subscribe({
        next: () => {
          this.toastService.mostrar('Acesso negado.', 'info');
          this.carregarSolicitacoesPendentes();
          this.carregarHistorico();
        },
        error: () => this.toastService.mostrar('Erro ao negar usuário.', 'erro')
      });
    }
  }
}
