import { Component, ElementRef, ViewChild, AfterViewInit, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd,NavigationStart } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { HttpClient } from '@angular/common/http';
// import { environment } from 'src/environments/environment'; // Descomente se for usar
import { AuthService } from '../../../core/service/auth.service';
import { Location } from '@angular/common'; // 👈 NOVO IMPORT AQUI

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class HeaderComponent implements OnInit, AfterViewInit, OnDestroy {

  isAdmin: boolean = false;
  isTecnico: boolean = false;
  menuAberto: boolean = false;

  // 👇 NOVA VARIÁVEL: Controla se o header aparece ou não
  mostrarHeader: boolean = true;
  // 👇 LISTA DE ROTAS INVISÍVEIS: Coloque aqui as rotas onde o header NÃO deve aparecer
  rotasSemHeader: string[] = ['/boas-vindas', '/login'];
  @ViewChild('navMenu') navMenu!: ElementRef;
  private routerSub!: Subscription;

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,
    private location: Location
  ) {}

 ngOnInit(): void {
    this.sincronizarPerfil();

    // 🔥 ABORDAGEM AGRESSIVA 1: Usar o Location para pegar a URL física exata do browser antes de qualquer roteamento do Angular
    this.verificarVisibilidadeHeader(this.location.path());

    // 🔥 ABORDAGEM AGRESSIVA 2: Escutar tanto o Start quanto o End do roteamento
    this.routerSub = this.router.events.pipe(
      filter(event => event instanceof NavigationStart || event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      // Se for Start, usa a URL para onde ele está indo. Se for End, usa a URL final consolidada.
      const urlAlvo = event instanceof NavigationStart ? event.url : event.urlAfterRedirects;
      this.verificarVisibilidadeHeader(urlAlvo);
    });
  }

  // Criei esta função para não repetirmos código
  verificarVisibilidadeHeader(url: string): void {
    // 1. Pega a URL limpa (sem os ?filtros). Se vier undefined ou vazia, forçamos a ser '/'
    let rotaAtual = url ? url.split('?')[0] : '/';

    if (rotaAtual === '') {
      rotaAtual = '/'; // Tratamento seguro para quando o Angular iniciar na raiz
    }

    // 2. Verificação EXATA. O header só some se a rota for exatamente '/' ou '/login'
    this.mostrarHeader = !this.rotasSemHeader.includes(rotaAtual);

    // 3. Centraliza a aba (se o header estiver visível)
    if (this.mostrarHeader) {
      this.centralizarAbaAtiva();
    }
  }

  sincronizarPerfil(): void {
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    this.isAdmin = tipoUsuario === 'ADMIN';
    this.isTecnico = tipoUsuario === 'TECNICO';
  }

  toggleMenu(event: Event): void {
    event.stopPropagation();
    this.menuAberto = !this.menuAberto;
  }

  @HostListener('document:click', ['$event'])
  fecharMenu(event: Event): void {
    this.menuAberto = false;
  }

  irParaPerfil(): void {
    console.log('Indo para o perfil... (A implementar)');
    this.menuAberto = false;
  }

  fazerLogout(): void {
    this.authService.logoutBackend().subscribe({
      next: () => {
        this.finalizarSessaoFront();
      },
      error: (erro) => {
        console.warn('O Back-end deu erro no logout, mas vamos deslogar do Front mesmo assim.', erro);
        this.finalizarSessaoFront();
      }
    });
  }

  private finalizarSessaoFront(): void {
    sessionStorage.clear();
    this.menuAberto = false;
    // Quando navegar pro login, a nossa nova lógica no ngOnInit vai esconder o header automaticamente!
    this.router.navigate(['/login']);
  }

  ngAfterViewInit(): void {
    if (this.mostrarHeader) {
      this.centralizarAbaAtiva();
    }
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
