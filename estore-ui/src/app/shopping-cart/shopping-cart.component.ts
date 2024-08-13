import { Component, OnInit } from '@angular/core';
import { ShoppingCart } from '../shopping-cart';
import { ShoppingCartService } from '../shopping-cart.service';
import { Product } from '../product';
import { Login } from '../login';
import { LoginService } from '../login.service';
import { SharingService } from '../sharing-service';
import { ProductService } from '../product.service';
import { ProductSharingService } from '../product-sharing-service';
import { Router } from '@angular/router';


/**
 * shopping-cart.component gets the current user and their assigned cart
 */

 @Component({
  selector: 'app-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css'],
  providers:[SharingService]
})

export class ShoppingCartComponent implements OnInit {
  
  shoppingCart: ShoppingCart | undefined;
  currentUser: Login | undefined;
  products: Product[] | undefined;
  inventory: Product[] = [];
  firstRemoval: boolean = true;
  isComplete:boolean = false;
  isDiscount:boolean = false;
  isAdmin: boolean = false;
  isLoggedIn: boolean= false;
  total: number = 0;
  amountSaved: number = 0;
  isShip :number = 0;
  discount : String = "";
  // all discounts are percent of the total
  discountCodes : [String, number][] = [["SNEAKS",30],["SEWEY", 7],["AAROHAN", 15],["RAY",1]];
  

  constructor(
    private shoppingCartService: ShoppingCartService,
    private userService: LoginService,
    private sharingService: SharingService,
    private productService: ProductService,
    private productSharingService: ProductSharingService,
    private router: Router
    ) { }
    //private sharingService: SharingService,

  /**
   * Initializes data as needed
   */
  ngOnInit(): void {
    this.getProducts();
    this.currentUser = this.sharingService.getData();
    if(this.currentUser){
      this.isLoggedIn= true;
      if(this.currentUser.username === "admin"){
        this.isAdmin= true;
      }
    }
    this.products = this.currentUser?.cart;
    //this.getUser();
    this.getCart();
    if(this.shoppingCart != undefined){
      this.products = Array.from(this.shoppingCart.shoppingCart.keys());
    }
    this.getTotal();
  }

