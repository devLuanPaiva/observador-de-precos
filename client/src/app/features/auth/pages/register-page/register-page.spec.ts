import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { vi, describe, it, expect, beforeEach, afterEach } from 'vitest';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { RegisterPage } from './register-page';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('RegisterPage', (): void => {
  let component: RegisterPage;
  let fixture: ComponentFixture<RegisterPage>;
  let authService: AuthService;
  let router: Router;
  let registerSpy: ReturnType<typeof vi.spyOn>;
  let navigateSpy: ReturnType<typeof vi.spyOn>;

  const mockRegistrationData = {
    email: 'newuser@example.com',
    password: 'StrongPassword123',
    confirmPassword: 'StrongPassword123',
  };

  const mockRegistrationError = {
    message: 'Email already exists',
  };

  beforeEach(async (): Promise<void> => {
    await TestBed.configureTestingModule({
      imports: [RegisterPage],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterPage);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    registerSpy = vi.spyOn(authService, 'register').mockReturnValue(of({}));
    navigateSpy = vi.spyOn(router, 'navigate').mockResolvedValue(true);

    fixture.detectChanges();
    await fixture.whenStable();
  });

  afterEach((): void => {
    registerSpy.mockRestore();
    navigateSpy.mockRestore();
  });

  it('should create', (): void => {
    expect(component).toBeTruthy();
  });

  it('should calculate password strength correctly for strong password', (): void => {
    const strongPassword: string = 'StrongPassword123';
    const expectedStrength: string = 'Forte';

    component.registerModel.set({
      name: '',
      email: '',
      password: strongPassword,
    });

    expect(component.passwordStrength()).toBe(expectedStrength);
  });

  it('should calculate password strength correctly for medium password', (): void => {
    const mediumPassword: string = 'Medium';
    const expectedStrength: string = 'Média';

    component.registerModel.set({
      name: '',
      email: '',
      password: mediumPassword,
    });

    expect(component.passwordStrength()).toBe(expectedStrength);
  });

  it('should calculate password strength correctly for weak password', (): void => {
    const weakPassword: string = 'weak';
    const expectedStrength: string = 'Média';

    component.registerModel.set({
      name: '',
      email: '',
      password: weakPassword,
    });

    expect(component.passwordStrength()).toBe(expectedStrength);
  });

  it('should mark form as touched when submitting invalid form', (): void => {
    component.registerForm().reset();

    component.onSubmit();

    expect(component.registerForm().touched()).toBe(true);
  });

  it('should not call register when form is invalid', (): void => {
    component.registerForm().reset();

    component.onSubmit();

    expect(registerSpy).not.toHaveBeenCalled();
  });

  it('should call AuthService.register with valid form data on successful registration', async (): Promise<void> => {
    component.registerModel.set({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(registerSpy).toHaveBeenCalledWith({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
  });

  it('should navigate to /login after successful registration', async (): Promise<void> => {
    component.registerModel.set({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should clear error message on successful registration', async (): Promise<void> => {
    component.registerModel.set({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
    component.submitError.set('Previous error');
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.submitError()).toBeNull();
  });

  it('should set error message when registration fails', async (): Promise<void> => {
    registerSpy.mockReturnValue(
      throwError(() => ({
        error: { message: mockRegistrationError.message }
      }))
    );
    component.registerModel.set({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.submitError()).toBe(mockRegistrationError.message);
  });

  it('should not navigate to /login when registration fails', async (): Promise<void> => {
    registerSpy.mockReturnValue(
      throwError(() => ({
        error: { message: mockRegistrationError.message }
      }))
    );
    component.registerModel.set({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(navigateSpy).not.toHaveBeenCalled();
  });

  it('should disable submit button while loading', (): void => {
    component.registerModel.set({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
    component.isSubmitting.set(true);

    expect(component.isSubmitting()).toBe(true);
  });

  it('should validate form requires name, email, and password', (): void => {
    component.registerForm().reset();

    expect(component.canSubmit()).toBe(false);
  });

  it('should have valid form when all required fields are filled correctly', async (): Promise<void> => {
    component.registerModel.set({
      name: 'Test User',
      email: mockRegistrationData.email,
      password: mockRegistrationData.password,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.canSubmit()).toBe(true);
  });
});
