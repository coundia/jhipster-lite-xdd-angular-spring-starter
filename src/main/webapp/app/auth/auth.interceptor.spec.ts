import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AuthServerProvider } from './auth-jwt.service';
import { authInterceptor } from './auth.interceptor';

describe('Auth Interceptor', () => {
  let service: AuthServerProvider;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withInterceptors([authInterceptor])), provideHttpClientTesting()],
    });

    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(AuthServerProvider);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('intercept', () => {
    it('should add authorization header with localStorageService', () => {
      const token = 'azerty';
      Storage.prototype.getItem = jest.fn(() => token);

      service.login({ username: 'John', password: '123' }).subscribe();

      const httpReq = httpMock.expectOne('api/authenticate');
      expect(httpReq.request.headers.has('Authorization')).toBe(true);
    });
  });
});
