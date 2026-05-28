import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@features/auth/services/auth.service';
import { selectUser } from '@features/auth/store/auth.selectors';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-user-menu',
  imports: [],
  templateUrl: './user-menu.html',
  styleUrl: './user-menu.scss',
})
export class UserMenuComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly store = inject(Store);

  open = signal(false);

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  user =
    this.store.selectSignal(
      selectUser
    );
}
