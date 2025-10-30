import { Component, OnInit } from '@angular/core';
import {BookService} from 'src/app/services/services/book.service'
import { Router } from "@angular/router";
import { PageResponseBookResponse } from "src/app/services/models";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  page = 0;
  size = 5;
  bookResponse: PageResponseBookResponse = {};

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
}
