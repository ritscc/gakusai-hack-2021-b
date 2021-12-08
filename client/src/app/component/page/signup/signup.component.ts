import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/service/auth.service';
import { AlertService } from 'src/app/shared/service/alert.service';
import { SignUpRequest } from 'src/app/request/signup.request';
import imageCompression from 'browser-image-compression';

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
    if (!this.requestBody.email || !this.requestBody.password || !this.requestBody.name) {
      return;
    }

    this.authService.signup(this.requestBody).subscribe(
      (accessToken) => {
        this.authService.setCredentials(accessToken);
        this.alertService.openSnackBar('サインアップに成功しました', 'SUCCESS');
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

  async onSelectFiles(event: Event) {
    const files: FileList | null = (event.target as HTMLInputElement).files;

    if (!files) {
      return;
    }

    // 1MB以下に圧縮する
    const compressOptions = {
      maxSizeMB: 1,
    };
    const compFile = await imageCompression(files[0], compressOptions);
    const image = await imageCompression.getDataUrlFromFile(compFile);

    this.requestBody.icon = image.slice(image.indexOf(',') + 1);
    this.iconSrc = this.sanitizer.bypassSecurityTrustUrl(image);
  }
}
