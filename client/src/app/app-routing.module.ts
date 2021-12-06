import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// components
import { HomeComponent } from './component/page/home/home.component';
import { HeaderComponent } from './component/page/header/header.component';
import { RankingComponent } from './component/page/ranking/ranking.component';
import { ProfileComponent } from './component/page/profile/profile.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: '',
    component: HeaderComponent,
    children: [
      {
        path: 'ranking',
        component: RankingComponent,
      },
      {
        path: 'profile',
        component: ProfileComponent,
      },
    ],
  },
  {
    path: 'login',
    component: HeaderComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
