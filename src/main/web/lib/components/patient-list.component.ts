import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { PatientService } from '../services/patient.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@alfresco/adf-core';
import { Patient } from '../model/patient.model';

@Component({
    selector: 'lamis-patients',
    templateUrl: './patient-list.component.html'
})
export class PatientListComponent implements OnInit, OnDestroy {
    @Input()
    path: string;
    page = 0;
    rows = new Array<Patient>();
    patients: Patient[];
    loading = false;
    public itemsPerPage: number = 10;
    public currentSearch: string = '';
    totalItems = 0;

    links = [
        {
            state: 'patients',
            icon: 'person_outline',
            tooltip: 'Add new Patient',
            roles: []
        }];
    display = 'list';

    constructor(private patientService: PatientService,
                protected notification: NotificationService,
                protected router: Router,
                protected activatedRoute: ActivatedRoute) {
        this.currentSearch = '';
    }

    ngOnDestroy(): void {
    }

    ngOnInit(): void {
        this.onPageChange(0);
    }

    searchPatient(search: any) {
        this.currentSearch = search;
        this.page = 0;
        this.loadAll();
    }


    public select(data: any): any {
        if(!!this.path){
            this.router.navigateByUrl(`${this.path}/${data.obj.id}`)
        }
        else {
            this.router.navigate(['..', 'patients', data.obj.id, 'view'], {relativeTo: this.activatedRoute});
        }
    }

    onPageChange(pageInfo) {
        this.page = pageInfo;
        this.loadAll();
    }

    loadPage(page: number) {
        if (page !== this.page) {
            this.page = page;
            this.loadAll();
        }
    }

    loadAll() {
        this.loading = true;
        this.patientService.search({
            query: this.currentSearch,
            page: this.page,
            size: this.itemsPerPage,
            sort: ['id', 'asc']
        }).subscribe(
            (res: any) => {
                this.onSuccess(res.body, res.headers)
            },
            (res: any) => this.onError(res)
        );
    }

    protected onSuccess(data: any, headers: any) {
        this.patients = data;
        this.totalItems = headers.get('X-Total-Count');
        this.rows = data;
        this.loading = false;
    }

    private onError(error: any) {
        this.notification.openSnackMessage(error.message);
        this.loading = false;
    }
}

