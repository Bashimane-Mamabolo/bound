import {Component, OnInit} from '@angular/core';
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrls: ['./borrowed-book-list.component.scss']
})
export class BorrowedBookListComponent implements OnInit {

  borrowedBooks: PageResponseBorrowedBookResponse = {};
  page = 0;
  size = 5;

  constructor(
    private bookService: BookService

  ) {
  }

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  returnBorrowedBook(book: BorrowedBookResponse) {}

  private findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: result => {
        this.borrowedBooks = result;
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
    this.page = this.borrowedBooks.totalPages as number -1;
  }

  get isLastPage(): boolean {
    return this.page == this.borrowedBooks.totalPages as number - 1;
  }
}
