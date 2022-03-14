import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITextLocalization, TextLocalization } from '../text-localization.model';

import { TextLocalizationService } from './text-localization.service';

describe('TextLocalization Service', () => {
  let service: TextLocalizationService;
  let httpMock: HttpTestingController;
  let elemDefault: ITextLocalization;
  let expectedResult: ITextLocalization | ITextLocalization[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TextLocalizationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
      message: 'AAAAAAA',
      locale: 'AAAAAAA',
      createdBy: 'AAAAAAA',
      createdDate: currentDate,
      lastModifiedBy: 'AAAAAAA',
      lastModifiedDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TextLocalization', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new TextLocalization()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TextLocalization', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          message: 'BBBBBB',
          locale: 'BBBBBB',
          createdBy: 'BBBBBB',
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TextLocalization', () => {
      const patchObject = Object.assign(
        {
          code: 'BBBBBB',
          lastModifiedBy: 'BBBBBB',
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new TextLocalization()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TextLocalization', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          message: 'BBBBBB',
          locale: 'BBBBBB',
          createdBy: 'BBBBBB',
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TextLocalization', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTextLocalizationToCollectionIfMissing', () => {
      it('should add a TextLocalization to an empty array', () => {
        const textLocalization: ITextLocalization = { id: 123 };
        expectedResult = service.addTextLocalizationToCollectionIfMissing([], textLocalization);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(textLocalization);
      });

      it('should not add a TextLocalization to an array that contains it', () => {
        const textLocalization: ITextLocalization = { id: 123 };
        const textLocalizationCollection: ITextLocalization[] = [
          {
            ...textLocalization,
          },
          { id: 456 },
        ];
        expectedResult = service.addTextLocalizationToCollectionIfMissing(textLocalizationCollection, textLocalization);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TextLocalization to an array that doesn't contain it", () => {
        const textLocalization: ITextLocalization = { id: 123 };
        const textLocalizationCollection: ITextLocalization[] = [{ id: 456 }];
        expectedResult = service.addTextLocalizationToCollectionIfMissing(textLocalizationCollection, textLocalization);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(textLocalization);
      });

      it('should add only unique TextLocalization to an array', () => {
        const textLocalizationArray: ITextLocalization[] = [{ id: 123 }, { id: 456 }, { id: 60771 }];
        const textLocalizationCollection: ITextLocalization[] = [{ id: 123 }];
        expectedResult = service.addTextLocalizationToCollectionIfMissing(textLocalizationCollection, ...textLocalizationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const textLocalization: ITextLocalization = { id: 123 };
        const textLocalization2: ITextLocalization = { id: 456 };
        expectedResult = service.addTextLocalizationToCollectionIfMissing([], textLocalization, textLocalization2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(textLocalization);
        expect(expectedResult).toContain(textLocalization2);
      });

      it('should accept null and undefined values', () => {
        const textLocalization: ITextLocalization = { id: 123 };
        expectedResult = service.addTextLocalizationToCollectionIfMissing([], null, textLocalization, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(textLocalization);
      });

      it('should return initial array if no TextLocalization is added', () => {
        const textLocalizationCollection: ITextLocalization[] = [{ id: 123 }];
        expectedResult = service.addTextLocalizationToCollectionIfMissing(textLocalizationCollection, undefined, null);
        expect(expectedResult).toEqual(textLocalizationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
