import { Component } from '@angular/core';
import { Router,RouterOutlet } from '@angular/router';
import { fadeAnimation } from './core/layout/animacoes/animacoes';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [fadeAnimation]
})
export class AppComponent {
  title = 'reservasti';

  constructor(public router: Router) {}

  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
  }
}
