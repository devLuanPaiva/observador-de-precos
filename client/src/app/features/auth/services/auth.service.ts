import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  apiUrl = signal(api_url)

  userToken = signal<string | null>(
    localStorage.getItem('userToken')
  )

  login(payload: LoginRequest) {
    return this.http.post<AuthResponse>(
      `${this.apiUrl()}/auth/login`,
      payload
    ).pipe(
      tap(response => {
        localStorage.setItem('userToken', response.accessToken);

        this.userToken.set(response.accessToken);
      })
    )
  }

  logout() {
    localStorage.removeItem('userToken');
    this.userToken.set(null);
  }

  isAuthenticated() {
    return !!this.userToken();
  }
}
