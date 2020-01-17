import { NgModule } from '@angular/core';
import { PatientSettingComponent } from './components/patient-setting.component';
import {
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule
} from '@angular/material';
import { LamisSharedModule } from '@lamis/web-core';
import { RouterModule } from '@angular/router';
import { PatientSettingsRoutes } from './services/settings.route';
import { CommonModule } from '@angular/common';
import { MediaMarshaller } from '@angular/flex-layout';

@NgModule({
    declarations: [
        PatientSettingComponent
    ],
    exports: [
        PatientSettingComponent
    ],
    imports: [
        CommonModule,
        MatButtonModule,
        LamisSharedModule,
        MatProgressSpinnerModule,
        MatCardModule,
        MatCardModule,
        MatIconModule,
        MatTooltipModule,
        RouterModule.forChild(PatientSettingsRoutes)
    ]
})
export class PatientSettingsModule {
}
