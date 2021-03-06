import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/shared/service/user.service';
import { AlertService } from 'src/app/shared/service/alert.service';
import { UserModel } from 'src/app/model/user.model';
import { NotificationModel } from 'src/app/model/notification.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  loginUser!: UserModel;
  notifications: NotificationModel[] = [];

  constructor(private userService: UserService, private alertService: AlertService) {}

  ngOnInit(): void {
    this.userService.getLoginUser().subscribe(
      (loginUser) => {
        this.loginUser = loginUser;
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );

    this.userService.getNotifications().subscribe(
      (notifications) => {
        this.notifications = notifications;
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
