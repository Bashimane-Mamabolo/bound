 import { Component } from '@angular/core';

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrls: ['./manage-book.component.scss']
})
export class ManageBookComponent {
  errorMessage: Array<string> = [];
  selectedPicture: string | undefined;
  selectedBookCover: any;

  protected onFileSelected(event: any) {

  }
}
