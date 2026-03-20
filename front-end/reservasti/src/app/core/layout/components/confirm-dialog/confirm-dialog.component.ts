import { Component, OnInit } from '@angular/core';
import { ConfirmDialogService } from '../../../api/service/confirm-dialog.service';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit {
  dialogData: any = null;

  constructor(private confirmService: ConfirmDialogService) {}

  ngOnInit() {
    this.confirmService.confirmState$.subscribe(data => {
      this.dialogData = data;
    });
  }

  responder(resposta: boolean) {
    if (this.dialogData) {
      this.dialogData.responder(resposta);
    }
  }
}
