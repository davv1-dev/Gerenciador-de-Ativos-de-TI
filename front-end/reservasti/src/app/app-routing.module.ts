import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FilaGlobalComponent } from './features/chamados/fila-global/fila-global.component';
import { LoginComponent } from './features/autenticacao/login/login.component';
import { FuncionarioCadastroComponent } from './features/funcionarios/funcionario-cadastro/funcionario-cadastro.component';
import { HomeFuncionarioComponent } from './features/portal/home-funcionario/home-funcionario.component';
import { AberturaChamadoComponent } from './features/chamados/abertura-chamado/abertura-chamado.component';
import { NovaReservaComponent } from './features/reservas/nova-reserva/nova-reserva.component';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },

  { path: 'fila', component: FilaGlobalComponent },

  { path: 'cadastro-funcionario', component:FuncionarioCadastroComponent },

  { path: 'home', component:HomeFuncionarioComponent},

  { path: 'fazerchamado', component:AberturaChamadoComponent},

  { path: 'novareserva', component: NovaReservaComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
