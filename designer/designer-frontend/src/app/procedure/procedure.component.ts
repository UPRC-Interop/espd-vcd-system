import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-procedure',
  templateUrl: './procedure.component.html',
  styleUrls: ['./procedure.component.css']
})
export class ProcedureComponent implements OnInit {

  countries=["Greece", "Germany", "France"];
  procedures=["Open Procedure","Restricted Procedure","Competitive procedure with Negotiation", "Competitive dialogue",
              "Innovation Partnership"];
  constructor() { }

  ngOnInit() {
  }

}
