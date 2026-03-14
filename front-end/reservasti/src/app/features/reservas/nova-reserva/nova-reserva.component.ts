import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EquipamentoService } from '../../../core/service/equipamento.service';
import { ReservaService } from '../../../core/service/reserva.service';
import { EquipamentoRetornoDTO } from '../../../core/models/equipamento';
import { ReservaDTO } from '../../../core/models/reserva';
import { CategoriariaRetornoDTO } from 'src/app/core/models/categoria';
import { Page } from 'src/app/core/models/chamado';
import { ToastService } from 'src/app/core/service/toast.service';

@Component({
  selector: 'app-nova-reserva',
  templateUrl: './nova-reserva.component.html',
  styleUrls: ['./nova-reserva.component.css']
})
export class NovaReservaComponent implements OnInit {

  equipamentos: EquipamentoRetornoDTO[] = [];
  equipamentoSelecionado: EquipamentoRetornoDTO | null = null;
  carregandoCatalogo = true;

  categorias: CategoriariaRetornoDTO[] = [];
  categoriaSelecionada: number | null = null;

  termoBusca: string = '';

  reservaForm: FormGroup;
  enviando = false;

  constructor(
    private equipamentoService: EquipamentoService,
    private reservaService: ReservaService,
    private fb: FormBuilder,
    private router: Router,
    private toastService:ToastService
  ) {
    this.reservaForm = this.fb.group({
      dataPrevistaRetirada: ['', Validators.required],
      dataPrevistaDevolucao: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.carregarCategorias();
    this.buscarEquipamentos();
  }

  buscarEquipamentos(): void {
    this.carregandoCatalogo = true;
    this.equipamentoService.listarCatalogo(this.termoBusca, undefined).subscribe({
      next: (pagina) => {
        this.equipamentos = pagina.content;
        this.carregandoCatalogo = false;
      },
      error: (err) => {
        console.error('Erro ao buscar catálogo', err);
        this.carregandoCatalogo = false;
      }
    });
  }

  selecionarEquipamento(equipamento: EquipamentoRetornoDTO): void {
    this.equipamentoSelecionado = equipamento;
  }

  limparSelecao(): void {
    this.equipamentoSelecionado = null;
    this.reservaForm.reset();
  }

  confirmarReserva(): void {
    if (this.reservaForm.invalid || !this.equipamentoSelecionado) return;

    this.enviando = true;

    const funcionarioIdMock = 6;

    const novaReserva: ReservaDTO = {
      equipamentoId: this.equipamentoSelecionado.id,
      funcionarioId: funcionarioIdMock,
      dataPrevistaRetirada: this.reservaForm.value.dataPrevistaRetirada,
      dataPrevistaDevolucao: this.reservaForm.value.dataPrevistaDevolucao
    };

    this.reservaService.abrirReserva(novaReserva).subscribe({
      next: () => {
        this.toastService.mostrar('Reserva solicitada com sucesso!','sucesso');
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.toastService.mostrar('Erro ao realizar reserva: ' + err.error.message,'erro');
        this.enviando = false;
      }
    });
  }
  carregarCategorias(): void {
    this.equipamentoService.listarCategorias().subscribe({
      next: (pagina: Page<CategoriariaRetornoDTO>) => {
        this.categorias = pagina.content;
      },
      error: (err) => console.error('Erro ao buscar categorias', err)
    });
  }
}
