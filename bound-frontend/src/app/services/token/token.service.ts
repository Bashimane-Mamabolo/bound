import { Injectable } from '@angular/core';

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
}
