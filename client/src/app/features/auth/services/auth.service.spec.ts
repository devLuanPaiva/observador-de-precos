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

    localStorage.clear();
  });

  afterEach(() => {
    if (httpMock) {
      httpMock.verify();
    }
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should load token from localStorage on service initialization', () => {
    TestBed.resetTestingModule();
    const testToken: string = 'stored-token';
    localStorage.setItem('userToken', testToken);
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

    expect(getItemSpy).toHaveBeenCalledWith('userToken');
    expect(newService.userToken()).toBe(testToken);

    getItemSpy.mockRestore();
  });

  it('should perform login and save token in localStorage and userToken signal', () => {
    const setItemSpy = vi.spyOn(Storage.prototype, 'setItem');
    const loginRequest: LoginRequest = mockLoginRequest;

    service.login(loginRequest).subscribe();

    const req = httpMock.expectOne((request) => request.url.includes('/auth/login'));
    req.flush(mockAuthResponse);

    expect(setItemSpy).toHaveBeenCalledWith('userToken', mockAuthResponse.accessToken);
    expect(service.userToken()).toBe(mockAuthResponse.accessToken);

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

  it('should remove token from localStorage and clear userToken signal on logout', () => {
    service.userToken.set('existing-token');
    localStorage.setItem('userToken', 'existing-token');
    const removeItemSpy = vi.spyOn(Storage.prototype, 'removeItem');

    service.logout();

    expect(removeItemSpy).toHaveBeenCalledWith('userToken');
    expect(service.userToken()).toBeNull();

    removeItemSpy.mockRestore();
  });

  it('should return correct authentication state from isAuthenticated method', () => {
    service.userToken.set(null);

    expect(service.isAuthenticated()).toBe(false);

    service.userToken.set('test-token');

    expect(service.isAuthenticated()).toBe(true);
  });
});
