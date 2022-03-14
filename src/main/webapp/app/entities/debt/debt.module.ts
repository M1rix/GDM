import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DebtComponent } from './list/debt.component';
import { DebtDetailComponent } from './detail/debt-detail.component';
import { DebtUpdateComponent } from './update/debt-update.component';
import { DebtDeleteDialogComponent } from './delete/debt-delete-dialog.component';
import { DebtRoutingModule } from './route/debt-routing.module';

@NgModule({
  imports: [SharedModule, DebtRoutingModule],
  declarations: [DebtComponent, DebtDetailComponent, DebtUpdateComponent, DebtDeleteDialogComponent],
  entryComponents: [DebtDeleteDialogComponent],
})
export class DebtModule {}
