import { Injectable } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Login } from './login';


@Injectable({
    providedIn: 'root',
})
export class SharingService{
    private static data: Login | undefined = undefined;

    setData(data:any){
        SharingService.data = data;
    }

    getData():any{
        return SharingService.data;
    }
}