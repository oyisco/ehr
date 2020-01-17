import { Facility } from './facility.model';
import { Moment } from 'moment';

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    MALE_TO_FEMALE = 'MALE_TO_FEMALE',
    FEMALE_TO_MALE = 'FEMALE_TO_MALE'
}

export const enum MaritalStatus {
    SINGLE = 'SINGLE',
    MARRIED = 'MARRIED',
    CONCUBINAGE = 'CONCUBINAGE',
    WIDOWED = 'WIDOWED',
    DIVORCED = 'DIVORCED',
    SEPARATED = 'SEPARATED'
}

export const enum BloodType {
    A = 'A',
    B = 'B',
    AB = 'AB',
    O = 'O'
}

export const enum Rhesus {
    POS = 'POS',
    NEG = 'NEG'
}

export const enum HB {
    AA = 'AA',
    AS = 'AS',
    SS = 'SS',
    SC = 'SC',
    CC = 'CC',
    ATHAL = 'ATHAL',
    BTHAL = 'BTHAL'
}

export interface Patient {
    facility?: Facility;
    id?: number;
    hospitalNum?: string;
    uniqueId?: string;
    name?: string;
    surname?: string;
    otherNames?: string;
    gender?: Gender;
    dateBirth?: Moment;
    dod?: Moment;
    active?: boolean;
    causeOfDeath?: string;
    maritalStatus?: MaritalStatus;
    bloodType?: BloodType;
    rhesus?: Rhesus;
    hb?: HB;
    criticalInfo?: string;
    generalInfo?: string;
    education?: string;
    occupation?: string;
    address?: string;
    phone?: string;
    nextKin?: string;
    addressKin?: string;
    phoneKin?: string;
    relationKin?: string;
    entryPoint?: string;
    targetGroup?: string;
    dateConfirmedHiv?: Moment;
    dateEnrolledPMTCT?: Moment;
    sourceReferral?: string;
    timeHivDiagnosis?: string;
    tbStatus?: string;
    pregnant?: boolean;
    breastfeeding?: boolean;
    dateRegistration?: Moment;
    statusRegistration?: string;
    enrollmentSetting?: string;
    dateStarted?: Moment;
    currentStatus?: string;
    dateCurrentStatus?: Moment;
    lastViralLoad?: number;
    lastCd4?: number;
    dateLastCd4?: Moment;
    dateLastViralLoad?: Moment;
    viralLoadDueDate?: Moment;
    viralLoadType?: string;
    dateLastRefill?: Moment;
    dateNextRefill?: Moment;
    lastRefillDuration?: number;
    lastRefillSetting?: string;
    dateLastClinic?: Moment;
    dateNextClinic?: Moment;
    dateTracked?: Moment;
    outcome?: string;
    causeDeath?: string;
    agreedDate?: Moment;
    sendMessage?: boolean;
    biometric?: boolean
}
