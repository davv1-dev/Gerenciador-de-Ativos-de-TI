import { Component, OnInit } from '@angular/core';
import { ToastService,ToastMessage } from '../../service/toast.service';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent implements OnInit {
  toastAtual: ToastMessage | null = null;
  private timeoutId: any;

  constructor(private toastService: ToastService) {}

  ngOnInit() {
    this.toastService.toastState$.subscribe(toast => {
      this.toastAtual = toast;

      if (this.timeoutId) clearTimeout(this.timeoutId);

      this.timeoutId = setTimeout(() => this.fechar(), 3500);
    });
  }

  fechar() {
    this.toastAtual = null;
  }
}
