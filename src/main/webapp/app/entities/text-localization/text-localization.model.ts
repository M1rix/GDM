import dayjs from 'dayjs/esm';

export interface ITextLocalization {
  id?: number;
  code?: string;
  message?: string;
  locale?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export class TextLocalization implements ITextLocalization {
  constructor(
    public id?: number,
    public code?: string,
    public message?: string,
    public locale?: string,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null
  ) {}
}

export function getTextLocalizationIdentifier(textLocalization: ITextLocalization): number | undefined {
  return textLocalization.id;
}
