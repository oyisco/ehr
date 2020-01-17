import { Component } from '@angular/core';
import { PatientService } from '../services/patient.service';

@Component({
    selector: 'patient-settings',
    templateUrl: './patient-setting.component.html'
})
export class PatientSettingComponent {
    indexing = false;

    constructor(private patientService: PatientService) {
    }

    index() {
        this.indexing = true;
        this.patientService.reindex().subscribe(res => this.indexing = false);
    }
}
