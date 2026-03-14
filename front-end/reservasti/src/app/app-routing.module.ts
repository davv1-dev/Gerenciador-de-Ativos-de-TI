import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FilaGlobalComponent } from './features/chamados/fila-global/fila-global.component';
import { LoginComponent } from './features/autenticacao/login/login.component';
import { FuncionarioCadastroComponent } from './features/funcionarios/funcionario-cadastro/funcionario-cadastro.component';
import { HomeFuncionarioComponent } from './features/portal/home-funcionario/home-funcionario.component';
import { AberturaChamadoComponent } from './features/chamados/abertura-chamado/abertura-chamado.component';
import { NovaReservaComponent } from './features/reservas/nova-reserva/nova-reserva.component';
import { HomeTecnicoComponent } from './features/portal/home-tecnico/home-tecnico.component';
import { ChamadoAtualComponent } from './features/chamados/chamado-atual/chamado-atual.component';
import { MinhaFilaComponent } from './features/chamados/minha-fila/minha-fila.component';
import { HomeAdminComponent } from './features/portal/home-admin/home-admin.component';
import { EquipamentoAdminComponent } from './features/equipamento/equipamento-admin/equipamento-admin.component';
import { AlocacaoAtivosComponent } from './features/departamentos/alocacao-ativos/alocacao-ativos.component';
import { animation } from '@angular/animations';
import { DepartamentoComponent } from './features/departamentos/departamento/departamento.component';
import { RelatoriosComponent } from './features/relatorios/relatorios/relatorios.component';
import { GestaoUsuariosComponent } from './features/funcionarios/gestao-usuarios/gestao-usuarios.component';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent, data: { animation: 'LoginPage' } },

  { path: 'fila', component: FilaGlobalComponent, data: { animation: 'FilaPage' } },

  {path: 'cadastro-funcionario', component:FuncionarioCadastroComponent, data: { animation: 'CadastroPage' } },

  {path: 'home',component:HomeFuncionarioComponent, data: { animation: 'HomePage' } },

  {path: 'fazerchamado',component:AberturaChamadoComponent, data: { animation: 'ChamadoPage' } },

  { path: 'novareserva', component: NovaReservaComponent, data: { animation: 'ReservaPage' } },

  { path: 'home-tecnico', component: HomeTecnicoComponent, data: {animation:'HomeTecnicoPage'}},

  { path: 'chamado-atual', component: ChamadoAtualComponent, data: {animation:'ChamadoAtualPage'}},

  { path: 'minha-fila', component: MinhaFilaComponent, data: {animation:'MinhaFilaPage'}},

  { path: 'home-admin', component: HomeAdminComponent, data: {animation:'HomeAdminPage'}},

  { path: 'admin/equipamentos', component: EquipamentoAdminComponent, data: {animation:'AdminEquipamentosPage'}},

  {path: 'admin/alocacao', component: AlocacaoAtivosComponent,data: {animation:'AlocacaoAtivosPage'}},

  {path: 'admin/departamentos', component: DepartamentoComponent,data: {animation:'DepartamentosPage'}},

  {path: 'admin/relatorios', component: RelatoriosComponent, data: {animation:'RelatoriosPage'}},

  {path: 'admin/usuarios', component: GestaoUsuariosComponent, data: {animation:'GestaoUsuariosPage'}}




];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
