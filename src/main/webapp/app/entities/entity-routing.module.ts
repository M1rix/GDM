import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'profile',
        data: { pageTitle: 'gdmApp.profile.home.title' },
        loadChildren: () => import('./profile/profile.module').then(m => m.ProfileModule),
      },
      {
        path: 'debt',
        data: { pageTitle: 'gdmApp.debt.home.title' },
        loadChildren: () => import('./debt/debt.module').then(m => m.DebtModule),
      },
      {
        path: 'currency',
        data: { pageTitle: 'gdmApp.currency.home.title' },
        loadChildren: () => import('./currency/currency.module').then(m => m.CurrencyModule),
      },
      {
        path: 'text-localization',
        data: { pageTitle: 'gdmApp.textLocalization.home.title' },
        loadChildren: () => import('./text-localization/text-localization.module').then(m => m.TextLocalizationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
