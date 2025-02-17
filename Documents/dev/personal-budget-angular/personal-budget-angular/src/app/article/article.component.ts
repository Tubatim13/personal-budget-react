import { Component, OnInit, NO_ERRORS_SCHEMA  } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'pb-article',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss'],
  schemas: [NO_ERRORS_SCHEMA]
})

export class ArticleComponent implements OnInit {

  constructor(){}

  ngOnInit(): void {}
}
