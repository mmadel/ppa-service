package com.cob.ppa.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
public class PatientMedicalRecordId implements Serializable {
    private String emrId;
    private LocalDate appointmentDate;

    public PatientMedicalRecordId() {
    }

    public PatientMedicalRecordId(String id, LocalDate appointmentDate) {
        this.emrId = id;
        this.appointmentDate = appointmentDate;
    }

    // equals and hashCode (based on id and appointmentDate)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientMedicalRecordId)) return false;
        PatientMedicalRecordId that = (PatientMedicalRecordId) o;
        return Objects.equals(emrId, that.emrId) &&
                Objects.equals(appointmentDate, that.appointmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emrId, appointmentDate);
    }

}
