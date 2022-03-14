import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DebtComponent } from '../list/debt.component';
import { DebtDetailComponent } from '../detail/debt-detail.component';
import { DebtUpdateComponent } from '../update/debt-update.component';
import { DebtRoutingResolveService } from './debt-routing-resolve.service';

const debtRoute: Routes = [
  {
    path: '',
    component: DebtComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DebtDetailComponent,
    resolve: {
      debt: DebtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DebtUpdateComponent,
    resolve: {
      debt: DebtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DebtUpdateComponent,
    resolve: {
      debt: DebtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(debtRoute)],
  exports: [RouterModule],
})
export class DebtRoutingModule {}
