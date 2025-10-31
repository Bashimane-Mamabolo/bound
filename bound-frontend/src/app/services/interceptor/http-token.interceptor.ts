import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from "../token/token.service";

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(
    private tokenService: TokenService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const jwtToken = this.tokenService.token;
    // We're not able to change the existing request, so we much Copy/Clone it
    if (jwtToken) {
      const authRequest = request.clone({
        headers: new HttpHeaders({
          Authorization: `Bearer ${jwtToken}`
        })
      });
      return next.handle(authRequest);

    }
    return next.handle(request);
  }
}
