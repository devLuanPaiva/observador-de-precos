import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient, withInterceptors, HttpClient } from '@angular/common/http';
import { vi, describe, it, expect, beforeEach, afterEach } from 'vitest';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let httpClient: HttpClient;
  let httpMock: HttpTestingController;
  let getItemSpy: ReturnType<typeof vi.spyOn>;

  const testUrl: string = 'http://api.example.com/test';
  const mockToken: string = 'mock-auth-token-12345';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });

    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    sessionStorage.clear();
    getItemSpy = vi.spyOn(Storage.prototype, 'getItem');
  });

  afterEach(() => {
    httpMock.verify();
    getItemSpy.mockRestore();
    sessionStorage.clear();
  });

  it('should add Authorization header with Bearer token when userToken exists in sessionStorage', () => {
    getItemSpy.mockReturnValue(mockToken);
    sessionStorage.setItem('userToken', mockToken);

    httpClient.get<void>(testUrl).subscribe();

    const req = httpMock.expectOne(testUrl);
    expect(req.request.headers.has('Authorization')).toBe(true);
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    expect(getItemSpy).toHaveBeenCalledWith('userToken');

    req.flush(null);
  });

  it('should NOT add Authorization header when userToken does not exist in sessionStorage', () => {
    getItemSpy.mockReturnValue(null);

    httpClient.get<void>(testUrl).subscribe();

    const req = httpMock.expectOne(testUrl);
    expect(req.request.headers.has('Authorization')).toBe(false);
    expect(getItemSpy).toHaveBeenCalledWith('userToken');

    req.flush(null);
  });

  it('should pass the request to the next handler unchanged when token is added', () => {
    getItemSpy.mockReturnValue(mockToken);
    sessionStorage.setItem('userToken', mockToken);
    const testData: Record<string, string> = { id: '1', name: 'test' };

    httpClient.get<typeof testData>(testUrl).subscribe();

    const req = httpMock.expectOne(testUrl);
    expect(req.request.method).toBe('GET');
    expect(req.request.url).toBe(testUrl);

    req.flush(testData);
  });

  it('should pass the request to the next handler unchanged when token does not exist', () => {
    getItemSpy.mockReturnValue(null);
    const testData: Record<string, string> = { id: '1', name: 'test' };

    httpClient.get<typeof testData>(testUrl).subscribe();

    const req = httpMock.expectOne(testUrl);
    expect(req.request.method).toBe('GET');
    expect(req.request.url).toBe(testUrl);

    req.flush(testData);
  });

  it('should handle POST requests with Authorization header when token exists', () => {
    getItemSpy.mockReturnValue(mockToken);
    sessionStorage.setItem('userToken', mockToken);
    const postData: Record<string, string> = { email: 'test@example.com' };

    httpClient.post<void>(testUrl, postData).subscribe();

    const req = httpMock.expectOne(testUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    expect(req.request.body).toEqual(postData);

    req.flush(null);
  });

  it('should not modify existing headers when adding Authorization header', () => {
    getItemSpy.mockReturnValue(mockToken);
    sessionStorage.setItem('userToken', mockToken);

    httpClient.get<void>(testUrl, {
      headers: { 'Content-Type': 'application/json' },
    }).subscribe();

    const req = httpMock.expectOne(testUrl);
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    expect(req.request.headers.get('Content-Type')).toBe('application/json');

    req.flush(null);
  });
});
