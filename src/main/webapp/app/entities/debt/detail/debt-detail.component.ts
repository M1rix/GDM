import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDebt } from '../debt.model';

@Component({
  selector: 'jhi-debt-detail',
  templateUrl: './debt-detail.component.html',
})
export class DebtDetailComponent implements OnInit {
  debt: IDebt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ debt }) => {
      this.debt = debt;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
