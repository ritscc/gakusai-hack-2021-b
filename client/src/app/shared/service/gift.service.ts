import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { GiftModel, GiftsModel } from 'src/app/model/gift.model';

@Injectable({
  providedIn: 'root',
})
export class GiftService {
  constructor(private http: HttpClient) {}

  getLoginUserGifts(): Observable<GiftModel[]> {
    return this.http
      .get<GiftsModel>(`${environment.API_BASE_URL}/api/users/me/gifts`)
      .pipe(map((response) => response.gifts));
  }
}
