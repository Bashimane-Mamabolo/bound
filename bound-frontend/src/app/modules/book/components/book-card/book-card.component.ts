import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from "src/app/services/models/book-response";

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss']
})
export class BookCardComponent {
  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

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

  protected onShowDetails() {
    this.details.emit(this._book);
  }

  protected onBorrow() {
    this.borrow.emit(this._book);
  }

  protected onAddToWaitingList() {
    this.addToWaitingList.emit(this._book);
  }

  protected onEdit() {
    this.edit.emit(this._book);
  }

  protected onShare(){
    this.share.emit(this._book);
  }

  protected onArchive(){
    this.archive.emit(this._book);
  }
}
