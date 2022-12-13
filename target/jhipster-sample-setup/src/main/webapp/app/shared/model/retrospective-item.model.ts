import { IRetrospectiveType } from '@/shared/model/retrospective-type.model';

export interface IRetrospectiveItem {
  id?: number;
  content?: string | null;
  fileContentType?: string | null;
  file?: string | null;
  titel?: string | null;
  retrospectiveType?: IRetrospectiveType | null;
}

export class RetrospectiveItem implements IRetrospectiveItem {
  constructor(
    public id?: number,
    public content?: string | null,
    public fileContentType?: string | null,
    public file?: string | null,
    public titel?: string | null,
    public retrospectiveType?: IRetrospectiveType | null
  ) {}
}
