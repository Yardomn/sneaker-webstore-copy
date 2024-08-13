import { Injectable } from '@angular/core';

/**
 * pass inventory
 */
@Injectable({
    providedIn: 'root',
})
export class ProductSharingService{
    private data:any = undefined;

    setData(data:any){
        this.data = data;
    }

    getData():any{
        return this.data;
    }
}