import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { of, Subject } from 'rxjs';
import { AccountService } from '../auth/account.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';
import { LoginService } from './login.service';

describe('Login Service', () => {
  let service: LoginService;
  let mockAccountService: AccountService;
  let authServerProvider: AuthServerProvider;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(LoginService);
    mockAccountService = TestBed.inject(AccountService);
    authServerProvider = TestBed.inject(AuthServerProvider);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('login', () => {
    it('should login', () => {
      mockAccountService.identity = jest.fn(() => of(null));
      const login = new Subject<void>();
      authServerProvider.login = jest.fn(() => login.asObservable());

      const credentials = {
        username: 'admin',
        password: 'admin',
      };

      service.login(credentials).subscribe();
      login.next();

      expect(authServerProvider.login).toHaveBeenCalledWith(credentials);
      expect(mockAccountService.identity).toHaveBeenCalled();
    });
  });

  describe('logout', () => {
    it('should logout', () => {
      authServerProvider.logout = jest.fn(() => of());

      service.logout();

      expect(authServerProvider.logout).toHaveBeenCalled();
    });
  });
});
