import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Login } from '../login';
import { SharingService } from '../sharing-service';
import { LoginService } from '../login.service';
import { ProductSharingService } from '../product-sharing-service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit{
  /** Object meant to hold the current user who is logged in. Undefined if no user is logged in */
  currentUser: Login | undefined;
  /** Variable meant to check if the user has checked out yet or not */
  hasCheckedOut: boolean= false;
  /** Variable to store the total price the user is about to pay in fiction */
  total: number | undefined; 
  constructor(
    private sharingService: SharingService,
    private router: Router,
    private userService: LoginService,
    private productSharingService: ProductSharingService
  ){}
  /**
   * Lines of code to be executed upon the page being loaded up
   */
  ngOnInit(): void {
    this.currentUser= this.sharingService.getData();
    this.total= this.productSharingService.getData();
  }

  /**
   * Function to check out the cart of the current user
   */
  checkout(): void{
    if(this.currentUser){
      this.currentUser.cart =[];
      this.hasCheckedOut= true;
      this.sharingService.setData(this.currentUser);
      this.userService.updateUser(this.currentUser).subscribe();
    }
  }
  /**
   * Function to return the user to the dashboard
   */
  goToDashboard(): void{
    this.router.navigateByUrl("/dashboard");
  }
}
