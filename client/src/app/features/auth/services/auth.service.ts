import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { tap } from 'rxjs';
import { AuthTokenService } from './auth-token.service';
import { AuthSessionService } from './auth-session.service';
import { AuthUser } from '../models/auth-user.model';
import { decodeJwtPayload } from '@shared/utils/jwt.util';



@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly platformId = inject(PLATFORM_ID);
  apiUrl = signal(api_url)

  private readonly tokenService = inject(AuthTokenService);
  private readonly session = inject(AuthSessionService);

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      this.session.hydrateSession();
    }
  }

  login(payload: LoginRequest) {
    return this.http.post<AuthResponse>(
      `${this.apiUrl()}/auth/login`,
      payload
    ).pipe(
      tap(response => {
        if (isPlatformBrowser(this.platformId)) {
          this.tokenService.setAccessToken(response.accessToken);
          this.tokenService.setRefreshToken(response.refreshToken);
        }

        const parsed = decodeJwtPayload(response.accessToken);
        if (parsed && typeof parsed.sub === 'string' && typeof parsed.name === 'string' && typeof parsed.email === 'string') {
          const user: AuthUser = { id: parsed.sub, name: parsed.name, email: parsed.email };
          this.session.setSession(user, response.accessToken, response.refreshToken);
        }
      })
    )
  }

  logout() {
    this.session.clearSession();
  }

  isAuthenticated() {
    return this.session.authenticated();
  }

  register(payload: { name: string; email: string; password: string }) {
    return this.http.post(`${this.apiUrl()}/auth/register`, payload);

  }

  // decoding handled by shared util
}
