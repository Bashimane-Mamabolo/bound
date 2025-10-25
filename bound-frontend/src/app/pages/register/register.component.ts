import { Component } from '@angular/core';
import { UserRegistrationRequest } from "src/app/services/models";
import { Router } from "@angular/router";
import { AuthenticationControllerService } from "src/app/services/services/authentication-controller.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerRequest : UserRegistrationRequest = {
    email: '',
    firstname: '',
    lastname: '',
    password: ''
  };
  errorMessage: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationControllerService
  ) {
  }

  protected register() {
    // Clear previous error messages
    this.errorMessage = [];

    this.authService.registerUser({
      body: this.registerRequest
    }).subscribe({
      next: () => {
        // On successful registration, navigate to account activation page
        this.router.navigate(['activate-account']);
      },
      error: (error) => {
        // Handle error, there will only be validation errors from the backend
        this.errorMessage = error.error.validationErrors;
      }
    });
  }

  protected login() {
    this.router.navigate(['login']);

  }
}
