import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {

  funcionalidades = [
    {
      imagem: 'https://images.unsplash.com/photo-1519389950473-47ba0277781c?q=80&w=1920&auto=format&fit=crop',
      titulo: 'Rapidez Inigualável',
      descricao: 'Encontre, reserve e aloque equipamentos em questão de segundos. O nosso motor de busca inteligente e os filtros avançados colocam o inventário inteiro na ponta dos seus dedos.'
    },
    {
      imagem: 'https://images.unsplash.com/photo-1561070791-2526d30994b5?q=80&w=1920&auto=format&fit=crop',
      titulo: 'Interface Fluida',
      descricao: 'Desenhado para o utilizador moderno. Esqueça os manuais de instruções complexos; a nossa plataforma é tão intuitiva que a sua equipa será produtiva desde o primeiro clique.'
    },
    {
      imagem: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=1920&auto=format&fit=crop',
      titulo: 'Controle Total e Simulações',
      descricao: 'Saiba exatamente onde está cada ativo. Simule expansões de departamentos e saiba imediatamente se o seu estoque atual suporta o crescimento, evitando compras desnecessárias.'
    }
  ];

  constructor(private router: Router) { }

  ngOnInit(): void {}

  irParaLogin(): void {
    this.router.navigate(['/login']);
  }
}
