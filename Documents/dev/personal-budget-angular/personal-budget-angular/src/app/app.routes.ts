import { Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { ArticleComponent } from './article/article.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { P404Component } from './p404/p404.component';

export const routes: Routes = [
  { path: '', component: HomepageComponent, pathMatch: 'full'}, // Default route
  { path: 'article', component: ArticleComponent }, // Article page
  { path: 'about', component: AboutComponent }, // About page
  { path: 'login', component: LoginComponent }, // Login page
  { path: '**', component: P404Component } // Error PAGE
];

