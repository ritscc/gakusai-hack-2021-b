import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// guards
import { AuthGuard } from 'src/app/shared/guard/auth.guard';

// components
import { HomeComponent } from './component/page/home/home.component';
import { SignupComponent } from './component/page/signup/signup.component';
import { HeaderComponent } from './component/page/header/header.component';
import { RankingComponent } from './component/page/ranking/ranking.component';
import { ProfileComponent } from './component/page/profile/profile.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'signup',
    component: SignupComponent,
  },
  {
    path: '',
    component: HeaderComponent,
    canActivate: [AuthGuard],
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
