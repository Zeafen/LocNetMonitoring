package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Collection;

@Entity(name = "maintenance_types")
public class MaintenanceType {
    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "maintenance_types_id_seq",
            sequenceName = "maintenance_types_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "maintenance_types_id_seq"
    )
    private Short id;

    @Size(min = 5, max = 25, message = "Наименование должно составлять от 5 до 25 символов")
    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^(\\d+) ?((day)|(month)|(mon)|(year)|([DdyY]))$", message = "Период не соответствует норме")
    private String period;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maintenanceType")
    private Collection<Maintenance> maintenances ;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Collection<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(Collection<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }
}
