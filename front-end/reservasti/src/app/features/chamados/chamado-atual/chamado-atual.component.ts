import { Component, OnInit } from '@angular/core';
import { ChamadoService } from '../../../core/service/chamado.service';
import { ResumoChamadoDTO } from '../../../core/models/chamado';
import { ToastService } from 'src/app/core/service/toast.service';
import { ConfirmDialogService } from 'src/app/core/service/confirm-dialog.service';

@Component({
  selector: 'app-fila-pessoal',
  templateUrl: './chamado-atual.component.html',
  styleUrls: ['./chamado-atual.component.css']
})
export class ChamadoAtualComponent implements OnInit {

  chamadoAtual: ResumoChamadoDTO | null = null;
  carregando: boolean = true;
  processando: boolean = false;

  private idTecnicoLogado = 7;

  constructor(private chamadoService: ChamadoService,
    private toastService: ToastService,
    private confirmDialogService: ConfirmDialogService
  ) {}

  ngOnInit(): void {
    this.carregarFila();
  }

  carregarFila(): void {
    this.carregando = true;

    this.chamadoService.listarFilaPessoal(this.idTecnicoLogado, 0, 1).subscribe({
      next: (pagina) => {
        this.chamadoAtual = pagina.content.length > 0 ? pagina.content[0] : null;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao carregar chamado', erro);
        this.carregando = false;
      }
    });
  }

  resolver(): void {
    if (!this.chamadoAtual) return;

    const idChamado = this.chamadoAtual.id

    this.confirmDialogService.confirmar('Concluir Atendimento', 'Tem certeza que deseja resolver este chamado?')
    .then((confirmado)=> {

          if(!confirmado) return

          this.processando = true;

          this.chamadoService.resolverChamado(idChamado).subscribe({
        next: () => {
          this.toastService.mostrar('Chamado resolvido com sucesso! Excelente trabalho!','sucesso');
          this.chamadoAtual = null;
          this.processando = false;
        },
        error: (erro) => {
          console.error('Erro ao resolver chamado', erro);
          this.toastService.mostrar('Falha ao resolver o chamado. Tente novamente.','erro');
          this.processando = false;
        }
      });
    })
  }
}
