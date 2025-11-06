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

  private _bookCover: string | undefined;

  get bookCover(): string | undefined {
    if (this._book.bookCover){
      return 'data:image/jpg;base64, ' + this._book.bookCover;
    }
    // return this._bookCover;
    // return 'https://source.unsplash.com/user/c_v_r/1900x800' unsplash not working
    return 'https://picsum.photos/200/300';
  }

  private _manage = false;

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

}
