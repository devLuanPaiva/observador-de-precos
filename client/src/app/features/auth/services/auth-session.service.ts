import { computed, inject, Injectable, signal } from '@angular/core';
import { AuthTokenService } from './auth-token.service';
import { Router } from '@angular/router';
import { AuthUser } from '../models/auth-user.model';

interface JwtPayload {
    sub?: string;
    name?: string;
    email?: string;
    type?: string;
    iat?: number;
    exp?: number;
    [key: string]: unknown;
}


@Injectable({
    providedIn: 'root',
})
export class AuthSessionService {
    private readonly tokenService = inject(AuthTokenService);

    private readonly router = inject(Router);

    private readonly userSignal = signal<AuthUser | null>(null);

    readonly user = this.userSignal.asReadonly();

    readonly authenticated = computed(() => !!this.user());

    setSession(user: AuthUser, token: string, refreshToken?: string): void {
        this.tokenService.setAccessToken(token);
        if (refreshToken) this.tokenService.setRefreshToken(refreshToken);
        this.userSignal.set(user);
    }

    clearSession(): void {
        this.tokenService.clearTokens();
        this.userSignal.set(null);
    }

    logout() {
        this.clearSession();
        this.router.navigate(['/login']);
    }

    hydrateSession() {
        const token = this.tokenService.getAccessToken();

        if (!token) return;

        const user = this.decodeJwtUser(token);
        if (user) {
            this.userSignal.set(user);
        }
    }

    private decodeJwtUser(token: string): AuthUser | null {
        try {
            const parts = token.split('.');
            if (parts.length < 2) return null;
            const payloadJson = AuthSessionService.safeBase64UrlDecode(parts[1]);
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
