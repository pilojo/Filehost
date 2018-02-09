import { Component } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition,
  keyframes
} from '@angular/animations';
import { HomeComponent } from './home/home.component';
import { AccountComponent } from './account/account.component';
import { LoginComponent } from './login/login.component';
import { UserSearchComponent } from './userSearch/userSearch.component';
import { FileSearchComponent } from './fileSearch/fileSearch.component';
import { SignUpComponent } from './signup/signup.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  animations: [
    trigger('menuState', [
      state('inactive', style({
        display: 'none'
      })),
      state('active',   style({
        display: 'block'
      })),
      transition('inactive => active', animate('0s ease-in')),
      transition('active => inactive', animate('0ms ease-out'))
    ])
  ]
})

export class AppComponent {
  state = 'inactive';
  reverseState = 'active';
  logedIn = false;
  animateMenu() {
        this.state = (this.state === 'active' ? 'inactive' : 'active');
        this.reverseState = (this.state === 'active' ? 'inactive' : 'active');
  }
}
