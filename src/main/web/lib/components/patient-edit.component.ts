import { Component, OnInit, ViewChild } from '@angular/core';
import { Patient } from '../model/patient.model';
import { PatientService } from '../services/patient.service';
import { NotificationService } from '@alfresco/adf-core';
import { ActivatedRoute } from '@angular/router';
import { MatButton, MatProgressBar } from '@angular/material';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AngularGlobalInjector } from '@qlack/angular-global-injector';

@Component({
    selector: 'lamis-patient-edit',
    templateUrl: './patient-edit.component.html'
})
export class PatientEditComponent implements OnInit{
    template = 'patient-edit';
    @ViewChild(MatProgressBar, {static: true}) progressBar: MatProgressBar;
    @ViewChild(MatButton, {static: true}) submitButton: MatButton;
    entity: Patient;
    isSaving: boolean;
    error = false;
    constructor(private patientService: PatientService,
                protected notification: NotificationService,
                protected activatedRoute: ActivatedRoute) {
        let service: PatientService = AngularGlobalInjector.get(PatientService);
    }

    onData(data: any) {
        this.entity = data;
    }

    createEntity(): Patient {
        return <Patient>{};
    }

    ngOnInit(): void {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({entity}) => {
            this.entity = !!entity && entity.body ? entity.body : entity;
            if (this.entity === undefined) {
                this.entity = this.createEntity();
            }
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.submitButton.disabled = true;
        //this.progressBar.mode = 'indeterminate';
        this.isSaving = true;
        if (this.entity.id !== undefined) {
            this.subscribeToSaveResponse(this.patientService.update(this.entity));
        } else {
            this.subscribeToSaveResponse(this.patientService.create(this.entity));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => {
                this.onSaveError();
                this.onError(res.message)
            });
    }

    private onSaveSuccess(result: any) {
        this.isSaving = false;
        this.notification.openSnackMessage('Patient successfully saved');
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
        this.error = true;
        this.submitButton.disabled = true;
        //this.progressBar.mode = 'determinate';
    }

    protected onError(errorMessage: string) {
        this.notification.showError(errorMessage);
    }

}
