import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITextLocalization, TextLocalization } from '../text-localization.model';
import { TextLocalizationService } from '../service/text-localization.service';

@Injectable({ providedIn: 'root' })
export class TextLocalizationRoutingResolveService implements Resolve<ITextLocalization> {
  constructor(protected service: TextLocalizationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITextLocalization> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((textLocalization: HttpResponse<TextLocalization>) => {
          if (textLocalization.body) {
            return of(textLocalization.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TextLocalization());
  }
}
