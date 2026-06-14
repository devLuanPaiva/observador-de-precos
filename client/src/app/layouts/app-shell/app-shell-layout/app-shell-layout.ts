import { Component } from '@angular/core';
import { SidebarComponent } from '../components/sidebar/sidebar';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../components/header/header';

@Component({
  selector: 'app-app-shell-layout',
  imports: [SidebarComponent, RouterOutlet, HeaderComponent],
  templateUrl: './app-shell-layout.html',
  styleUrl: './app-shell-layout.scss',
})
export class AppShellLayout { }
