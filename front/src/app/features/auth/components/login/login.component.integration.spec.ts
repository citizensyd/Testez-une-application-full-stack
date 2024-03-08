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
import {of, throwError} from "rxjs";
import {By} from "@angular/platform-browser";

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

  it('should display an error when the login is incorrect', () => {
    // Simuler une réponse d'erreur de l'AuthService
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Invalid credentials')));

    // Remplir le formulaire avec des données incorrectes
    component.form.setValue({ email: 'wrong@example.com', password: 'wrongpassword' });

    // Simuler la soumission du formulaire
    component.submit();

    // Vérifier si la méthode login du AuthService a été appelée avec des données incorrectes
    expect(authService.login).toHaveBeenCalledWith({ email: 'wrong@example.com', password: 'wrongpassword' });

    // Attendre que les Promises résolvent
    fixture.whenStable().then(() => {
      // Vérifier si le drapeau d'erreur est activé
      expect(component.onError).toBe(true);

      // Vérifier si aucun routage n'a été effectué
      expect(router.navigate).not.toHaveBeenCalledWith(['/sessions']);
    });
  });

  it('should show error messages for required fields', () => {
    // Simuler une réponse d'erreur de l'AuthService
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Invalid credentials')));

    // Simuler la soumission du formulaire avec des champs vides
    component.form.setValue({ email: '', password: '' });
    component.submit();

    // Vérifier si les champs sont marqués comme invalides
    expect(component.form.get('email')!.valid).toBe(false);
    expect(component.form.get('password')!.valid).toBe(false);

    // Vérifier si les messages d'erreur sont affichés
    fixture.detectChanges(); // Déclencher la détection des changements pour mettre à jour la vue
    expect(component.onError).toBe(true);
    const errorElement = fixture.debugElement.query(By.css('.error')).nativeElement;

    expect(errorElement).not.toBeNull();
    expect(errorElement.textContent).toContain('An error occurred');
  });

});
