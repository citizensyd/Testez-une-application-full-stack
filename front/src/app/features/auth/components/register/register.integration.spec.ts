import {RegisterComponent} from "./register.component";
import {ComponentFixture, fakeAsync, TestBed, tick} from "@angular/core/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {expect} from "@jest/globals";
import {delay, of} from "rxjs";

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

  it('should navigate to login on successful registration', fakeAsync(() => {
    // Mock the register method to return an immediately resolving Observable
    const registerMock = jest.spyOn(authService, 'register').mockReturnValue(of({}).pipe(delay(0)));

    // Spy on the navigate method of the router
    const navigateSpy = jest.spyOn(router, 'navigate').mockReturnValue(Promise.resolve(true));

    // Fill the form with test data that should pass any validation
    component.form.setValue({
      email: 'test@example.com',
      password: 'password',
      lastName: 'test',
      firstName: 'test'
    });

    // Ensure the form is valid if there are any validators
    expect(component.form.valid).toBe(true);

    // Call the submit method of the component
    component.submit();

    // Simulate the passage of time until all asynchronous operations complete
    tick();

    // Check if authService.register was called
    expect(registerMock).toHaveBeenCalled();

    // Check if router.navigate was called with the expected argument
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  }));
});
