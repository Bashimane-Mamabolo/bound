import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { AuthenticationnControllerService } from "src/app/services/services/authenticationn-controller.service";

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss']
})
export class ActivateAccountComponent {

  message = '';
  isCorrect = true;
  submitted = false;

  constructor(
    private router: Router,
    private authService: AuthenticationnControllerService
  ) {
  }

  protected onCodeCompletion(code: string) {
    this.confirmAccount(code);
  }

  protected goToLogin() {
    this.router.navigate(['login']);
  }

  private confirmAccount(code: string) {
    this.authService.confirmAccount({
      code
    }).subscribe(
      {
        next: () => {
          this.message = 'Account activated successfully! \n You can now log in.';
          this.submitted = true;
          this.isCorrect = true;
        },
        error: () => {
          this.message = 'Invalid or expired activation code. \nPlease check your email for the correct code.';
          this.submitted = true;
          this.isCorrect = false;
        }
      }
    )
  }
}
