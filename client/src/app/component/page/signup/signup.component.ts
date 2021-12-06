import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
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
  iconSrc!: SafeUrl;

  constructor(
    private router: Router,
    private authService: AuthService,
    private alertService: AlertService,
    private sanitizer: DomSanitizer
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

  onClickFileSelectButton(event: Event): void {
    (event.target as HTMLInputElement).value = '';
  }

  onSelectFiles(event: Event): void {
    const files: FileList | null = (event.target as HTMLInputElement).files;

    if (files === null) {
      return;
    }

    Array.from(files).forEach((file) => {
      var reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        const result = reader.result as string;
        this.requestBody.icon = result.slice(result.indexOf(',') + 1);
        this.iconSrc = this.sanitizer.bypassSecurityTrustUrl(result);
      };
    });
  }
}
