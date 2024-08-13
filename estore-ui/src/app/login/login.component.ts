import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Location } from '@angular/common';
import { Login } from '../login';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { SharingService } from '../sharing-service';
import { ProductService } from '../product.service';

/**
 * Component of Login/Logout page
 * @author: team0
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  /** Array of all users registered */
  users: Login[]= [];
  /** Static of type Login or undefined value meant to
   * hold the value of the current user, if one is logged in */
  static currentUser: Login | undefined;
  /**Vartiable to keep track if there is a user logged in or not */
  isLoggedIn: boolean= false;

  constructor(private loginService: LoginService,
    private location: Location,
    private router: Router,
    private share: SharingService){}

  /**
   * Actions to be performed upon the page opening
   */
  ngOnInit(): void {
    this.getUsers();
    if(LoginComponent.currentUser){
      this.isLoggedIn= true;
    }
  }

  /**
   * Function to return the current user Logged In
   * @returns any: current user could be of type Login or undefined
   */
  getCurrentUser(): any{
    //return this.currentUser;
    return LoginComponent.currentUser;
  }
  /**
   * Function to get all users registered
   */
  getUsers(): void {
    this.loginService.getUsers()
    .subscribe(users => this.users = users);
  }
  /**
   * Function to add a new user with the username passed in the parameter
   * @param username : string value containing the username for the new user
   * @returns : void
   */
  add(username: string): void {
    let numUsers= this.users.length;
    username = username.trim();
    if (!username) { return; }
    let newUser= { username } as Login
    newUser.cart=[];
    newUser.id= numUsers + 1;
    this.loginService.addUser(newUser)
      .subscribe(user => {
        this.users.push(user);
      });
    this.router.navigateByUrl("/dashboard");
  }
  /**
   * Function to return to the last page before login
   */
  goBack(): void{
    this.location.back();
  }
  /**
   * Function to log the user in
   * @param username : string value of username of user to log in
   */
  login(username: string): void{
   for(let i= 0;i < this.users.length; i+=1){
    if(username === this.users[i].username){
      LoginComponent.currentUser= this.users[i];
      break;
    }
   }
   if(LoginComponent.currentUser){
    console.log(this.users);
    LoginComponent.currentUser.isLoggedIn= true;
    console.log(LoginComponent.currentUser);
    this.loginService.updateUser(LoginComponent.currentUser).subscribe();
    this.isLoggedIn= true;
    this.share.setData(LoginComponent.currentUser);
    this.router.navigateByUrl("/login");
   }
  }
  /**
   * Function to log out the current user
   */
  logout(): void{
    /** Function to log user out of store */
    if(LoginComponent.currentUser){
      LoginComponent.currentUser.isLoggedIn= false;
      for(let i=0;i<this.users.length;i+=1){
        if(LoginComponent.currentUser.username === this.users[i].username){
          this.users[i]= LoginComponent.currentUser;
          break;
        }
      }
      LoginComponent.currentUser= this.share.getData();
      if(LoginComponent.currentUser)
        this.loginService.updateUser(LoginComponent.currentUser).subscribe();
      LoginComponent.currentUser= undefined;
      this.share.setData(undefined);
      this.isLoggedIn= false;
      this.router.navigateByUrl("/dashboard");
    }
  }
}

