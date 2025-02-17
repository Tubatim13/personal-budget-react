import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { MenuComponent } from './menu/menu.component';
import { HeroComponent } from './hero/hero.component';
import { HomepageComponent } from './homepage/homepage.component';
import { ArticleComponent } from './article/article.component';
import { FooterComponent } from './footer/footer.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { P404Component } from './p404/p404.component';

@Component({
  selector: 'pb-root',
  standalone: true,
  imports: [
    RouterOutlet, // ✅ Required for routing
    MenuComponent,
    HeroComponent,
    HomepageComponent,
    ArticleComponent,
    FooterComponent,
    AboutComponent,
    LoginComponent,
    P404Component,
    HttpClientModule
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent {
  title = 'personal-budget-angular';
}
