import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { Login } from './login';

import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginURL: string= "http://localhost:8080/users"
  private currUser : Login|undefined;

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  /** GET users from the server */
  getUsers(): Observable<Login[]> {
    return this.http.get<Login[]>(this.loginURL)
      .pipe(
        tap(_ => this.log('fetched users')),
        catchError(this.handleError<Login[]>('getusers', []))
      );
  }

  /* GET user whose username is search term */
  findUser(username: string): Observable<Login> {
    return this.http.get<Login>(`${this.loginURL}/?username=${username}`).pipe(
      tap(x => x ? this.log(`user ${username} logged in`) : this.log(`no user found`)),
      catchError(this.handleError<Login>(`findUser name=${username}`))
    );
  }
  /* GET user whose isLoggedIn status is true */
  getLoggedInUser(): Observable<Login>{
    return this.http.get<Login>(`${this.loginURL}/?isLoggedIn=${true}`).pipe(
      tap(x => x ? this.log(`user ${x.username} found`) : this.log(`no user found`)),
      catchError(this.handleError<Login>(`getLoggedInUser=${true}`))
    );
  }

  /** POST: add a new user to the server */
  addUser(user: Login): Observable<Login> {
    return this.http.post<Login>(this.loginURL, user, this.httpOptions).pipe(
      tap((newUser: Login) => this.log(`added new user ${newUser.username}`)),
      catchError(this.handleError<Login>('addUser'))
    );
  }
  
  /** PUT: update the user on the server */
  updateUser(user: Login): Observable<any> {
    return this.http.put(this.loginURL, user, this.httpOptions).pipe(
      tap(_ => this.log(`updated user login status for user ${user.username}}`)),
      catchError(this.handleError<any>('updateUserStatus'))
    );
  }
  
  testMessage(user: Login): void{
    if(user){
      this.log(`user is defined`);
    }
    else{
      this.log(`user is undefined`);
    }
  }
  /**
   * HandleError and Log functions
   */

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a LoginService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`LoginService: ${message}`);
  }

  //Shopping cart methods

  setCurrUser(user : Login | undefined){
    this.currUser = user;
  }

  getCurrUser() : Login|undefined{
    if (this.currUser != undefined){
      return this.currUser;
    }
    return undefined;
  }
  logOut(){
    if( this.currUser != undefined){
      this.currUser.isLoggedIn = false;
      this.updateUser(this.currUser).subscribe(currUser => this.currUser = currUser);
    }
  }


}
