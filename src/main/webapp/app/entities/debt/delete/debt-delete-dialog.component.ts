import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDebt } from '../debt.model';
import { DebtService } from '../service/debt.service';

@Component({
  templateUrl: './debt-delete-dialog.component.html',
})
export class DebtDeleteDialogComponent {
  debt?: IDebt;

  constructor(protected debtService: DebtService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.debtService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
