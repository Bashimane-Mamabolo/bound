import { Component } from '@angular/core';
import {OnInit} from "@angular/core";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit{

  ngOnInit() {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if(window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', (event) => {
        linkColor.forEach(link => {link.classList.remove('active');});
        link.classList.add('active');
      });
    });
  }

  protected logout() {
      localStorage.removeItem('token');
      window.location.reload();
  }
}


