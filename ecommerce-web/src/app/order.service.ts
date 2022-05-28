import { HttpClient, HttpParams, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Observable } from 'rxjs';
import { Order } from './order/order';
import { PaginatedOrder } from './order/patinated.order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private orderUrl = 'http://localhost:8080/ecommsapi/orders/get';
  private uploadUrl = 'http://localhost:8080/ecommsapi/orders/upload';

  constructor(private http: HttpClient) { }

  getOrders(event?:PageEvent): Observable<PaginatedOrder> {
    event?.previousPageIndex
    var input = {
      length : event?.length,
      pageIndex : event?.pageIndex,
      pageSize : event?.pageSize,
      previousPageIndex : event?.previousPageIndex
    }
    
    return this.http.post<PaginatedOrder>(this.orderUrl, input);
  }

  getOrders1(): Observable<any> {
    return this.http.get<any>(this.orderUrl);
  }

  uploadCsv(file: File):Observable<any>{
    const formdata : FormData = new FormData();
    formdata.append('file', file);
    const req = new HttpRequest('POST', this.uploadUrl, formdata, {
      responseType : 'json'
    });
    return this.http.request(req);
  }
  
}
