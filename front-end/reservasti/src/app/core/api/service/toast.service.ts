import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface ToastMessage {
  mensagem: string;
  tipo: 'sucesso' | 'erro' | 'info';
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toastSubject = new Subject<ToastMessage>();
  toastState$ = this.toastSubject.asObservable();

  mostrar(mensagem: string, tipo: 'sucesso' | 'erro' | 'info' = 'info') {
    this.toastSubject.next({ mensagem, tipo });
  }
}
