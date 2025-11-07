 import { Component } from '@angular/core';
 import { Router } from "@angular/router";
 import { BookRequest } from "src/app/services/models/book-request";
 import { BookService } from "src/app/services/services";

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrls: ['./manage-book.component.scss']
})
export class ManageBookComponent {
  errorMessage: Array<string> = [];
  selectedPicture: string | undefined;
  selectedBookCover: any;
  bookRequest: BookRequest = {authorName: '', isbn: '', synopsis: '', title: ''}

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  protected onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);
    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  protected saveBook() {
    this.bookService.createBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        this.bookService.uploadBookCover({
          'book-id': bookId,
          body: {
            file: this.selectedBookCover
          }
        }).subscribe({
          next: (bookId) => {
            this.router.navigate(['/books/my-books']);
          }
        })

      },
      error: err => {
        this.errorMessage = err.error.validationErrors;
      }
    })
  }
}
