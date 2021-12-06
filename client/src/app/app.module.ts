import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// components
import { AppComponent } from './app.component';

// modules
import { AppRoutingModule } from './app-routing.module';
import { SharedModule } from './shared/shared.module';
import { HeaderComponent } from './component/page/header/header.component';
import { HeaderUserMenuComponent } from './component/container/header-user-menu/header-user-menu.component';
import { ControlPanelComponent } from './component/container/control-panel/control-panel.component';
import { HomeComponent } from './component/page/home/home.component';
import { RankingComponent } from './component/page/ranking/ranking.component';
import { RankingTableComponent } from './component/container/ranking-table/ranking-table.component';
import { ProfileComponent } from './component/page/profile/profile.component';
import { SignupComponent } from './component/page/signup/signup.component';
import { LoginComponent } from './component/page/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HeaderUserMenuComponent,
    ControlPanelComponent,
    HomeComponent,
    RankingComponent,
    RankingTableComponent,
    ProfileComponent,
    SignupComponent,
    LoginComponent,
  ],
  imports: [SharedModule, BrowserModule, AppRoutingModule, BrowserAnimationsModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
