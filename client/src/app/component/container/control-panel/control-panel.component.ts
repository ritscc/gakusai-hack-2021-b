import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-control-panel',
  templateUrl: './control-panel.component.html',
  styleUrls: ['./control-panel.component.css'],
})
export class ControlPanelComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  onClickGiftButton() {
    this.router.navigate(['/gifts']);
  }

  onClickRankingButton() {
    this.router.navigate(['/ranking']);
  }

  onClickNotificationsButton() {
    this.router.navigate(['/notifications']);
  }

  onClickProfileButton() {
    this.router.navigate(['/profile']);
  }
}
