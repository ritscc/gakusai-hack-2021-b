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

@NgModule({
  declarations: [AppComponent, HeaderComponent, HeaderUserMenuComponent, ControlPanelComponent],
  imports: [SharedModule, BrowserModule, AppRoutingModule, BrowserAnimationsModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
