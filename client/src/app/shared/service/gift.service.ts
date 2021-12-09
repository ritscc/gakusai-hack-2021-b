import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { GiftModel, GiftsModel } from 'src/app/model/gift.model';
import { GiftShareRequest } from 'src/app/request/gift-share.request';

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

  obtainGift(giftId: number): Observable<void> {
    return this.http.post<void>(`${environment.API_BASE_URL}/api/gifts/${giftId}/obtain`, null);
  }

  shareGift(userId: number, requestBody: GiftShareRequest): Observable<void> {
    return this.http.post<void>(
      `${environment.API_BASE_URL}/api/users/${userId}/gifts`,
      requestBody
    );
  }
}
