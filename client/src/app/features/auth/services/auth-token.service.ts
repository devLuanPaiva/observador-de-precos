import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthTokenService {
  setAccessToken(token: string): void {
    sessionStorage.setItem('access_token', token);
  }

  getAccessToken(): string | null {
    return sessionStorage.getItem('access_token');
  }

  clearAccessToken(): void {
    sessionStorage.removeItem('access_token');
  }
}

