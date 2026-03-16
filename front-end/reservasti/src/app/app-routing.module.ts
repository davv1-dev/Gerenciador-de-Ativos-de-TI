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
import { DepartamentoComponent } from './features/departamentos/departamento/departamento.component';
import { RelatoriosComponent } from './features/relatorios/relatorios/relatorios.component';
import { GestaoUsuariosComponent } from './features/funcionarios/gestao-usuarios/gestao-usuarios.component';
import { AuthGuard } from './core/guards/auth.guard';
import { LandingPageComponent } from './features/pagina-boas-vindas/landing-page/landing-page.component';
import { EsqueciSenhaComponent } from './features/pagina-suporte/esqueci-senha/esqueci-senha.component';


const routes: Routes = [
  { path: '', redirectTo: '/boas-vindas', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, data: { animation: 'LoginPage' } },
  { path: 'boas-vindas', component: LandingPageComponent, data: { animation: 'BoasVindasPage' } },
  { path: 'cadastro', component: FuncionarioCadastroComponent, data: { animation: 'CadastroPage' } },
  { path: 'esqueci-senha', component: EsqueciSenhaComponent, data:{animation:"EsqueciSenhaPage"} },

  { path: 'home', component: HomeFuncionarioComponent, canActivate: [AuthGuard], data: { animation: 'HomePage' } },
  { path: 'fazerchamado', component: AberturaChamadoComponent, canActivate: [AuthGuard], data: { animation: 'ChamadoPage' } },
  { path: 'novareserva', component: NovaReservaComponent, canActivate: [AuthGuard], data: { animation: 'ReservaPage' } },

  { path: 'home-tecnico', component: HomeTecnicoComponent, canActivate: [AuthGuard], data: { animation: 'HomeTecnicoPage' } },
  { path: 'tecnico/fila', component: FilaGlobalComponent, canActivate: [AuthGuard], data: { animation: 'FilaPage' } },
  { path: 'tecnico/chamado-atual', component: ChamadoAtualComponent, canActivate: [AuthGuard], data: { animation: 'ChamadoAtualPage' } },
  { path: 'tecnico/minha-fila', component: MinhaFilaComponent, canActivate: [AuthGuard], data: { animation: 'MinhaFilaPage' } },

  { path: 'home-admin', component: HomeAdminComponent, canActivate: [AuthGuard], data: { animation: 'HomeAdminPage' } },
  { path: 'admin/equipamentos', component: EquipamentoAdminComponent, canActivate: [AuthGuard], data: { animation: 'AdminEquipamentosPage' } },
  { path: 'admin/alocacao', component: AlocacaoAtivosComponent, canActivate: [AuthGuard], data: { animation: 'AlocacaoAtivosPage' } },
  { path: 'admin/departamentos', component: DepartamentoComponent, canActivate: [AuthGuard], data: { animation: 'DepartamentosPage' } },
  { path: 'admin/relatorios', component: RelatoriosComponent, canActivate: [AuthGuard], data: { animation: 'RelatoriosPage' } },
  { path: 'admin/usuarios', component: GestaoUsuariosComponent, canActivate: [AuthGuard], data: { animation: 'GestaoUsuariosPage' } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
