import { Component } from '@angular/core';
import { Patient } from '../model/patient.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'lamis-patient',
    templateUrl: './patient-details.component.html'
})
export class PatientDetailsComponent {
    template = 'patient-details';
    entity: Patient;

    constructor(private router: Router, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.data.subscribe(({entity}) => {
            this.entity = !!entity && entity.body ? entity.body : entity;
        });
    }

    previousState() {
        window.history.back();
    }
}
