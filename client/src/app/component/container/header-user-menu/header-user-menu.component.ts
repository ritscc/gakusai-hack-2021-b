import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/service/auth.service';

@Component({
  selector: 'app-header-user-menu',
  templateUrl: './header-user-menu.component.html',
  styleUrls: ['./header-user-menu.component.css'],
})
export class HeaderUserMenuComponent implements OnInit {
  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {}

  goProfilePage() {
    this.router.navigate(['/profile']);
  }

  logout() {
    this.authService.logout();
  }
}
