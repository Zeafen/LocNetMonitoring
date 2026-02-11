package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Collection;


@Entity(name = "machine_models")
public class MachineModel {
    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "smallint")
    @SequenceGenerator(
            name = "machine_models_id_seq",
            sequenceName = "machine_models_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "machine_models_id_seq"
    )
    private Short id;

    @Size(min = 5, max = 25, message = "Наименование должно составлять от 5 до 25 символов")
    @Column(nullable = false, length = 25)
    private String name;

    @Min(1)
    @Column(nullable = false, columnDefinition = "smallint")
    private Short yearsExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private MachineType type;

    @OneToMany(mappedBy = "model")
    private Collection<Machine> modelMachines ;

    @OneToMany(mappedBy = "model")
    private Collection<ModelStandard> modelModelStandards ;

    public Short getId() {
        return id;
    }

    public void setId(final Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Short getYearsExpire() {
        return yearsExpire;
    }

    public void setYearsExpire(final Short yearsExpire) {
        this.yearsExpire = yearsExpire;
    }

    public MachineType getType() {
        return type;
    }

    public void setType(final MachineType type) {
        this.type = type;
    }

    public Collection<Machine> getModelMachines() {
        return modelMachines;
    }

    public void setModelMachines(final Collection<Machine> modelMachines) {
        this.modelMachines = modelMachines;
    }

    public Collection<ModelStandard> getModelModelStandards() {
        return modelModelStandards;
    }

    public void setModelModelStandards(Collection<ModelStandard> modelModelStandards) {
        this.modelModelStandards = modelModelStandards;
    }
}
