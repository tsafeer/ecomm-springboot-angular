import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { OrderService } from '../order.service';
import { Order } from './order';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  public orderData: any;
  public pageEvent: PageEvent;
  public datasource: any;
  public pageIndex: number = 1;
  public pageSize: number = 5;
  public length: number = 0;

  displayedColumns: string[] = ['orderID', 'orderedBy', 'orderDate', 'orderPriority', 'region', 
                'country', 'itemType', 'salesChannel', 'shipDate', 'unitsSold', 'unitPrice', 'unitCost',
                 'totalRevenue', 'totalCost', 'totalProfit'];
  
  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    // this.orderService.getOrders1().subscribe((data) => {
    //   console.log('data', data);
    //   this.orderData = data;
    // });
    this.pageEvent = new PageEvent();
    this.pageEvent.pageSize = 5;
    this.pageEvent.pageIndex = 0;
    this.getServerData(this.pageEvent);
  }

  public getServerData(event: PageEvent) {
    this.orderService.getOrders(event).subscribe((response) => {
      console.log('response', response);
      
        this.datasource = new MatTableDataSource<Order>(response.orders);
        this.pageIndex = response.pageIndex;
        this.pageSize = response.pageSize;
        this.length = response.length;
       
      
    });
    return event;
  }
}
