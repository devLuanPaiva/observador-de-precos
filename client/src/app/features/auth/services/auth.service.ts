import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly platformId = inject(PLATFORM_ID);
  apiUrl = signal(api_url)

  userToken = signal<string | null>(null)

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('userToken');
      if (token) {
        this.userToken.set(token);
      }
    }
  }

  login(payload: LoginRequest) {
    return this.http.post<AuthResponse>(
      `${this.apiUrl()}/auth/login`,
      payload
    ).pipe(
      tap(response => {
        if (typeof localStorage !== 'undefined') {
          localStorage.setItem('userToken', response.accessToken);
        }
        this.userToken.set(response.accessToken);
      })
    )
  }

  logout() {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('userToken');
    }
    this.userToken.set(null);
  }

  isAuthenticated() {
    return !!this.userToken();
  }

  register(payload: { name: string; email: string; password: string }) {
    return this.http.post(`${this.apiUrl()}/auth/register`, payload);

  }
}
