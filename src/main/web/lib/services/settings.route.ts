import { Routes } from '@angular/router';
import { PatientSettingComponent } from '../components/patient-setting.component';

export const PatientSettingsRoutes: Routes = [
    {
        path: '',
        data: {
            title: 'Patient Settings',
            breadcrumb: 'PATIENT SETTINGS'
        },
        children: [
            {
                path: '',
                component: PatientSettingComponent,
                data: {},
            }
        ]
    }
];
