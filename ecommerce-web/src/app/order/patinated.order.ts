import { Order } from "./order";

export class PaginatedOrder {
    length:number;
    pageIndex:number;
    pageSize:number;
    previousPageIndex:number;
    orders:Order[];
}