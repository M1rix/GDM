import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICurrency, getCurrencyIdentifier } from '../currency.model';

export type EntityResponseType = HttpResponse<ICurrency>;
export type EntityArrayResponseType = HttpResponse<ICurrency[]>;

@Injectable({ providedIn: 'root' })
export class CurrencyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/currencies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(currency: ICurrency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(currency);
    return this.http
      .post<ICurrency>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(currency: ICurrency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(currency);
    return this.http
      .put<ICurrency>(`${this.resourceUrl}/${getCurrencyIdentifier(currency) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(currency: ICurrency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(currency);
    return this.http
      .patch<ICurrency>(`${this.resourceUrl}/${getCurrencyIdentifier(currency) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICurrency>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICurrency[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCurrencyToCollectionIfMissing(currencyCollection: ICurrency[], ...currenciesToCheck: (ICurrency | null | undefined)[]): ICurrency[] {
    const currencies: ICurrency[] = currenciesToCheck.filter(isPresent);
    if (currencies.length > 0) {
      const currencyCollectionIdentifiers = currencyCollection.map(currencyItem => getCurrencyIdentifier(currencyItem)!);
      const currenciesToAdd = currencies.filter(currencyItem => {
        const currencyIdentifier = getCurrencyIdentifier(currencyItem);
        if (currencyIdentifier == null || currencyCollectionIdentifiers.includes(currencyIdentifier)) {
          return false;
        }
        currencyCollectionIdentifiers.push(currencyIdentifier);
        return true;
      });
      return [...currenciesToAdd, ...currencyCollection];
    }
    return currencyCollection;
  }

  protected convertDateFromClient(currency: ICurrency): ICurrency {
    return Object.assign({}, currency, {
      createdDate: currency.createdDate?.isValid() ? currency.createdDate.toJSON() : undefined,
      lastModifiedDate: currency.lastModifiedDate?.isValid() ? currency.lastModifiedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? dayjs(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((currency: ICurrency) => {
        currency.createdDate = currency.createdDate ? dayjs(currency.createdDate) : undefined;
        currency.lastModifiedDate = currency.lastModifiedDate ? dayjs(currency.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
