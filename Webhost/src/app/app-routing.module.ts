import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { AccountComponent } from './account/account.component';
import { LoginComponent } from './login/login.component';
import { UserSearchComponent } from './userSearch/userSearch.component';
import { FileSearchComponent } from './fileSearch/fileSearch.component';
import { SignUpComponent } from './signup/signup.component';


const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'account',
    component: AccountComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'userSearch',
    component: UserSearchComponent
  },
  {
    path: 'fileSearch',
    component: FileSearchComponent
  },
  {
    path: 'signup',
    component: SignUpComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
