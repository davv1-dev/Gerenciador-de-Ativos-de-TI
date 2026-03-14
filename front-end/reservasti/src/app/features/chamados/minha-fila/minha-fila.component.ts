import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ChamadoService } from 'src/app/core/service/chamado.service';
import { ToastService } from 'src/app/core/service/toast.service';
import { Page, ResumoChamadoDTO } from 'src/app/core/models/chamado'; // 👈 Importando o Page!

@Component({
  selector: 'app-minha-fila',
  templateUrl: './minha-fila.component.html',
  styleUrls: ['../fila-global/fila-global.component.css'] // Reaproveitando o CSS (Excelente escolha!)
})
export class MinhaFilaComponent implements OnInit, OnDestroy {

  // 👇 Usando o mesmo padrão da Fila Global
  minhaFilaPage: Page<ResumoChamadoDTO> | null = null;
  carregando: boolean = true;
  processandoId: number | null = null; // Para desabilitar o botão enquanto redireciona

  private sseSubscription: Subscription | null = null;
  private idTecnicoLogado = 7;

  constructor(
    private chamadoService: ChamadoService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarFila(0); // Começando pela página 0
    this.iniciarEscutaAoVivo();
  }

  // 👇 Método padronizado para suportar paginação
  carregarFila(pagina: number): void {
    this.carregando = true;
    this.chamadoService.listarFilaPessoal(this.idTecnicoLogado, pagina, 10).subscribe({
      next: (page) => {
        this.minhaFilaPage = page;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar minha fila', erro);
        this.toastService.mostrar('Erro ao carregar sua fila inicial.', 'erro');
        this.carregando = false;
      }
    });
  }

  iniciarEscutaAoVivo(): void {
    this.sseSubscription = this.chamadoService.escutarMinhaFilaAoVivo(this.idTecnicoLogado).subscribe({
      next: (novoChamado) => {

        // 👇 Logica segura para atualizar o Page com o novo item do SSE
        if (this.minhaFilaPage && this.minhaFilaPage.content) {
          this.minhaFilaPage.content.push(novoChamado); // FIFO
          this.minhaFilaPage.totalElements++;
        } else {
          // Se estava nulo, força um recarregamento
          this.carregarFila(0);
        }

        this.toastService.mostrar('Novo chamado direcionado para você!', 'info');
      },
      error: (erro) => {
        console.error('Erro na escuta ao vivo da Minha Fila', erro);
      }
    });
  }

  // 👇 Redireciona para o Modo Foco (Chamado Atual)
  atenderChamado(idChamado: number): void {
    this.processandoId = idChamado;
    // Opcional: mostrar um toast avisando que está abrindo
    // this.toastService.mostrar('Abrindo área de trabalho...', 'info');

    // O router.navigate é muito rápido, mas mantemos o processandoId para dar feedback visual no botão
    this.router.navigate(['/chamado-atual']);
  }

  ngOnDestroy(): void {
    if (this.sseSubscription) {
      this.sseSubscription.unsubscribe();
    }
  }
}
