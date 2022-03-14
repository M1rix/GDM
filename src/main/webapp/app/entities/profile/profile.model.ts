import dayjs from 'dayjs/esm';
import { IDebt } from 'app/entities/debt/debt.model';
import { ProfileStatus } from 'app/entities/enumerations/profile-status.model';

export interface IProfile {
  id?: number;
  passport?: string | null;
  jshshir?: string | null;
  inn?: string | null;
  phone?: string | null;
  accessToken?: string;
  firstName?: string | null;
  lastName?: string | null;
  score?: number | null;
  status?: ProfileStatus;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  debts?: IDebt[] | null;
}

export class Profile implements IProfile {
  constructor(
    public id?: number,
    public passport?: string | null,
    public jshshir?: string | null,
    public inn?: string | null,
    public phone?: string | null,
    public accessToken?: string,
    public firstName?: string | null,
    public lastName?: string | null,
    public score?: number | null,
    public status?: ProfileStatus,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public debts?: IDebt[] | null
  ) {}
}

export function getProfileIdentifier(profile: IProfile): number | undefined {
  return profile.id;
}
