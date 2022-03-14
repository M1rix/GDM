import dayjs from 'dayjs/esm';
import { IDebt } from 'app/entities/debt/debt.model';
import { CurrencyStatus } from 'app/entities/enumerations/currency-status.model';

export interface ICurrency {
  id?: number;
  code?: string;
  countryFlag?: string;
  position?: number | null;
  status?: CurrencyStatus;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  debts?: IDebt[] | null;
}

export class Currency implements ICurrency {
  constructor(
    public id?: number,
    public code?: string,
    public countryFlag?: string,
    public position?: number | null,
    public status?: CurrencyStatus,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public debts?: IDebt[] | null
  ) {}
}

export function getCurrencyIdentifier(currency: ICurrency): number | undefined {
  return currency.id;
}
