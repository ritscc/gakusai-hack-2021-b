import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { GiftService } from 'src/app/shared/service/gift.service';
import { UserService } from 'src/app/shared/service/user.service';
import { AlertService } from 'src/app/shared/service/alert.service';
import { GiftModel } from 'src/app/model/gift.model';
import { UserModel } from 'src/app/model/user.model';
import { ShareGiftComponent } from 'src/app/component/container/share-gift/share-gift.component';

@Component({
  selector: 'app-gifts',
  templateUrl: './gifts.component.html',
  styleUrls: ['./gifts.component.css'],
})
export class GiftsComponent implements OnInit {
  loginUser!: UserModel;
  gifts: GiftModel[] = [];

  constructor(
    private matDialog: MatDialog,
    private giftService: GiftService,
    private userService: UserService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.userService.getLoginUser().subscribe(
      (loginUser) => {
        this.loginUser = loginUser;
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );

    this.getLoginUserGifts();
  }

  getLoginUserGifts() {
    this.giftService.getLoginUserGifts().subscribe(
      (gifts) => {
        this.gifts = gifts;
        this.gifts.sort((a, b) => {
          if (a.id < b.id) {
            return 1;
          } else {
            return -1;
          }
        });
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }

  onClickSendButton(giftId: number) {
    this.matDialog
      .open(ShareGiftComponent, {
        data: {
          giftId: giftId,
        },
      })
      .afterClosed()
      .subscribe((_) => this.getLoginUserGifts());
  }
}