  /**
   * Gets products in inventory
   */
   getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.inventory = products.slice(0, 50));
  }

  /**
   * Gets the current user
   */
  getUser(): void {
    this.currentUser = this.userService.getCurrUser();
  }

  /**
   * Gets the customer's cart when they are logged in 
   */
  getCart(): void {
    if(this.currentUser != undefined){
      this.shoppingCartService.getCart(this.currentUser.id).subscribe(shoppingCart => this.shoppingCart = shoppingCart);
    }
  }

  /**
   * gets the current quantity of product from inventory
   * @param inventory
   * @param product
   */
   updateCartWithInventory (inventory: Product[], product: Product): number {
    let name = product.name;
    let quantity = 0;
    inventory.forEach(element => {
      if (element.name === name) {
        quantity = element.quantity + 1;
      }
    });
    return quantity;
  }


  /**
   * Removes selected product from the user's cart
   *
   * @param product
   */
  removeFromCart(product: Product): void {
   if(this.currentUser){
    let lenCart= this.currentUser.cart.length;
    let lenInventory= this.inventory.length;
    let i =0; //loop variable
    let inventoryIndex= -1;
    for(i=0;i<lenInventory;i++){
      if(this.inventory[i].id === product.id){
        inventoryIndex= i;
        break;
      }
    }
    if(inventoryIndex < 0){
      window.alert("Product does not exist in inventory");
      return;
    }
    for(i=0;i<lenCart;i++){
      if(this.currentUser.cart[i].id === product.id){
        this.shoppingCartService.updateQuantityRemove(
          this.currentUser.cart[i], this.currentUser.cart[i].quantity + this.inventory[inventoryIndex].quantity
          );
        this.currentUser.cart.splice(i,1);
        this.getTotal();
        return;
      }
    }
    window.alert("Product does not exist in cart. How on earth were you even able to trigger this? I'd give you an award for this if I weren't just text in an alert window. You're still here? Why? You can click the ok button now, you know. Seriously? You're still reading this? You know what? I'm going to play the silent game by myself, since maybe then you'll click the OK button. 3,2,1, go! <awkward silence>");
   }
  }

  /**
   * sets user status to logged out and brings them to the login page
   */
  cartLogOut () {
    this.userService.logOut();
  }

  /**
   * clears user's cart
   */
  completeOrder(): void {
    this.currentUser = this.sharingService.getData();
    if (this.currentUser != undefined) {
      let cart = this.currentUser.cart;
      let length = cart.length;
      for (var i = 0; i < length; i++) {
        cart.pop();
      }
      this.currentUser.cart = cart;
      this.userService.updateUser(this.currentUser).subscribe(currentUser => this.currentUser = currentUser);
      this.sharingService.setData(this.currentUser);
      this.isComplete = true;
    }
  }
  /**
   * gets the total cost of all the items within the cart
   */
  getTotal() {
    this.currentUser = this.sharingService.getData();
    let total = 0;
    if (this.currentUser != undefined) {
      if(this.currentUser.cart.length == 0){
        this.total= 0;
        return;
      }
      this.products = this.currentUser?.cart;
      this.products.forEach(product => {
        total = total + (product.price*product.quantity);
      });
    }
    this.total = total;
  }
  /**
   * checks to see if the discount code is valid and updates isDiscount as needed
   */
  applyDiscountCode() {
    if(this.currentUser.cart.length == 0){
      window.alert("Please add products to the cart first before choosing this!");
      return;
    }
    this.discount=((document.getElementById("dcode") as HTMLInputElement).value);

    var i : number = 0;
    while(this.discountCodes[i]){
      let code = this.discountCodes[i];
      if(this.discount == code[0]) {
        let substract = this.total * (code[1]/100);
        this.total = this.total - substract;
        this.isDiscount = true;
        this.amountSaved = substract;
      }
      i++;
    }
    if (!this.isDiscount) {
      window.alert("This discount code is wrong");
    }
  }
  /**
   * Function to apply normal shipping to the order
   */
  applyNormalShipping(){
    if(this.currentUser.cart.length == 0){
      window.alert("Please add products to the cart first before choosing this!");
      return;
    }
    if (this.isShip == 1){
    this.total -=5;
    this.isShip = 0;
  }
  if (this.isShip == 2){
    this.total -=10;
    this.isShip = 0;
  }
  }
  /**
   * Function to apply expedited shipping to the order
   */
  applyExpediteShipping(){
    if(this.currentUser.cart.length == 0){
      window.alert("Please add products to the cart first before choosing this!");
      return;
    }
    if (this.isShip == 0){
    this.total +=5;
    this.isShip = 1;
  }
  if (this.isShip == 2){
    this.total -=5;
    this.isShip = 1;
  }
  }
  /**
   * Function to apply expedited shipping to the order
   */
  applyPremiumShipping(){
    if(this.currentUser.cart.length == 0){
      window.alert("Please add products to the cart first before choosing this!");
      return;
    }
    if (this.isShip == 0){
      this.total +=10;
      this.isShip = 2;
    }
    if (this.isShip == 1){
      this.total +=5;
      this.isShip = 2;
    }
  }
  /**
   * Function to increase the quantity of a product by 1 and reflect the change in the product inventory
   * @param product Product whose quantity is to be increased by one
   * @returns void
   */
  addQuantity(product: Product): void{
    let l2= this.inventory.length;
    for(let i= 0; i<l2; i++){
      if(this.inventory[i].id === product.id){
        if(this.inventory[i].quantity == 0){
          window.alert("We do not have more stock of this product.");
          return;
        }
        this.inventory[i].quantity-=1;
        this.productService.updateProduct(this.inventory[i]).subscribe();
        break;
      }
    }
    if(this.products){
      let l1= this.products.length;
      for(let i= 0; i<l1; i++){
        if(this.products[i].id === product.id){
          this.products[i].quantity+=1;
        }
      }
    }
    this.getTotal();
    this.router.navigateByUrl("/cart");
  }
  /**
   * Function to reduce the quantity of a product by 1 and reflect the change in the product inventory
   * @param product Product whose quantity is to be reduced by 1
   * @returns void
   */
  reduceQuantity(product: Product): void{
    if(this.products){
      let l1= this.products.length;
      for(let i= 0; i<l1; i++){
        if(this.products[i].id === product.id){
          if(this.products[i].quantity == 1){
            window.alert("You can have a minimum of one of a product in the cart. If you no longer wish to purchase this product, please remove it.")
            return;
          }
          this.products[i].quantity-=1;
        }
      }
    }
    let l2= this.inventory.length;
    for(let i= 0; i<l2; i++){
      if(this.inventory[i].id === product.id){
        this.inventory[i].quantity+=1;
        this.productService.updateProduct(this.inventory[i]).subscribe();
      }
    }
    this.getTotal();
  }

  goToCheckout(): void{
    if(this.currentUser){
      if(this.currentUser.cart.length == 0){
        window.alert("You need to have products in the cart to check out!");
        return;
      }
    }
    this.productSharingService.setData(this.total);
    this.router.navigateByUrl("/checkout");
  }
}