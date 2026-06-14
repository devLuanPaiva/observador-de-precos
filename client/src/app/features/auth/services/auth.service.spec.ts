import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { PLATFORM_ID } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { vi } from 'vitest';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const mockLoginRequest: LoginRequest = {
    email: 'test@example.com',
    password: 'password123',
  };

  const mockAuthResponse: AuthResponse = {
    accessToken: 'mock-access-token',
    refreshToken: 'mock-refresh-token',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        { provide: PLATFORM_ID, useValue: 'browser' },
      ],
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);

    sessionStorage.clear();
  });

  afterEach(() => {
    if (httpMock) {
      httpMock.verify();
    }
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should load token from sessionStorage on service initialization', () => {
    TestBed.resetTestingModule();
    const testToken: string = 'stored-token';
    sessionStorage.setItem('access_token', testToken);
    const getItemSpy = vi.spyOn(Storage.prototype, 'getItem').mockReturnValue(testToken);

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        { provide: PLATFORM_ID, useValue: 'browser' },
      ],
    });

    const newService: AuthService = TestBed.inject(AuthService);

    expect(getItemSpy).toHaveBeenCalledWith('access_token');
    expect(newService.access_token()).toBe(testToken);

    getItemSpy.mockRestore();
  });

  it('should perform login and save token in sessionStorage and access_token signal', () => {
    const setItemSpy = vi.spyOn(Storage.prototype, 'setItem');
    const loginRequest: LoginRequest = mockLoginRequest;

    service.login(loginRequest).subscribe();

    const req = httpMock.expectOne((request) => request.url.includes('/auth/login'));
    req.flush(mockAuthResponse);

    expect(setItemSpy).toHaveBeenCalledWith('access_token', mockAuthResponse.accessToken);
    expect(service.access_token()).toBe(mockAuthResponse.accessToken);

    setItemSpy.mockRestore();
  });

  it('should send correct POST request to /auth/login endpoint', () => {
    const loginRequest: LoginRequest = mockLoginRequest;

    service.login(loginRequest).subscribe();

    const req = httpMock.expectOne((request) => request.url.includes('/auth/login'));
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(loginRequest);

    req.flush(mockAuthResponse);
  });

  it('should remove token from sessionStorage and clear access_token signal on logout', () => {
    service.access_token.set('existing-token');
    sessionStorage.setItem('access_token', 'existing-token');
    const removeItemSpy = vi.spyOn(Storage.prototype, 'removeItem');

    service.logout();

    expect(removeItemSpy).toHaveBeenCalledWith('access_token');
    expect(service.access_token()).toBeNull();

    removeItemSpy.mockRestore();
  });

  it('should return correct authentication state from isAuthenticated method', () => {
    service.access_token.set(null);

    expect(service.isAuthenticated()).toBe(false);

    service.access_token.set('test-token');

    expect(service.isAuthenticated()).toBe(true);
  });
});
