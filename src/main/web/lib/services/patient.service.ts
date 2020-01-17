import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption, DATE_FORMAT, SERVER_API_URL_CONFIG, ServerApiUrlConfig } from '@lamis/web-core';
import { map } from 'rxjs/operators';
import { Patient } from '../model/patient.model';

import * as moment_ from 'moment';

const moment = moment_;

type EntityResponseType = HttpResponse<Patient>;
type EntityArrayResponseType = HttpResponse<Patient[]>;


@Injectable({providedIn: 'root'})
export class PatientService {
    public resourceUrl = '';
    public resourceSearchUrl = '';

    constructor(protected http: HttpClient, @Inject(SERVER_API_URL_CONFIG) private serverUrl: ServerApiUrlConfig) {
        this.resourceUrl = serverUrl.SERVER_API_URL + '/api/patients';
        this.resourceSearchUrl = serverUrl.SERVER_API_URL + '/api/_search/patients';
    }

    create(patient: Patient): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(patient);
        return this.http
            .post<Patient>(this.resourceUrl, copy, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(patient: Patient): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(patient);
        return this.http
            .put<Patient>(this.resourceUrl, copy, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Patient>(`${this.resourceUrl}/${id}`, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Patient[]>(this.resourceUrl, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .post<Patient[]>(this.resourceSearchUrl, req, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    reindex() {
        return this.http.get(this.resourceUrl + '/re-index');
    }

    protected convertDateFromClient(patient: Patient): Patient {
        const copy: Patient = Object.assign({}, patient, {
            dob: patient.dateBirth != null && patient.dateBirth.isValid() ? patient.dateBirth.format(DATE_FORMAT) : null,
            dod: patient.dateRegistration != null && patient.dateRegistration.isValid() ? patient.dateRegistration.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.name = res.body.surname + ', ' + res.body.otherNames;
            res.body.dateBirth = res.body.dateBirth != null ? moment(res.body.dateBirth) : null;
            res.body.dateRegistration = res.body.dateRegistration != null ? moment(res.body.dateRegistration) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((patient: Patient) => {
                patient.name = patient.surname + ', ' + patient.otherNames;
                patient.dateBirth = patient.dateBirth != null ? moment(patient.dateBirth) : null;
                patient.dateRegistration = patient.dateRegistration != null ? moment(patient.dateRegistration) : null;
            });
        }
        return res;
    }
}
