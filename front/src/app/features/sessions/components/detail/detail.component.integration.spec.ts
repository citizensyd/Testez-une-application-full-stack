import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import {SessionApiService} from "../../services/session-api.service";
import {TeacherService} from "../../../../services/teacher.service";
import { Session } from '../../interfaces/session.interface';
import {ActivatedRoute, Router} from "@angular/router";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {of} from "rxjs";
import {MatIconModule} from "@angular/material/icon";
import {MatCardModule} from "@angular/material/card";
import {CommonModule} from "@angular/common";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let sessionService: SessionService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockSession: Session = {
    name: "Introduction à TypeScript",
    description: "Cette session couvre les bases de TypeScript, y compris les types, les interfaces, et les classes.",
    date: new Date('2024-03-15T09:00:00'),
    teacher_id: 25,
    users: [201, 202, 203, 204],
    createdAt: new Date('2024-02-20T10:00:00'),
    updatedAt: new Date('2024-02-25T15:30:00')
  }
  const mockTeacher: Teacher = {
    id: 25,
    lastName: "Dupont",
    firstName: "Jean",
    createdAt: new Date('2023-08-10T14:00:00'),
    updatedAt: new Date('2024-01-05T11:00:00')
  };

  beforeEach(async () => {
    const activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
        },
      },
    };
    // Mocks pour les services
    const sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({})),
      delete: jest.fn().mockReturnValue(of({})),
      // autres méthodes selon les besoins
    };

    const teacherServiceMock = {
      detail: jest.fn().mockReturnValue(of(mockTeacher)),
    };

    const sessionServiceMock = {
      sessionInformation: {
        admin: true,
        id: 1
      },
    };

    const routerMock = {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        CommonModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },

      ],
    })
      .compileComponents();
    sessionService = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session and teacher details on init', () => {
    expect(sessionApiService.detail).toHaveBeenCalledWith('1');
    expect(teacherService.detail).toHaveBeenCalledWith(mockSession.teacher_id.toString());
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
  });

});

