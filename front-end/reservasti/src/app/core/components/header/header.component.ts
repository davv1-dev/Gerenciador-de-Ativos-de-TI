import { Component, ElementRef, ViewChild, AfterViewInit, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { AuthService } from '../../../core/service/auth.service'; // Ajuste o caminho se necessário

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class HeaderComponent implements OnInit, AfterViewInit, OnDestroy {

  // 👇 Deixamos sem valor inicial, vamos preencher dinamicamente!
  isAdmin: boolean = false;
  isTecnico: boolean = false;

  // Variável para controlar o abre/fecha do menu do perfil
  menuAberto: boolean = false;

  @ViewChild('navMenu') navMenu!: ElementRef;
  private routerSub!: Subscription;

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.sincronizarPerfil(); // 👈 Chama a função para ler quem está logado

    this.routerSub = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.centralizarAbaAtiva();
    });
  }

  // Lógica para definir o que vai aparecer no header baseado no token/sessionStorage
  sincronizarPerfil(): void {
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    this.isAdmin = tipoUsuario === 'ADMIN';
    this.isTecnico = tipoUsuario === 'TECNICO';
  }

  // Lógica do Menu do Avatar
  toggleMenu(event: Event): void {
    event.stopPropagation(); // Evita que o clique feche o menu na mesma hora
    this.menuAberto = !this.menuAberto;
  }

  // Fecha o menu se clicar em qualquer outro lugar da tela (UX perfeita!)
  @HostListener('document:click', ['$event'])
  fecharMenu(event: Event): void {
    this.menuAberto = false;
  }

  irParaPerfil(): void {
    // Stub para o futuro
    console.log('Indo para o perfil... (A implementar)');
    this.menuAberto = false;
  }

  fazerLogout(): void {
    // Chama o service para fazer o trabalho sujo
    this.authService.logoutBackend().subscribe({
      next: () => {
        this.menuAberto = false; // Fecha o balãozinho
        this.authService.encerrarSessaoLocal(); // Limpa o Front
      },
      error: (erro) => {
        console.warn('O Back-end deu erro no logout, mas vamos deslogar do Front mesmo assim.', erro);
        this.menuAberto = false;
        this.authService.encerrarSessaoLocal();
      }
    });
  }

  private finalizarSessaoFront(): void {
    // 2. Limpa todos os dados do navegador
    sessionStorage.clear();
    // 3. Fecha o menu e manda pro login
    this.menuAberto = false;
    this.router.navigate(['/login']);
  }

  ngAfterViewInit(): void {
    this.centralizarAbaAtiva();
  }

  ngOnDestroy(): void {
    if (this.routerSub) {
      this.routerSub.unsubscribe();
    }
  }

  centralizarAbaAtiva(event?: MouseEvent): void {
    setTimeout(() => {
      let elementoAlvo: HTMLElement | null = null;
      if (event && (event.target as HTMLElement).classList.contains('nav-link')) {
        elementoAlvo = event.target as HTMLElement;
      }
      else if (this.navMenu) {
        elementoAlvo = this.navMenu.nativeElement.querySelector('.ativo');
      }

      if (elementoAlvo) {
        elementoAlvo.scrollIntoView({
          behavior: 'smooth',
          inline: 'center',
          block: 'nearest'
        });
      }
    }, 50);
  }
}
