import { Component, OnInit } from '@angular/core';
import { Login } from '../login';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { SharingService } from '../sharing-service';
import { ShoppingCartService } from '../shopping-cart.service';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
/**
 * The data attached to the products component
 */
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  currentUser: Login|undefined;
  isLoggedIn: boolean= false;
  isAdmin: boolean= false;
/**
 *  The constructor of the products componet
 * @param productService - imported to aid the functions
 * @param shareService - imported to aid the functions
 * @param shoppingCartService - imported to aid the functions
 */
  constructor(private productService: ProductService,
    private shareService: SharingService,
    private shoppingCartService: ShoppingCartService
    ) { }
    
  /**
   * The function initializes the following data upon opening the page
   */
  ngOnInit(): void {
    this.getProducts();
    this.currentUser= this.shareService.getData();
    if(this.currentUser){
      this.isLoggedIn= true;
      if(this.currentUser.username === "admin"){
        this.isAdmin= true;
      }
    }
  }
  /**
   * The function serves to get the desired products when requested
   */
  getProducts(): void {
    this.productService.getProducts()
    .subscribe(products => this.products = products);
  }
  /**
   * The function adds the product to the store
   * @param name - the name of the product
   */
  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.productService.addProduct({ name } as Product)
      .subscribe(product => {
        this.products.push(product);
      });
  }
  /**
   * The function deletes the product from the store
   * @param product - the product to be deleted
   */
  delete(product: Product): void {
    this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe();
  }
  /**
   * Adds a selected product to the cart
   * @param product - the product to be added to the cart
   */
  addToCart(product: Product): void {
    if(this.currentUser != undefined) {
        this.shoppingCartService.addToCartMain(product);
    }
  }
}


