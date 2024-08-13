import { Component, OnInit } from '@angular/core';
import { Login } from '../login';
import { Product } from '../product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  products: Product[] = [];
  currentUser: Login | undefined;

  constructor(private productService: ProductService) { }

  /**
   * The function that initializes the dashboard data
   */
  ngOnInit(): void {
    this.getProducts();
  }

  /**
   * function created to fetch products when desired
   */
  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(0, 21));
  }
}