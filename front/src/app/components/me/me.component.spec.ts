import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import {UserService} from "../../services/user.service";
import {of} from "rxjs";
import {User} from "../../interfaces/user.interface";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call userService.getById on ngOnInit with correct id', () => {
    const userService = TestBed.inject(UserService);
    const spy = jest.spyOn(userService, 'getById');
    component.ngOnInit();
    expect(spy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
  });

  it('should set user on successful getUserById', () => {
    const userService = TestBed.inject(UserService);
    const mockUser: User = {
      id: 1,
      email: 'test@example.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: false,
      password: 'password123',
      createdAt: new Date('2020-01-01T00:00:00.000Z')
      // updatedAt est facultatif, donc pas inclus ici
    };

    jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));
    component.ngOnInit();

    expect(component.user).toEqual(mockUser);
  });

  it('should call window.history.back on back()', () => {
    // Créer un mock pour window.history.back
    const historySpy = jest.spyOn(window.history, 'back');

    // Appeler la méthode back()
    component.back();

    // Vérifier si window.history.back a été appelé
    expect(historySpy).toHaveBeenCalled();

    // Nettoyage: supprimer le mock pour éviter des effets sur d'autres tests
    historySpy.mockRestore();
  });
});
