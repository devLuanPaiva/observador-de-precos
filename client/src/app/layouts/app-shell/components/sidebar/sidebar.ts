import { Component, signal } from '@angular/core';
import { SIDEBAR_MENU } from './sidebar-menu';
import { RouterLink } from '@angular/router';
import { LucideDynamicIcon } from '@lucide/angular';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, LucideDynamicIcon],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class SidebarComponent {
  collapsed = signal(false);
  items = SIDEBAR_MENU;
}
