import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITextLocalization } from '../text-localization.model';
import { TextLocalizationService } from '../service/text-localization.service';

@Component({
  templateUrl: './text-localization-delete-dialog.component.html',
})
export class TextLocalizationDeleteDialogComponent {
  textLocalization?: ITextLocalization;

  constructor(protected textLocalizationService: TextLocalizationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.textLocalizationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
