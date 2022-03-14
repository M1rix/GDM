import dayjs from 'dayjs/esm';
import { IProfile } from 'app/entities/profile/profile.model';
import { ICurrency } from 'app/entities/currency/currency.model';
import { DebtStatus } from 'app/entities/enumerations/debt-status.model';

export interface IDebt {
  id?: number;
  amount?: number;
  status?: DebtStatus;
  dueDate?: dayjs.Dayjs;
  description?: string | null;
  debtScore?: number | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  profile?: IProfile | null;
  currency?: ICurrency | null;
}

export class Debt implements IDebt {
  constructor(
    public id?: number,
    public amount?: number,
    public status?: DebtStatus,
    public dueDate?: dayjs.Dayjs,
    public description?: string | null,
    public debtScore?: number | null,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public profile?: IProfile | null,
    public currency?: ICurrency | null
  ) {}
}

export function getDebtIdentifier(debt: IDebt): number | undefined {
  return debt.id;
}
