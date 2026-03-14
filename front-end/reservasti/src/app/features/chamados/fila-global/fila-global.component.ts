import { ToastService } from './../../../core/service/toast.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ChamadoService } from '../../../core/service/chamado.service';
import { Page, ResumoChamadoDTO } from '../../../core/models/chamado';
import { Subscription, interval } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-fila-global',
  templateUrl: './fila-global.component.html',
  styleUrls: ['./fila-global.component.css']
})
export class FilaGlobalComponent implements OnInit, OnDestroy {
  chamadosPage: Page<ResumoChamadoDTO> | null = null;
  carregando: boolean = true;
  processandoId: number | null = null;
  private pingSubscription!: Subscription;
  private idDoTecnicoLogado = 7;

  private sseSubscription?: Subscription;

  constructor(private chamadoService: ChamadoService,
              private router: Router,
              private toastService:ToastService
  ) { }

  ngOnInit(): void {
    this.carregarFila(0);
    this.iniciarEscutaAoVivo();
    this.avisarQueEstouOnline();

    this.pingSubscription = interval(120000).subscribe(() => {
      this.avisarQueEstouOnline();
    });
  }


  ngOnDestroy(): void {
    if (this.sseSubscription) {
      this.sseSubscription.unsubscribe();
    }
    if (this.pingSubscription) {
      this.pingSubscription.unsubscribe();
    }
  }

  carregarFila(pagina: number): void {
    this.carregando = true;
    this.chamadoService.listarFilaGlobal(pagina, 10).subscribe({
      next: (page) => {
        this.chamadosPage = page;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar a fila', erro);
        this.carregando = false;
      }
    });
  }

  iniciarEscutaAoVivo(): void {

    this.sseSubscription = this.chamadoService.escutarFilaGlobalAoVivo(this.idDoTecnicoLogado).subscribe({
      next: (novoChamado) => {
        console.log(novoChamado);

        if (this.chamadosPage && this.chamadosPage.content) {

          this.chamadosPage.content.push(novoChamado);

          this.chamadosPage.totalElements++;
        } else {
          this.carregarFila(0);
        }
      },
      error: (erro) => {
        console.error('Erro na escuta ao vivo', erro);
      }
    });
  }
  assumir(chamadoId: number): void {
    this.processandoId = chamadoId;

    this.chamadoService.assumirChamado(chamadoId, this.idDoTecnicoLogado).subscribe({
      next: () => {
        this.toastService.mostrar('Chamado assumido com sucesso! Redirecionando para aba do chamado');
        this.processandoId = null;
        this.router.navigate(['/chamado-atual']);
      },
      error: (erro) => {
        console.error('Erro ao assumir o chamado', erro);

        const msgErro = erro.error?.message || erro.error || 'Você já possui um chamado em andamento ou ocorreu um erro.';
        this.toastService.mostrar(msgErro,'info');

        this.processandoId = null;
      }
    });
  }
  avisarQueEstouOnline(): void {
    this.chamadoService.pingTecnico(this.idDoTecnicoLogado).subscribe({
      error: (err) => console.error('Erro no ping silencioso', err)
    });
  }
}
