import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/shared/service/user.service';
import { AlertService } from 'src/app/shared/service/alert.service';
import { NotificationModel } from 'src/app/model/notification.model';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css'],
})
export class NotificationsComponent implements OnInit {
  notifications: NotificationModel[] = [];

  constructor(private userService: UserService, private alertService: AlertService) {}

  ngOnInit(): void {
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
