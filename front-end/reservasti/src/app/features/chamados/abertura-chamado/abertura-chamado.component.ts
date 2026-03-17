import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ChamadoService } from '../../../core/service/chamado.service';
import { AberturaChamadoDTO } from '../../../core/models/chamado';
import { FuncionarioRetornoDTO } from 'src/app/core/models/funcionario';
import { TecnicoService } from 'src/app/core/service/tecnico.service';
import { ToastService } from 'src/app/core/service/toast.service';

@Component({
  selector: 'app-chamado-abertura',
  templateUrl: './abertura-chamado.component.html',
  styleUrls: ['./abertura-chamado.component.css']
})
export class AberturaChamadoComponent implements OnInit {

  chamadoForm!: FormGroup;
  carregando = false;

  tecnicosOnline: FuncionarioRetornoDTO[] = [];

  tiposDeProblema = [
    { label: 'Hardware (Computador, Mouse, Teclado)', value: 'HARDWARE' },
    { label: 'Software (Sistema Lento, Travando)', value: 'LENTIDAO_SISTEMA' },
    { label: 'Rede / Internet (Sem conexão)', value: 'PROBLEMA_DE_REDE' },
    { label: 'Acesso / Senha', value: 'ACESSO' },
    { label: 'Outros', value: 'OUTROS' }
  ];

  constructor(
    private fb: FormBuilder,
    private chamadoService: ChamadoService,
    private tecnicoService: TecnicoService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario === 'TECNICO') {
      this.toastService.mostrar('Acesso negado. Técnicos não podem abrir chamados por esta tela.', 'erro');
      this.router.navigate(['/home-tecnico']);
      return;
    }

    this.iniciarFormulario();
    this.carregarTecnicos();
  }

  iniciarFormulario(): void {
    this.chamadoForm = this.fb.group({
      equipamentoId: [null],
      tecnicoId: [null],
      tipoProblema: ['', Validators.required],
      localizacao: ['', Validators.required],
      descricaoDetalhada: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  enviarChamado(): void {
    if (this.chamadoForm.valid) {
      this.carregando = true;

      const dto: AberturaChamadoDTO = {
        tecnicoId: null,
        ...this.chamadoForm.value
      };

      this.chamadoService.abrirChamado(dto).subscribe({
        next: (retorno) => {
          this.toastService.mostrar(`Chamado aberto! Posição atual na fila: ${retorno.posicaoFila || 'Calculando...'}`, 'sucesso');
          this.router.navigate(['/home']);
        },
        error: (erro) => {
          console.error(erro);
          this.toastService.mostrar('Erro ao abrir o chamado, tente novamente mais tarde', 'erro');
          this.carregando = false;
        }
      });
    } else {
      this.chamadoForm.markAllAsTouched();
    }
  }

  carregarTecnicos(): void {
    this.tecnicoService.listarTecnicosOnline().subscribe({
      next: (pagina) => {
        this.tecnicosOnline = pagina.content;
      },
      error: (erro) => {
        console.error('Erro ao buscar técnicos online', erro);
      }
    });
  }

  voltar(): void {
    this.router.navigate(['/home']);
  }
}
