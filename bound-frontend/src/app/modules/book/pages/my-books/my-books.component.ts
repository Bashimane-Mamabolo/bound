import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import {BookResponse, PageResponseBookResponse } from "src/app/services/models";
import { BookService } from "src/app/services/services";

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrls: ['./my-books.component.scss']
})
export class MyBooksComponent implements OnInit{
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
    this.bookService.findAllBooksByOwner(
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

  protected archiveBook(book: BookResponse) {

  }

  protected shareBook(book: BookResponse) {

  }

  protected editBook(book: BookResponse) {
    this.router.navigate(['books', 'manage', book.id]);
  }
}
