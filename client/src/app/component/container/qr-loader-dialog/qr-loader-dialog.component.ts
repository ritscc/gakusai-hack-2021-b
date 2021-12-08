import { Component, OnInit, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AlertService } from 'src/app/shared/service/alert.service';
import { GiftService } from 'src/app/shared/service/gift.service';

@Component({
  selector: 'app-qr-loader-dialog',
  templateUrl: './qr-loader-dialog.component.html',
  styleUrls: ['./qr-loader-dialog.component.css'],
})
export class QrLoaderDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private matDialogRef: MatDialogRef<QrLoaderDialogComponent>,
    private router: Router,
    private alertService: AlertService,
    private giftService: GiftService
  ) {}

  ngOnInit(): void {}

  scanSuccessHandler(content: string) {
    const giftId = Number(content);
    if (isNaN(giftId)) {
      this.matDialogRef.close('ERROR');
      this.alertService.openSnackBar('そのQRコードは読み取れません', 'ERROR');
      return;
    }

    this.giftService.obtainGift(giftId).subscribe(
      (_) => {
        this.matDialogRef.close('OK');
        this.alertService.openSnackBar('ギフトを獲得しました', 'SUCCESS');
        this.router.navigate(['/gifts']);
      },
      (error) => {
        this.matDialogRef.close('ERROR');
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
