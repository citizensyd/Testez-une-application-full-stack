import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {of} from "rxjs";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    // Mock AuthService et Router
    const authServiceMock = {
      login: jest.fn().mockReturnValue(of({ token: 'fake-token' })),
    };

    const routerMock = {
      navigate: jest.fn(),
    };
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to "/sessions" on successful login', () => {
    // Remplir le formulaire avec des données de test
    component.form.setValue({ email: 'test@example.com', password: 'password' });

    // Simuler la soumission du formulaire
    component.submit();

    // Vérifier si la méthode login du AuthService a été appelée
    expect(authService.login).toHaveBeenCalledWith({ email: 'test@example.com', password: 'password' });

    // Attendre que les Promises résolvent
    fixture.whenStable().then(() => {
      // Vérifier si le navigateur a été redirigé vers '/sessions'
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    });
  });



});
