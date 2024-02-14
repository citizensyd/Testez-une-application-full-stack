import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';

import {SessionService} from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;
  const testUserLogIn: SessionInformation = {
    token: 'test-token',
    type: 'user',
    id: 123,
    username: 'testuser',
    firstName: 'Test',
    lastName: 'User',
    admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in a user', () => {
    service.logIn(testUserLogIn);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(testUserLogIn);
  });

  it('should log out a user', () => {
    service.logIn(testUserLogIn);
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit true when user logs in and false when user logs out', (done) => {
    const subscription = service.$isLogged().subscribe((isLogged) => {
      if (isLogged) {
        expect(isLogged).toBe(true);
        service.logOut();
      } else {
        expect(isLogged).toBe(false);
        subscription.unsubscribe();
        done();
      }
    });

    service.logIn(testUserLogIn);
  });


});
