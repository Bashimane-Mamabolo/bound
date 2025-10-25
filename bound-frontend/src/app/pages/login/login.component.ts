import { Component } from '@angular/core';
import { UserAuthenticationRequest } from "src/app/services/models/user-authentication-request";
import { Router } from "@angular/router";
import { AuthenticationControllerService } from "src/app/services/services/authentication-controller.service";
import { TokenService } from "src/app/services/token/token.service";

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
    private authService: AuthenticationControllerService,
    private tokenService: TokenService
  ) {
  }

  login() {
    this.errorMessage = [];
    this.authService.authenticateUser({
      body: this.authRequest
    }).subscribe({
      next: (response) => {
        this.tokenService.token = response.jwtToken as string;  //store the token using token service
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
