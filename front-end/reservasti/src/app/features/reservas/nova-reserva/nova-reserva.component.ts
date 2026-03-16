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

  paginaAtual: number = 0;
  totalPaginas: number = 0;
  constructor(
    private equipamentoService: EquipamentoService,
    private reservaService: ReservaService,
    private fb: FormBuilder,
    private router: Router,
    private toastService: ToastService
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

  buscarEquipamentos(pagina: number = 0): void {
    this.carregandoCatalogo = true;
    this.paginaAtual = pagina;

     const idCategoria = this.categoriaSelecionada ? Number(this.categoriaSelecionada) : undefined;

    this.equipamentoService.listarCatalogo(this.termoBusca, idCategoria, this.paginaAtual, 10).subscribe({
      next: (paginaResposta) => {

        this.equipamentos = paginaResposta.content.filter(eqp => eqp.status === 'DISPONIVEL');

        this.totalPaginas = paginaResposta.totalPages;
        this.carregandoCatalogo = false;
      },
      error: (err) => {
        console.error('Erro ao buscar catálogo', err);
        this.carregandoCatalogo = false;
      }
    });
  }
  mudarPagina(novaPagina: number): void {
    if (novaPagina >= 0 && novaPagina < this.totalPaginas) {
      this.buscarEquipamentos(novaPagina);
    }
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

    const novaReserva: ReservaDTO = {
      equipamentoId: this.equipamentoSelecionado.id,
      dataPrevistaRetirada: this.reservaForm.value.dataPrevistaRetirada,
      dataPrevistaDevolucao: this.reservaForm.value.dataPrevistaDevolucao
    };

    this.reservaService.abrirReserva(novaReserva).subscribe({
      next: () => {
        this.toastService.mostrar('Reserva solicitada com sucesso!', 'sucesso');
        this.voltarParaHome();
      },
      error: (err) => {
        this.toastService.mostrar('Erro ao realizar reserva: ' + err.error.message, 'erro');
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

  voltarParaHome(): void {
    const tipoUsuario: string = sessionStorage.getItem('tipoUsuario') || '';

    if (tipoUsuario === 'ADMIN') {
      this.router.navigate(['/home-admin']);
    } else if (tipoUsuario === 'TECNICO') {
      this.router.navigate(['/home-tecnico']);
    } else {
      this.router.navigate(['/home']);
    }
  }

  aplicarFiltros(): void {
    this.buscarEquipamentos(0);
  }
}
