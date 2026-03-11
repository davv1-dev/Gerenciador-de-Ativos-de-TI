import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FuncionarioService } from '../../../core/service/funcionario.service';
import { DepartamentoService, DepartamentoDTO } from '../../../core/service/departamento.service';

@Component({
  selector: 'app-funcionario-cadastro',
  templateUrl: './funcionario-cadastro.component.html',
  styleUrls: ['./funcionario-cadastro.component.css']
})
export class FuncionarioCadastroComponent implements OnInit {

  funcionarioForm!: FormGroup;
  carregando = false;

  listaDepartamentos: DepartamentoDTO[] = [];

  constructor(
    private fb: FormBuilder,
    private funcionarioService: FuncionarioService,
    private departamentoService: DepartamentoService
  ) {}

  ngOnInit(): void {
    this.iniciarFormulario();
    this.carregarDepartamentos();
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
        next: (retorno) => {
          console.log('Sucesso! Funcionário cadastrado:', retorno);
          alert('Funcionário cadastrado com sucesso!');
          this.funcionarioForm.reset();
          this.carregando = false;
        },
        error: (erro) => {
          console.error('Erro ao cadastrar', erro);
          alert('Erro ao cadastrar. Verifique o console.');
          this.carregando = false;
        }
      });
    } else {
      this.funcionarioForm.markAllAsTouched();
    }
  }

  carregarDepartamentos(): void {

    this.departamentoService.listarTodos().subscribe({
      next: (dados) => {
        this.listaDepartamentos = dados;
      },
      error: (erro) => {
        console.error('Erro ao buscar departamentos', erro);
        alert('Não foi possível carregar a lista de departamentos.');
      }
    });
  }
}
