import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DebtStatus } from 'app/entities/enumerations/debt-status.model';
import { IDebt, Debt } from '../debt.model';

import { DebtService } from './debt.service';

describe('Debt Service', () => {
  let service: DebtService;
  let httpMock: HttpTestingController;
  let elemDefault: IDebt;
  let expectedResult: IDebt | IDebt[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DebtService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      amount: 0,
      status: DebtStatus.ONGOING,
      dueDate: currentDate,
      description: 'AAAAAAA',
      debtScore: 0,
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
          dueDate: currentDate.format(DATE_TIME_FORMAT),
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

    it('should create a Debt', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dueDate: currentDate.format(DATE_TIME_FORMAT),
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dueDate: currentDate,
          createdDate: currentDate,
          lastModifiedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Debt()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Debt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amount: 1,
          status: 'BBBBBB',
          dueDate: currentDate.format(DATE_TIME_FORMAT),
          description: 'BBBBBB',
          debtScore: 1,
          createdBy: 'BBBBBB',
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dueDate: currentDate,
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

    it('should partial update a Debt', () => {
      const patchObject = Object.assign(
        {
          status: 'BBBBBB',
          dueDate: currentDate.format(DATE_TIME_FORMAT),
          debtScore: 1,
          createdBy: 'BBBBBB',
          createdDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new Debt()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dueDate: currentDate,
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

    it('should return a list of Debt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amount: 1,
          status: 'BBBBBB',
          dueDate: currentDate.format(DATE_TIME_FORMAT),
          description: 'BBBBBB',
          debtScore: 1,
          createdBy: 'BBBBBB',
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dueDate: currentDate,
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

    it('should delete a Debt', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDebtToCollectionIfMissing', () => {
      it('should add a Debt to an empty array', () => {
        const debt: IDebt = { id: 123 };
        expectedResult = service.addDebtToCollectionIfMissing([], debt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(debt);
      });

      it('should not add a Debt to an array that contains it', () => {
        const debt: IDebt = { id: 123 };
        const debtCollection: IDebt[] = [
          {
            ...debt,
          },
          { id: 456 },
        ];
        expectedResult = service.addDebtToCollectionIfMissing(debtCollection, debt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Debt to an array that doesn't contain it", () => {
        const debt: IDebt = { id: 123 };
        const debtCollection: IDebt[] = [{ id: 456 }];
        expectedResult = service.addDebtToCollectionIfMissing(debtCollection, debt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(debt);
      });

      it('should add only unique Debt to an array', () => {
        const debtArray: IDebt[] = [{ id: 123 }, { id: 456 }, { id: 50885 }];
        const debtCollection: IDebt[] = [{ id: 123 }];
        expectedResult = service.addDebtToCollectionIfMissing(debtCollection, ...debtArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const debt: IDebt = { id: 123 };
        const debt2: IDebt = { id: 456 };
        expectedResult = service.addDebtToCollectionIfMissing([], debt, debt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(debt);
        expect(expectedResult).toContain(debt2);
      });

      it('should accept null and undefined values', () => {
        const debt: IDebt = { id: 123 };
        expectedResult = service.addDebtToCollectionIfMissing([], null, debt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(debt);
      });

      it('should return initial array if no Debt is added', () => {
        const debtCollection: IDebt[] = [{ id: 123 }];
        expectedResult = service.addDebtToCollectionIfMissing(debtCollection, undefined, null);
        expect(expectedResult).toEqual(debtCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
