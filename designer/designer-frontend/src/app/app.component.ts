import {Component} from '@angular/core';
import {DataService} from './services/data.service';

// import {NgForm, FormControl} from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isLinear: boolean = true;

  constructor(public dataService: DataService) {
  }
}
