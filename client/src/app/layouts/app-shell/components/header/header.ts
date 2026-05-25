import { Component } from '@angular/core';
import { UserMenuComponent } from '../user-menu/user-menu';

@Component({
  selector: 'app-header',
  imports: [UserMenuComponent],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class HeaderComponent { }
