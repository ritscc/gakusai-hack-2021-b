import { Pipe, PipeTransform } from '@angular/core';
import { UserModel } from 'src/app/model/user.model';

@Pipe({
  name: 'username',
})
export class UsernamePipe implements PipeTransform {
  transform(value: UserModel, ...args: unknown[]): string {
    if (value) {
      return `${value.name}`;
    } else {
      return '';
    }
  }
}
