import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { AuthSessionService } from './auth-session.service';
import { AuthUser } from '../models/auth-user.model';
import { decodeJwtPayload } from '@shared/utils/jwt.util';
import { map } from 'rxjs';



@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly platformId = inject(PLATFORM_ID);
  apiUrl = signal(api_url)

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
      map(response => {
        const parsed = decodeJwtPayload(response.accessToken);
        let user: AuthUser | null = null;
        if (parsed && typeof parsed.sub === 'string' && typeof parsed.name === 'string' && typeof parsed.email === 'string') {
          user = { id: parsed.sub, name: parsed.name, email: parsed.email };
          this.session.setSession(user, response.accessToken, response.refreshToken);
        } else {
          this.session.setSession(null, response.accessToken, response.refreshToken);
        }

        return {
          ...response,
          user
        } as AuthResponse;
      })
    )
  }

  logout() {
    this.session.clearSession();
  }

  isAuthenticated(): boolean {
    return !!this.session.user();
  }

  register(payload: { name: string; email: string; password: string }) {
    return this.http.post(`${this.apiUrl()}/auth/register`, payload);

  }

  sessionRefreshToken() {
    return this.session.getRefreshToken();
  }

  refreshToken() {
    const refreshToken =
      this.sessionRefreshToken();

    return this.http.post<AuthResponse>(
      `${this.apiUrl()}/auth/refresh`,
      {
        refreshToken
      }
    );
  }

  getRefreshToken() {
  return this.session
      .getRefreshToken();
}

}
