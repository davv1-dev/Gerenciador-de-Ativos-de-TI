import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FilaGlobalComponent } from './features/chamados/fila-global/fila-global.component';
import { LoginComponent } from './features/autenticacao/login/login.component';
import { FuncionarioCadastroComponent } from './features/funcionarios/funcionario-cadastro/funcionario-cadastro.component';
import { HomeFuncionarioComponent } from './features/portal/home-funcionario/home-funcionario.component';
import { AberturaChamadoComponent } from './features/chamados/abertura-chamado/abertura-chamado.component';
import { NovaReservaComponent } from './features/reservas/nova-reserva/nova-reserva.component';
import { HeaderComponent } from './core/components/header/header.component';
import { HomeTecnicoComponent } from './features/portal/home-tecnico/home-tecnico.component';
import { ChamadoAtualComponent } from './features/chamados/chamado-atual/chamado-atual.component';
import { ToastComponent } from './core/components/toast/toast.component';
import { ConfirmDialogComponent } from './core/components/confirm-dialog/confirm-dialog.component';
import { MinhaFilaComponent } from './features/chamados/minha-fila/minha-fila.component';
import { HomeAdminComponent } from './features/portal/home-admin/home-admin.component';
import { EquipamentoAdminComponent } from './features/equipamento/equipamento-admin/equipamento-admin.component';
import { AlocacaoAtivosComponent } from './features/departamentos/alocacao-ativos/alocacao-ativos.component';
import { DepartamentoComponent } from './features/departamentos/departamento/departamento.component';
import { RelatoriosComponent } from './features/relatorios/relatorios/relatorios.component';
import { GestaoUsuariosComponent } from './features/funcionarios/gestao-usuarios/gestao-usuarios.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { LandingPageComponent } from './features/pagina-boas-vindas/landing-page/landing-page.component';
import { EsqueciSenhaComponent } from './features/pagina-suporte/esqueci-senha/esqueci-senha.component';
import { MinhasReservasComponent } from './features/reservas/minhas-reservas/minhas-reservas.component';



@NgModule({
  declarations: [
    AppComponent,
    FilaGlobalComponent,
    LoginComponent,
    FuncionarioCadastroComponent,
    HomeFuncionarioComponent,
    AberturaChamadoComponent,
    NovaReservaComponent,
    HomeTecnicoComponent,
    ChamadoAtualComponent,
    ToastComponent,
    ConfirmDialogComponent,
    MinhaFilaComponent,
    HomeAdminComponent,
    EquipamentoAdminComponent,
    AlocacaoAtivosComponent,
    DepartamentoComponent,
    RelatoriosComponent,
    GestaoUsuariosComponent,
    LandingPageComponent,
    EsqueciSenhaComponent,
    MinhasReservasComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    HeaderComponent,
    BrowserAnimationsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
