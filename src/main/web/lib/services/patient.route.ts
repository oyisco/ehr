import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { PagingParamsResolve, UserRouteAccessService } from '@lamis/web-core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PatientService } from './patient.service';
import { Patient } from '../model/patient.model';
import { PatientDetailsComponent } from '../components/patient-details.component';
import { PatientEditComponent } from '../components/patient-edit.component';
import { PatientListComponent } from '../components/patient-list.component';

@Injectable({providedIn: 'root'})
export class PatientResolve implements Resolve<Patient> {
    constructor(private service: PatientService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Patient> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Patient>) => response.ok),
                map((patient: HttpResponse<Patient>) => patient.body)
            );
        }
        return of(<Patient>{});
    }
}

export const PatientRoutes: Routes = [
    {
        path: '',
        data: {
            title: 'Patients',
            breadcrumb: 'PATIENTS'
        },
        children: [
            {
                path: '',
                component: PatientListComponent,
                resolve: {
                    pagingParams: PagingParamsResolve
                },
                data: {},
            },
            {
                path: ':id/view',
                component: PatientDetailsComponent,
                resolve: {
                    entity: PatientResolve
                },
                data: {
                    authorities: ['ROLE_USER'],
                    title: 'Patient Details',
                    breadcrumb: 'PATIENT DETAILS'
                },
                //canActivate: [UserRouteAccessService]
            },
            {
                path: 'new',
                component: PatientEditComponent,
                data: {
                    authorities: ['ROLE_DEC'],
                    title: 'Add Patient',
                    breadcrumb: 'ADD PATIENT'
                },
                //canActivate: [UserRouteAccessService]
            },
            {
                path: ':id/edit',
                component: PatientEditComponent,
                resolve: {
                    entity: PatientResolve
                },
                data: {
                    authorities: ['ROLE_DEC'],
                    title: 'Patient Edit',
                    breadcrumb: 'PATIENT EDIT'
                },
                //canActivate: [UserRouteAccessService]
            }
        ]
    }
];

