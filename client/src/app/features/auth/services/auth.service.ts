import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { tap } from 'rxjs';
import { AuthTokenService } from './auth-token.service';
import { AuthSessionService } from './auth-session.service';
import { AuthUser } from '../models/auth-user.model';

interface JwtPayload {
  sub?: string;
  name?: string;
  email?: string;
  [key: string]: unknown;
}

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

        const user = this.decodeJwtUser(response.accessToken);
        if (user) {
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

  private decodeJwtUser(token: string): AuthUser | null {
    try {
      const parts = token.split('.');
      if (parts.length < 2) return null;
      const payloadJson = AuthService.safeBase64UrlDecode(parts[1]);
      const parsed = JSON.parse(payloadJson) as JwtPayload | null;
      if (!parsed) return null;
      if (typeof parsed.sub === 'string' && typeof parsed.name === 'string' && typeof parsed.email === 'string') {
        return { id: parsed.sub, name: parsed.name, email: parsed.email };
      }
      return null;
    } catch {
      return null;
    }
  }

  private static safeBase64UrlDecode(input: string): string {
    input = input.replaceAll('-', '+').replaceAll('_', '/');
    const pad = input.length % 4;
    if (pad === 2) input += '==';
    else if (pad === 3) input += '=';
    else if (pad !== 0) input += '===='.slice(pad);
    return atob(input);
  }
}
