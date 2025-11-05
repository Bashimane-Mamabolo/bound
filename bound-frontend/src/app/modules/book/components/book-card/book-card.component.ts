import { Component, Input } from '@angular/core';
import { BookResponse } from "src/app/services/models/book-response";

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss']
})
export class BookCardComponent {

  private _book: BookResponse = {};

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value){
    this._book = value;
  }

}
