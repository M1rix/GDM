import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDebt, getDebtIdentifier } from '../debt.model';

export type EntityResponseType = HttpResponse<IDebt>;
export type EntityArrayResponseType = HttpResponse<IDebt[]>;

@Injectable({ providedIn: 'root' })
export class DebtService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/debts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(debt: IDebt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(debt);
    return this.http
      .post<IDebt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(debt: IDebt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(debt);
    return this.http
      .put<IDebt>(`${this.resourceUrl}/${getDebtIdentifier(debt) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(debt: IDebt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(debt);
    return this.http
      .patch<IDebt>(`${this.resourceUrl}/${getDebtIdentifier(debt) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDebt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDebt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDebtToCollectionIfMissing(debtCollection: IDebt[], ...debtsToCheck: (IDebt | null | undefined)[]): IDebt[] {
    const debts: IDebt[] = debtsToCheck.filter(isPresent);
    if (debts.length > 0) {
      const debtCollectionIdentifiers = debtCollection.map(debtItem => getDebtIdentifier(debtItem)!);
      const debtsToAdd = debts.filter(debtItem => {
        const debtIdentifier = getDebtIdentifier(debtItem);
        if (debtIdentifier == null || debtCollectionIdentifiers.includes(debtIdentifier)) {
          return false;
        }
        debtCollectionIdentifiers.push(debtIdentifier);
        return true;
      });
      return [...debtsToAdd, ...debtCollection];
    }
    return debtCollection;
  }

  protected convertDateFromClient(debt: IDebt): IDebt {
    return Object.assign({}, debt, {
      dueDate: debt.dueDate?.isValid() ? debt.dueDate.toJSON() : undefined,
      createdDate: debt.createdDate?.isValid() ? debt.createdDate.toJSON() : undefined,
      lastModifiedDate: debt.lastModifiedDate?.isValid() ? debt.lastModifiedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dueDate = res.body.dueDate ? dayjs(res.body.dueDate) : undefined;
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? dayjs(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((debt: IDebt) => {
        debt.dueDate = debt.dueDate ? dayjs(debt.dueDate) : undefined;
        debt.createdDate = debt.createdDate ? dayjs(debt.createdDate) : undefined;
        debt.lastModifiedDate = debt.lastModifiedDate ? dayjs(debt.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
