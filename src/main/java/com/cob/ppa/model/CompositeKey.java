package com.cob.ppa.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
public class CompositeKey {
    private String emrId;
    private LocalDate dos;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return emrId == that.emrId && dos.equals(that.dos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emrId, dos);
    }

    @Override
    public String toString() {
        return "CompositeKey{" + "id=" + emrId + ", dos='" + dos + '\'' + '}';
    }
}
