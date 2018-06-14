import {Component} from '@angular/core';
import {DataService} from './services/data.service';
import {TranslateService} from '@ngx-translate/core';

// import {NgForm, FormControl} from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isLinear: boolean = true;

  constructor(public dataService: DataService, public translate: TranslateService) {
    translate.setDefaultLang('ESPD_el');
    // translate.setDefaultLang('ESPD_de');
  }
}
