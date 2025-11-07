import { Component, OnInit } from '@angular/core';
import {BookService} from 'src/app/services/services/book.service'
import { Router } from "@angular/router";
import {BookResponse, PageResponseBookResponse } from "src/app/services/models";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  page = 0;
  size = 3;
  bookResponse: PageResponseBookResponse = {};
  // isLastPage: boolean = false;

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
        this.findAllBooks()
    }

  private findAllBooks() {
    this.bookService.findAllDisplayableBooks(
      {
        page: this.page,
        size: this.size
      }
    ).subscribe({
      next: (books) =>{
        this.bookResponse = books;
      },
      error: () => {

      }
    })

  }

  protected gotoFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  protected goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  protected goToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  protected goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  protected goToLastPage() {
    this.page = this.bookResponse.totalPages as number -1;
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number - 1;
  }

  protected borrowBook(book: BookResponse) {

  }
}
