export interface IRetrospectiveType {
  id?: number;
  name?: string;
}

export class RetrospectiveType implements IRetrospectiveType {
  constructor(public id?: number, public name?: string) {}
}
