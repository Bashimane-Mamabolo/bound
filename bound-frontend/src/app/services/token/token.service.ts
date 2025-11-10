import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  // save token to local storage
  set token(token: string | null) {
    if (token) {
      localStorage.setItem('token', token);
    } else {
      localStorage.removeItem('token');
    }
  }

  // get token from local storage
  get token(): string | null {
    return localStorage.getItem('token') as string;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  private isTokenValid() {
    const token =  this.token;
    if (!token) {
      return false;
    }
    // decode the token, install a dependency to do that
    const jwtHelper = new JwtHelperService();
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }
}
