import {Component, Inject} from '@angular/core';
import {NgForm} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers:[]
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  readonly apiUrl = 'http://localhost:8080/app/api/auth/login';
  invalid = false;

  constructor(private http: HttpClient, private router: Router) {
  }

  authenticate(username: HTMLInputElement, password: HTMLInputElement): void {
    this.invalid = false;

    if (this.username.trim().length === 0){
      username.select();
      username.focus();
      return;
    }else if (this.password.length === 0){
      password.select();
      password.focus();
      return;
    }

    const payload = `username=${this.username}&password=${this.password}`;
    this.http.post<string>(this.apiUrl, payload, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).subscribe({
      next: jws => {
        localStorage.setItem("token", jws);
        this.router.navigateByUrl("app");
      },
      error: err => {
        if (err.status === 401){
          this.invalid = true;
          username.select();
          username.focus();
        }else{
          console.error(err);
        }
      }
    });
  }
}
