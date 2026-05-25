import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@features/auth/services/auth.service';

@Component({
  selector: 'app-user-menu',
  imports: [],
  templateUrl: './user-menu.html',
  styleUrl: './user-menu.scss',
})
export class UserMenuComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  open = signal(false);

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
