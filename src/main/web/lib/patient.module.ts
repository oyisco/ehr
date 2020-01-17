import { CoreModule } from '@alfresco/adf-core';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FlexLayoutModule, MediaMarshaller } from '@angular/flex-layout';
import {
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDividerModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatProgressBarModule,
    MatSelectModule,
    MatTabsModule
} from '@angular/material';
import { RouterModule } from '@angular/router';
import { CovalentMessageModule, CovalentSearchModule } from '@covalent/core';
import { JsonFormModule, LamisSharedModule } from '@lamis/web-core';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { PatientDetailsComponent } from './components/patient-details.component';
import { PatientEditComponent } from './components/patient-edit.component';
import { PatientListComponent } from './components/patient-list.component';
import { PatientRoutes } from './services/patient.route';

@NgModule({
    declarations: [
        PatientListComponent,
        PatientDetailsComponent,
        PatientEditComponent
    ],
    imports: [
        CommonModule,
        NgJhipsterModule,
        LamisSharedModule,
        JsonFormModule,
        MatInputModule,
        MatIconModule,
        MatDividerModule,
        MatCardModule,
        MatSelectModule,
        MatButtonModule,
        MatTabsModule,
        FlexLayoutModule,
        RouterModule.forChild(PatientRoutes),
        MatProgressBarModule,
        CovalentMessageModule,
        MatListModule,
        MatChipsModule,
        CoreModule,
        CovalentSearchModule,
        NgbPaginationModule
    ],
    exports: [
        PatientListComponent,
        PatientDetailsComponent,
        PatientEditComponent
    ],
    providers: []
})
export class PatientModule {
}
