import { Product } from "./product";

export interface Login {
    id: number;
    username: string;
    isLoggedIn: boolean;
    cart: Product[];
}
