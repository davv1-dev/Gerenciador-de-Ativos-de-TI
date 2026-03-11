import { Component, OnInit, OnDestroy } from '@angular/core'; // 👈 Importamos o OnDestroy
import { ChamadoService } from '../../../core/service/chamado.service';
import { Page, ResumoChamadoDTO } from '../../../core/models/chamado';
import { Subscription } from 'rxjs'; // 👈 Importamos o Subscription

@Component({
  selector: 'app-fila-global',
  templateUrl: './fila-global.component.html',
  styleUrls: ['./fila-global.component.css']
})
export class FilaGlobalComponent implements OnInit, OnDestroy {
  chamadosPage: Page<ResumoChamadoDTO> | null = null;
  carregando: boolean = true;

  private sseSubscription?: Subscription;

  constructor(private chamadoService: ChamadoService) { }

  ngOnInit(): void {
    this.carregarFila(0);
    this.iniciarEscutaAoVivo();
  }

  ngOnDestroy(): void {
    if (this.sseSubscription) {
      this.sseSubscription.unsubscribe();
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
    const tecnicoIdMock = 1;

    this.sseSubscription = this.chamadoService.escutarFilaGlobalAoVivo(tecnicoIdMock).subscribe({
      next: (novoChamado) => {
        console.log('🔥 NOVO CHAMADO VIA SSE RECEBIDO:', novoChamado);

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
}
