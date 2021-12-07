import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { UserModel, UsersModel } from 'src/app/model/user.model';
import { NotificationModel, NotificationsModel } from 'src/app/model/notification.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  getUsers(): Observable<UserModel[]> {
    return this.http
      .get<UsersModel>(`${environment.API_BASE_URL}/api/users`)
      .pipe(map((response) => response.users));
  }

  getLoginUser(): Observable<UserModel> {
    return this.http.get<UserModel>(`${environment.API_BASE_URL}/api/users/me`);
  }

  getNotifications(): Observable<NotificationModel[]> {
    return this.http
      .get<NotificationsModel>(`${environment.API_BASE_URL}/api/users/me/notifications`)
      .pipe(map((response) => response.notifications));
  }
}
