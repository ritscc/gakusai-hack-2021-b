import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/service/auth.service';
import { AlertService } from 'src/app/shared/service/alert.service';
import { LoginRequest } from 'src/app/request/login.request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  requestBody = {} as LoginRequest;
  isHidden: boolean = true;

  constructor(
    private router: Router,
    private authService: AuthService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {}

  onSubmit(): void {
    if (!this.requestBody.email || !this.requestBody.password) {
      return;
    }

    this.authService.login(this.requestBody).subscribe(
      (accessToken) => {
        this.authService.setCredentials(accessToken);
        this.alertService.openSnackBar('ログインに成功しました', 'SUCCESS');
        this.router.navigate(['/gifts']);
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
