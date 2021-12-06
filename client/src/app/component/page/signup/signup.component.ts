import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/service/auth.service';
import { AlertService } from 'src/app/shared/service/alert.service';
import { SignUpRequest } from 'src/app/request/signup.request';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  requestBody = {} as SignUpRequest;
  isHidden: boolean = true;

  constructor(
    private router: Router,
    private authService: AuthService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {}

  onSubmit(): void {
    this.authService.signup(this.requestBody).subscribe(
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

  selectIconFile() {
		// TODO
    console.log('アイコン選択モーダルを表示');
  }
}
