import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AlertService } from 'src/app/shared/service/alert.service';
import { UserService } from 'src/app/shared/service/user.service';
import { GiftService } from 'src/app/shared/service/gift.service';
import { UserModel } from 'src/app/model/user.model';
import { GiftShareRequest } from 'src/app/request/gift-share.request';

@Component({
  selector: 'app-share-gift',
  templateUrl: './share-gift.component.html',
  styleUrls: ['./share-gift.component.css'],
})
export class ShareGiftComponent implements OnInit {
  loginUser!: UserModel;
  users!: UserModel[];
  sharedUserId!: number;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { giftId: number },
    private matDialogRef: MatDialogRef<ShareGiftComponent>,
    private alertService: AlertService,
    private userService: UserService,
    private giftService: GiftService
  ) {}

  ngOnInit(): void {
    this.userService.getLoginUser().subscribe(
      (loginUser) => {
        this.loginUser = loginUser;

        // ユーザ一覧を取得
        this.userService.getUsers().subscribe(
          (users) => {
            this.users = users.filter((user) => user.id !== this.loginUser.id);
          },
          (error) => {
            this.matDialogRef.close('ERROR');
            this.alertService.openSnackBar(error, 'ERROR');
          }
        );
      },
      (error) => {
        this.matDialogRef.close('ERROR');
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }

  onClickShareButton() {
    const requestBody: GiftShareRequest = {
      giftId: this.data.giftId,
    };

    this.giftService.shareGift(this.sharedUserId, requestBody).subscribe(
      (_) => {
        this.matDialogRef.close('OK');
        this.alertService.openSnackBar('ギフトをおすそわけしました', 'SUCCESS');
      },
      (error) => {
        this.matDialogRef.close('ERROR');
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }

  onClickUser(userId: number) {
    this.sharedUserId = userId;
  }
}
