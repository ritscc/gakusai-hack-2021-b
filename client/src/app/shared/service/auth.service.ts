import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AccessTokenModel } from 'src/app/model/access-token.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private router: Router,
    private http: HttpClient,
    private cookieService: CookieService
  ) {}

  public logout(): void {
    this.cookieService.delete(environment.CREDENTIALS_KEY);
    this.router.navigate(['/']);
  }

  public getCredentials(): string {
    return this.cookieService.get(environment.CREDENTIALS_KEY);
  }

  public setCredentials(accessToken: AccessTokenModel): void {
    const expiredDate = new Date();
    expiredDate.setDate(expiredDate.getDate() + 7);

    this.cookieService.set(
      environment.CREDENTIALS_KEY,
      `${accessToken.tokenType} ${accessToken.accessToken}`,
      expiredDate
    );
  }

  public checkAuthenticated(): boolean {
    return this.cookieService.check(environment.CREDENTIALS_KEY);
  }
}
