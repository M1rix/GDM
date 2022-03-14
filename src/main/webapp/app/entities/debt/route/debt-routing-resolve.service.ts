import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDebt, Debt } from '../debt.model';
import { DebtService } from '../service/debt.service';

@Injectable({ providedIn: 'root' })
export class DebtRoutingResolveService implements Resolve<IDebt> {
  constructor(protected service: DebtService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDebt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((debt: HttpResponse<Debt>) => {
          if (debt.body) {
            return of(debt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Debt());
  }
}
