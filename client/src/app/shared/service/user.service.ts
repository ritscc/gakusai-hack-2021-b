import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { UserModel, UsersModel } from 'src/app/model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  getUsers(): Observable<UserModel[]> {
    return this.http
      .get<UsersModel>(`${environment.API_BASE_URL}/api/users`)
      .pipe(map((users) => users.users));
  }

  getLoginUser(): Observable<UserModel> {
    return this.http.get<UserModel>(`${environment.API_BASE_URL}/api/users/me`);
  }
}
