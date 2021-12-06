import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-user-menu',
  templateUrl: './header-user-menu.component.html',
  styleUrls: ['./header-user-menu.component.css'],
})
export class HeaderUserMenuComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  goProfilePage() {
    this.router.navigate(['/profile']);
  }

  logout() {
    // TODO: ログアウト処理
    this.router.navigate(['/']);
  }
}
