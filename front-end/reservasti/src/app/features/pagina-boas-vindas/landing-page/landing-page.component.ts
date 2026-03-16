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
      imagem: 'https://images.unsplash.com/photo-1588508065123-287b28e013da?q=80&w=1920&auto=format&fit=crop',
      titulo: 'Gestão e Reservas Inteligentes',
      descricao: 'Encontre, reserve e aloque equipamentos em questão de segundos. O nosso motor de busca inteligente e os filtros avançados colocam o inventário inteiro na ponta dos seus dedos.'
    },
    {
      imagem: 'https://images.unsplash.com/photo-1531482615713-2afd69097998?q=80&w=1920&auto=format&fit=crop',
      titulo: 'Help Desk Integrado',
      descricao: 'Esqueça as planilhas perdidas. Os usuários abrem chamados rapidamente e nossos técnicos gerenciam filas globais e individuais para garantir que nenhum equipamento fique parado.'
    },
    {
      imagem: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=1920&auto=format&fit=crop',
      titulo: 'Controle Total e Simulações',
      descricao: 'Saiba exatamente onde está cada ativo. Simule expansões de departamentos e descubra imediatamente se o seu estoque atual suporta o crescimento, evitando compras desnecessárias.'
    },
    {
      imagem: 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?q=80&w=1920&auto=format&fit=crop',
      titulo: 'Infraestrutura Visível',
      descricao: 'Desenhado para o setor de TI moderno. Acompanhe o ciclo de vida dos ativos desde a aquisição até o descarte, com uma interface fluida e altamente intuitiva.'
    }
  ];

  constructor(private router: Router) { }

  ngOnInit(): void {}

  irParaLogin(): void {
    this.router.navigate(['/login']);
  }
}
