import { Component, Input } from '@angular/core';

import { Product } from '../product';

import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { ProductService } from '../product.service';
import { Login } from '../login';
import { SharingService } from '../sharing-service';
import { ShoppingCartService } from '../shopping-cart.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
/**
 * The data attached to the product-detal component
 */
export class ProductDetailComponent {

  product: Product | undefined;
  products: Product[] = [];
  currentUser: Login|undefined= undefined;
  isLoggedIn: boolean= false;
  isAdmin: boolean= false;
/**
 * The constructor of the product-detal component
 * @param route - imported to aid the functions
 * @param productService - imported to aid the functions
 * @param location - imported to aid the functions
 * @param shareService - imported to aid the functions
 * @param shoppingCartService - imported to aid the functions
 */
  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private shareService: SharingService,
    private shoppingCartService: ShoppingCartService,
    private router: Router
  ) {}
  
  /**
   * The function initializes the following data upon opening the page
   */
  ngOnInit(): void {
    this.getProduct();
    this.currentUser= this.shareService.getData();
    this.currentUser= this.shareService.getData();
    if(this.currentUser){
      this.isLoggedIn= true;
      if(this.currentUser.username === "admin"){
        this.isAdmin= true;
      }
    }
  }
  /**
   * The function is used to get a single desired product
   */
  getProduct(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProduct(id)
      .subscribe(product => this.product = product);
  }

  /**
   * The function serves as the operator of the go-back button
   */
  goBack(): void {
    this.location.back();
  }

  /**
   * The function saves the changes made to an element
   */
  save(): void {
    if (this.product) {
      this.productService.updateProduct(this.product)
        .subscribe(() => this.goBack());
    }
  }
  /**
   * The function deletes a product from the store
   * @param product - the product to be deleted
   */
  delete(product: Product): void {
    this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe();
    this.goBack();
  }

  /**
   * Adds a selected product to the cart
   * @param product - the product to be added to the cart
   * 
   */
  addToCart(product: Product) {
    if(this.currentUser){
      let flag: boolean = false;
      let lenCart= this.currentUser.cart.length;
      for(let i= 0;i <lenCart; i++){
        if(product.id === this.currentUser.cart[i].id){
          flag= true;
          break;
        }
      }
      if (product.quantity == 0) {
        window.alert("This product is currently out of stock");
      }
      else if(flag){
        window.alert("This product is already in the cart. If you wish to add more quantity to your order you can do so from the cart");
        this.router.navigateByUrl("/cart");
      }
      else {
        this.shoppingCartService.addToCartMain(product);
      }
    }
  }

  getImage(product: Product): void{
    var prodname = product.name;
    prodname = prodname.replaceAll(' ', '_');
    const path = 'assets\\images\\' + prodname + '.png';
    document.getElementById('imagebox').innerHTML = '<img src=' + path + ' id="imageBox" alt="Problem loading image" height=200px/>';
  }
}
