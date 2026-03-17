import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ChamadoService } from 'src/app/core/service/chamado.service';
import { ToastService } from 'src/app/core/service/toast.service';
import { Page, ResumoChamadoDTO } from 'src/app/core/models/chamado';

@Component({
  selector: 'app-minha-fila',
  templateUrl: './minha-fila.component.html',
  styleUrls: ['../fila-global/fila-global.component.css']
})
export class MinhaFilaComponent implements OnInit, OnDestroy {

  minhaFilaPage: Page<ResumoChamadoDTO> | null = null;
  carregando: boolean = true;
  processandoId: number | null = null;

  private sseSubscription: Subscription | null = null;

  constructor(
    private chamadoService: ChamadoService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario !== 'TECNICO') {
      this.toastService.mostrar('Acesso negado. Área restrita a técnicos.', 'erro');
      this.router.navigate(['/home']);
      return;
    }

    this.carregarFila(0);
    this.iniciarEscutaAoVivo();
  }

  carregarFila(pagina: number): void {
    this.carregando = true;
    this.chamadoService.listarFilaPessoal(pagina, 10).subscribe({
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
    this.sseSubscription = this.chamadoService.escutarMinhaFilaAoVivo().subscribe({
      next: (novoChamado) => {
        if (this.minhaFilaPage && this.minhaFilaPage.content) {
          this.minhaFilaPage.content.push(novoChamado);
          this.minhaFilaPage.totalElements++;
        } else {
          this.carregarFila(0);
        }
        this.toastService.mostrar('Novo chamado direcionado para você!', 'info');
      },
      error: (erro) => {
        console.error('Erro na escuta ao vivo da Minha Fila', erro);
      }
    });
  }

  atenderChamado(idChamado: number): void {
    this.processandoId = idChamado;
    sessionStorage.setItem('idChamadoAtual', idChamado.toString());
    this.router.navigate(['/tecnico/chamado-atual']);
  }

  ngOnDestroy(): void {
    if (this.sseSubscription) {
      this.sseSubscription.unsubscribe();
    }
  }
}
