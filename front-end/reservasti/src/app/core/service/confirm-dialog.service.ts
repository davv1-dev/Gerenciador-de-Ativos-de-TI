import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfirmDialogService {
  private confirmSubject = new Subject<any>();
  confirmState$ = this.confirmSubject.asObservable();

  confirmar(titulo: string, mensagem: string): Promise<boolean> {
    return new Promise((resolve) => {
      this.confirmSubject.next({
        titulo,
        mensagem,
        responder: (resposta: boolean) => {
          resolve(resposta);
          this.fechar();
        }
      });
    });
  }

  private fechar() {
    this.confirmSubject.next(null);
  }
}
