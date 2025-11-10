import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";
import {BookResponse} from "../../../../services/models/book-response";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";

@Component({
  selector: 'app-return-books',
  templateUrl: './return-books.component.html',
  styleUrls: ['./return-books.component.scss']
})
export class ReturnBooksComponent implements OnInit {

  returnedBooks: PageResponseBorrowedBookResponse = {};
  page = 0;
  size = 5;
  protected message = '';
  protected level = 'success';


  constructor(
    private bookService: BookService

  ) {
  }

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }


  approveBookReturn(book: BorrowedBookResponse){
    if (!book.bookReturned){
      this.level = 'error';
      this.message = 'The book is not yet returned.';
      return;
    }
    this.bookService.approvedReturnedBook({
      'book-id': book.id as number
    }).subscribe({
      next: result => {
        this.level = 'success';
        this.message = 'Book returned successfully approved';
        this.findAllBorrowedBooks();
      }
    })
  }

  private findAllBorrowedBooks() {
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: result => {
        this.returnedBooks = result;
      }
    })
  }

  protected gotoFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  protected goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }

  protected goToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  protected goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  protected goToLastPage() {
    this.page = this.returnedBooks.totalPages as number -1;
  }

  get isLastPage(): boolean {
    return this.page == this.returnedBooks.totalPages as number - 1;
  }

}
