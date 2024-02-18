import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, fakeAsync, flushMicrotasks, TestBed, tick, waitForAsync} from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {expect} from '@jest/globals';

import {RegisterComponent} from './register.component';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {delay, of, throwError} from "rxjs";
import {NgZone} from "@angular/core";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: any;
  let router: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    authService = TestBed.inject(AuthService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set onError to true on error during registration', () => {
    // Mock the register method of the AuthService to return an observable that throws an error
    jest.spyOn(authService, 'register').mockImplementation(() => throwError(() => new Error('Error')));

    // Call the submit method
    component.submit();

    // Verification that onError is set to true
    expect(component.onError).toBe(true);
  });


});
