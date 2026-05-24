import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { vi, describe, it, expect, beforeEach, afterEach } from 'vitest';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { LoginPage } from './login-page';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginPage', (): void => {
  let component: LoginPage;
  let fixture: ComponentFixture<LoginPage>;
  let authService: AuthService;
  let router: Router;
  let loginSpy: ReturnType<typeof vi.spyOn>;
  let navigateSpy: ReturnType<typeof vi.spyOn>;

  const mockLoginData = {
    email: 'test@example.com',
    password: 'Password123',
  };

  const mockLoginError = {
    message: 'Credenciais inválidas. Por favor, verifique seu email e senha e tente novamente.',
  };

  beforeEach(async (): Promise<void> => {
    await TestBed.configureTestingModule({
      imports: [LoginPage],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginPage);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    const mockAuthResponse = {
      accessToken: 'access-token-mock',
      refreshToken: 'refresh-token-mock',
    };

    loginSpy = vi.spyOn(authService, 'login').mockReturnValue(of(mockAuthResponse));
    navigateSpy = vi.spyOn(router, 'navigate').mockResolvedValue(true);

    fixture.detectChanges();
    await fixture.whenStable();
  });

  afterEach((): void => {
    loginSpy.mockRestore();
    navigateSpy.mockRestore();
  });

  it('should create', (): void => {
    expect(component).toBeTruthy();
  });

  it('should mark form as touched when submitting invalid form', (): void => {
    component.authForm().reset();

    component.onSubmit();

    expect(component.authForm().touched()).toBe(true);
  });

  it('should call AuthService.login when form is valid', async (): Promise<void> => {
    component.authModel.set({
      email: mockLoginData.email,
      password: mockLoginData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(loginSpy).toHaveBeenCalledWith({
      email: mockLoginData.email,
      password: mockLoginData.password,
    });
  });

  it('should navigate to /dashboard when login is successful', async (): Promise<void> => {
    component.authModel.set({
      email: mockLoginData.email,
      password: mockLoginData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(navigateSpy).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should set error message when login fails', async (): Promise<void> => {
    loginSpy.mockReturnValue(
      throwError(() => ({
        error: { message: mockLoginError.message }
      }))
    );
    component.authModel.set({
      email: mockLoginData.email,
      password: mockLoginData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.submitError()).toBe(mockLoginError.message);
  });
});
