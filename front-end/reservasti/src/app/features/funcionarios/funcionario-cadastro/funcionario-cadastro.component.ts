import { Page } from 'src/app/core/models/chamado';
import { ToastService } from './../../../core/service/toast.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FuncionarioService } from '../../../core/service/funcionario.service';
import { DepartamentoRetornoDTO } from '../../../core/models/departamento';
import { DepartamentoService } from 'src/app/core/service/departamento.service';
import { Router } from '@angular/router'; // 👈 Adicionamos o Router para redirecionar

@Component({
  selector: 'app-funcionario-cadastro',
  templateUrl: './funcionario-cadastro.component.html',
  styleUrls: ['./funcionario-cadastro.component.css']
})
export class FuncionarioCadastroComponent implements OnInit {

  funcionarioForm!: FormGroup;
  carregando = false;

  private _listaDepartamentos!: Page<DepartamentoRetornoDTO>;

  constructor(
    private fb: FormBuilder,
    private funcionarioService: FuncionarioService,
    private departamentoService: DepartamentoService,
    private toastService: ToastService,
    private router: Router // 👈 Injetado aqui
  ) {}

  ngOnInit(): void {
    this.iniciarFormulario();
    this.carregarDepartamentos();
  }

  get departamentos(): Page<DepartamentoRetornoDTO> {
    return this._listaDepartamentos;
  }

  iniciarFormulario(): void {
    this.funcionarioForm = this.fb.group({
      nomeCompleto: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', Validators.required],
      numeroDeTelefone: ['', [Validators.pattern(/^\d{2} \d{5}-\d{4}$/)]],
      departamentoId: [null, Validators.required],

      endereco: this.fb.group({
        logradouro: ['', Validators.required],
        bairro: ['', Validators.required],
        cep: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
        cidade: ['', Validators.required],
        uf: ['', Validators.required],
        numero: [null],
        complemento: ['']
      })
    });
  }

  salvar(): void {
    if (this.funcionarioForm.valid) {
      this.carregando = true;
      const dto = this.funcionarioForm.value;

      this.funcionarioService.cadastrar(dto).subscribe({
        next: () => {
          // 👇 Mensagem alinhada com a regra de negócio!
          this.toastService.mostrar('Cadastro realizado! Seu acesso foi enviado para aprovação do administrador.', 'sucesso');
          this.funcionarioForm.reset();
          this.carregando = false;

          // 👇 Manda o usuário de volta para o login para aguardar
          this.router.navigate(['/login']);
        },
        error: (erro) => {
          console.error(erro);
          this.toastService.mostrar('Erro ao cadastrar. Verifique os dados ou tente novamente mais tarde.', 'erro');
          this.carregando = false;
        }
      });
    } else {
      this.funcionarioForm.markAllAsTouched();
      this.toastService.mostrar('Por favor, preencha todos os campos obrigatórios corretamente.', 'info');
    }
  }

  carregarDepartamentos(): void {
    this.departamentoService.listarDepartamentos().subscribe({
      next: (dados) => {
        this._listaDepartamentos = dados;
      },
      error: (erro) => {
        console.error('Erro ao buscar departamentos', erro);
        this.toastService.mostrar('Não foi possível carregar a lista de departamentos.', 'info');
      }
    });
  }
}
