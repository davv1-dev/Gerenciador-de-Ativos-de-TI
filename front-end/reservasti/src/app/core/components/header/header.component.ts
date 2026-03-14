import { Component, ElementRef, ViewChild, AfterViewInit, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { filter, Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class HeaderComponent implements OnInit, AfterViewInit, OnDestroy {

  isAdmin: boolean = true;
  isTecnico: boolean = false;

  @ViewChild('navMenu') navMenu!: ElementRef;

  // Variável para guardar a nossa "escuta" e evitar vazamento de memória
  private routerSub!: Subscription;

  constructor(private router: Router) {}

  ngOnInit(): void {
    // 1. O Header começa a escutar TODAS as mudanças de rota do sistema
    this.routerSub = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd) // Filtra para avisar só quando a navegação terminar
    ).subscribe(() => {
      // 2. Quando a rota mudar, manda a roleta se alinhar sozinha!
      this.centralizarAbaAtiva();
    });
  }

  ngAfterViewInit(): void {
    this.centralizarAbaAtiva();
  }

  ngOnDestroy(): void {
    // Boa prática: limpa a escuta se o header for destruído
    if (this.routerSub) {
      this.routerSub.unsubscribe();
    }
  }

  centralizarAbaAtiva(event?: MouseEvent): void {
    // O setTimeout garante que o Angular já aplicou a classe 'ativo' no novo link
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
