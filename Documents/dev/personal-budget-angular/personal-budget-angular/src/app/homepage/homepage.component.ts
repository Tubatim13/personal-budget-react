import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { ArticleComponent } from '../article/article.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { isPlatformBrowser } from '@angular/common';

interface BudgetItem {
  title: string;
  budget: number;
}

@Component({
  selector: 'pb-homepage',
  standalone: true,
  imports: [ArticleComponent, HttpClientModule],
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {
  public dataSource = {
    datasets: [
      {
        data: [] as number[],
        backgroundColor: [
          '#ffcd56',
          '#ff6384',
          '#36a2eb',
          '#fd6b19',
        ]
      }
    ],
    labels: [] as string[]
  };

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: object) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.http.get<{ myBudget: BudgetItem[] }>('http://localhost:3000/budget')
      .subscribe({
        next: (res) => {
          res.myBudget.forEach((item, i) => {
            this.dataSource.datasets[0].data[i] = item.budget;
            this.dataSource.labels[i] = item.title;
          });

          if (isPlatformBrowser(this.platformId)) {
            this.createChart();
          }
        },
        error: (err) => {
          console.error('Error fetching budget:', err);
          alert('Failed to connect to the backend. Ensure the API is running.');
        }
      });
  }

  createChart() {
    if (isPlatformBrowser(this.platformId)) {
      const canvas = document.getElementById('myChart') as HTMLCanvasElement;
      if (canvas) {
        new Chart(canvas.getContext('2d')!, {
          type: 'pie',
          data: this.dataSource,
          options: {
            responsive: true,
            maintainAspectRatio: false
          }
        } as ChartConfiguration);
      } else {
        console.error('Canvas element not found');
      }
    }
  }
}
