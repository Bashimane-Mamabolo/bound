import { Component } from '@angular/core';
import { UserAuthenticationRequest } from "src/app/services/models/user-authentication-request";
import { Router } from "@angular/router";
import { AuthenticationControllerService } from "src/app/services/services/authentication-controller.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  authRequest: UserAuthenticationRequest = {
    email: '',
    password: ''
  };
  errorMessage: Array<string> = [];

  constructor(
    private router : Router,
    private authService: AuthenticationControllerService
    // another service
  ) {
  }

  login() {
    this.errorMessage = [];
    this.authService.authenticateUser({
      body: this.authRequest
    }).subscribe({
      next: (response) => {
        // TODO: Handle successful authentication (e.g., store token, redirect)
        this.router.navigate(['books'])  //route the user to books page after successful login
      },
      error: (error) => {
        console.log(error);
        if(error.error.validationErrors){
          this.errorMessage = error.error.validationErrors;

        } else {
          this.errorMessage.push(error.error.error);
        }

      }
    })

  }

  Register() {

    this.router.navigate(['register']);
  }
}
