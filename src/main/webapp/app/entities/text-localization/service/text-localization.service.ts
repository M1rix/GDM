import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITextLocalization, getTextLocalizationIdentifier } from '../text-localization.model';

export type EntityResponseType = HttpResponse<ITextLocalization>;
export type EntityArrayResponseType = HttpResponse<ITextLocalization[]>;

@Injectable({ providedIn: 'root' })
export class TextLocalizationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/text-localizations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(textLocalization: ITextLocalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(textLocalization);
    return this.http
      .post<ITextLocalization>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(textLocalization: ITextLocalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(textLocalization);
    return this.http
      .put<ITextLocalization>(`${this.resourceUrl}/${getTextLocalizationIdentifier(textLocalization) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(textLocalization: ITextLocalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(textLocalization);
    return this.http
      .patch<ITextLocalization>(`${this.resourceUrl}/${getTextLocalizationIdentifier(textLocalization) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITextLocalization>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITextLocalization[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTextLocalizationToCollectionIfMissing(
    textLocalizationCollection: ITextLocalization[],
    ...textLocalizationsToCheck: (ITextLocalization | null | undefined)[]
  ): ITextLocalization[] {
    const textLocalizations: ITextLocalization[] = textLocalizationsToCheck.filter(isPresent);
    if (textLocalizations.length > 0) {
      const textLocalizationCollectionIdentifiers = textLocalizationCollection.map(
        textLocalizationItem => getTextLocalizationIdentifier(textLocalizationItem)!
      );
      const textLocalizationsToAdd = textLocalizations.filter(textLocalizationItem => {
        const textLocalizationIdentifier = getTextLocalizationIdentifier(textLocalizationItem);
        if (textLocalizationIdentifier == null || textLocalizationCollectionIdentifiers.includes(textLocalizationIdentifier)) {
          return false;
        }
        textLocalizationCollectionIdentifiers.push(textLocalizationIdentifier);
        return true;
      });
      return [...textLocalizationsToAdd, ...textLocalizationCollection];
    }
    return textLocalizationCollection;
  }

  protected convertDateFromClient(textLocalization: ITextLocalization): ITextLocalization {
    return Object.assign({}, textLocalization, {
      createdDate: textLocalization.createdDate?.isValid() ? textLocalization.createdDate.toJSON() : undefined,
      lastModifiedDate: textLocalization.lastModifiedDate?.isValid() ? textLocalization.lastModifiedDate.toJSON() : undefined,
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
      res.body.forEach((textLocalization: ITextLocalization) => {
        textLocalization.createdDate = textLocalization.createdDate ? dayjs(textLocalization.createdDate) : undefined;
        textLocalization.lastModifiedDate = textLocalization.lastModifiedDate ? dayjs(textLocalization.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
