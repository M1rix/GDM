import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TextLocalizationComponent } from './list/text-localization.component';
import { TextLocalizationDetailComponent } from './detail/text-localization-detail.component';
import { TextLocalizationUpdateComponent } from './update/text-localization-update.component';
import { TextLocalizationDeleteDialogComponent } from './delete/text-localization-delete-dialog.component';
import { TextLocalizationRoutingModule } from './route/text-localization-routing.module';

@NgModule({
  imports: [SharedModule, TextLocalizationRoutingModule],
  declarations: [
    TextLocalizationComponent,
    TextLocalizationDetailComponent,
    TextLocalizationUpdateComponent,
    TextLocalizationDeleteDialogComponent,
  ],
  entryComponents: [TextLocalizationDeleteDialogComponent],
})
export class TextLocalizationModule {}
