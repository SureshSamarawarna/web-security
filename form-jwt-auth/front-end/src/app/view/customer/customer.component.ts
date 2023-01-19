import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

type Customer = {
  id: string,
  name: string,
  address: string,
  contact: string
}

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {

  readonly apiUrl = 'http://localhost:8080/app/api/customers';
  customerList: Array<Customer> = [];

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
    const token = localStorage.getItem("token");
    this.http.get<Customer[]>(this.apiUrl, {
      headers: {
        "Authorization": "Bearer " + token
      }
    }).subscribe({
      next: customers => this.customerList = customers,
      error: err => {
        if (err.status === 401){
          localStorage.removeItem("token");
          this.router.navigateByUrl('login');
        }
      }
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigateByUrl('login');
  }
}
