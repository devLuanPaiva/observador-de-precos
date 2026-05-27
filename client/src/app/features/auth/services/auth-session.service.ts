import { computed, inject, Injectable, signal } from '@angular/core';
import { AuthTokenService } from './auth-token.service';
import { Router } from '@angular/router';
import { AuthUser } from '../models/auth-user.model';



@Injectable({
  providedIn: 'root',
})
export class AuthSessionService {
  private readonly tokenService = inject(AuthTokenService);

  private readonly router = inject(Router);

  private readonly userSignal = signal<AuthUser | null>(null);

  readonly user = this.userSignal.asReadonly();

  readonly authenticated = computed(() => !!this.user());

  setSession(user: AuthUser, token: string): void {
    this.tokenService.setAccessToken(token);
    this.userSignal.set(user);
  }

  clearSession(): void {
    this.tokenService.clearAccessToken();
    this.userSignal.set(null);
  }

  logout() {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  hydrateSession() {
    const token = this.tokenService.getAccessToken();

    if (!token) return


  }
}
