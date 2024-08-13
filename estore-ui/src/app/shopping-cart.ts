import { Product } from "./product";

export interface ShoppingCart{

    shoppingCart : Map<Product, number>;
    isCompleteOrder : boolean;
    totalCost : number;
}