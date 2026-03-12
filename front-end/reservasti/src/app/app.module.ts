import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FilaGlobalComponent } from './features/chamados/fila-global/fila-global.component';
import { LoginComponent } from './features/autenticacao/login/login.component';
import { FuncionarioCadastroComponent } from './features/funcionarios/funcionario-cadastro/funcionario-cadastro.component';
import { HomeFuncionarioComponent } from './features/portal/home-funcionario/home-funcionario.component';
import { AberturaChamadoComponent } from './features/chamados/abertura-chamado/abertura-chamado.component';
import { NovaReservaComponent } from './features/reservas/nova-reserva/nova-reserva.component';
import { HeaderComponent } from './core/components/header/header.component';


@NgModule({
  declarations: [
    AppComponent,
    FilaGlobalComponent,
    LoginComponent,
    FuncionarioCadastroComponent,
    HomeFuncionarioComponent,
    AberturaChamadoComponent,
    NovaReservaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    HeaderComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
