import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'datetime',
})
export class DatetimePipe implements PipeTransform {
  transform(value: Date, format: string): string {
    return moment(value).format(format);
  }
}
