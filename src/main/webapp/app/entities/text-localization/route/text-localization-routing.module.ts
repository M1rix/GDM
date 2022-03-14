import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TextLocalizationComponent } from '../list/text-localization.component';
import { TextLocalizationDetailComponent } from '../detail/text-localization-detail.component';
import { TextLocalizationUpdateComponent } from '../update/text-localization-update.component';
import { TextLocalizationRoutingResolveService } from './text-localization-routing-resolve.service';

const textLocalizationRoute: Routes = [
  {
    path: '',
    component: TextLocalizationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TextLocalizationDetailComponent,
    resolve: {
      textLocalization: TextLocalizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TextLocalizationUpdateComponent,
    resolve: {
      textLocalization: TextLocalizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TextLocalizationUpdateComponent,
    resolve: {
      textLocalization: TextLocalizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(textLocalizationRoute)],
  exports: [RouterModule],
})
export class TextLocalizationRoutingModule {}
