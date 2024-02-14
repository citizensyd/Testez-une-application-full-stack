import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {SessionApiService} from '../../services/session-api.service';

import {FormComponent} from './form.component';
import {Router} from "@angular/router";
import {NgZone} from "@angular/core";
import {of} from "rxjs";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionApiService: any;
  let mockSnackBar: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {

    mockSessionApiService = {
      create: jest.fn().mockReturnValue(of({})), // Mock pour create
      update: jest.fn().mockReturnValue(of({}))  // Mock pour update
    };

    mockSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: mockSnackBar }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect non-admin user', () => {
    // Configurer le mock pour simuler un utilisateur non admin
    mockSessionService.sessionInformation.admin = false;

    fixture.detectChanges(); // Pour prendre en compte la nouvelle configuration du mock

    const router = TestBed.inject(Router);
    const ngZone = TestBed.inject(NgZone); // Injecter NgZone
    const spy = jest.spyOn(router, 'navigate');

    ngZone.run(() => {
      component.ngOnInit(); // Appeler ngOnInit à l'intérieur de ngZone.run
    });

    // Vérifier si la méthode navigate a été appelée avec le bon argument
    expect(spy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should call create and exitPage on submit when not updating', () => {
    const ngZone = TestBed.inject(NgZone); // Injecter NgZone
    component.onUpdate = false;
    component.sessionForm = new FormGroup({
      // Configurez ici les contrôles du formulaire en fonction de la structure de votre formulaire
    });
    component.sessionForm.setValue({ /* valeurs de test pour votre formulaire */ });

    ngZone.run(() => {
      component.submit(); // Appeler ngOnInit à l'intérieur de ngZone.run
    });


    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
  });




});
