import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MeComponent } from './me.component';
import { UserService } from '../../services/user.service';
import { SessionService } from '../../services/session.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import {expect} from "@jest/globals";
import { User } from '../../interfaces/user.interface';
import {MatIconModule} from "@angular/material/icon";
import {SessionInformation} from "../../interfaces/sessionInformation.interface";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let snackBar: MatSnackBar;
  let router: Router;

  const mockSessionInfo: SessionInformation = {
    token: "some-fake-token",
    type: "Bearer",
    id: 1,
    username: "john.doe",
    firstName: "John",
    lastName: "Doe",
    admin: false
  };
  const mockUser: User = {
    id: mockSessionInfo.id, // Assure-toi que l'ID correspond
    email: "john.doe@example.com",
    lastName: "Doe",
    firstName: "John",
    admin: false,
    password: "password123",
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [RouterTestingModule, MatSnackBarModule, HttpClientTestingModule, MatCardModule, MatIconModule],
      providers: [UserService, SessionService, MatSnackBar]
    }).compileComponents();
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    snackBar = TestBed.inject(MatSnackBar);

    router = TestBed.inject(Router);

    sessionService.sessionInformation = mockSessionInfo;
    fixture = TestBed.createComponent(MeComponent);
    jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should load user data on initialization', () => {
    expect(userService.getById).toHaveBeenCalledWith(mockSessionInfo.id.toString());
    expect(component.user).toEqual(mockUser);
  });

  it('should delete user account', () => {
    jest.spyOn(router, 'navigate');
    jest.spyOn(userService, 'delete').mockReturnValue(of(null));
    const logOutSpy = jest.spyOn(sessionService, 'logOut');
    jest.spyOn(snackBar, 'open');

    component.delete();

    expect(userService.delete).toHaveBeenCalledWith(mockSessionInfo.id.toString());
    expect(snackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', {duration: 3000});
  });

});
