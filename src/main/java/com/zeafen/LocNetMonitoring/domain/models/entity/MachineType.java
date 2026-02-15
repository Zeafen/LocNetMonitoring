package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.Set;


@Table
@Entity(name = "machine_types")
public class MachineType {
    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "smallint")
    @SequenceGenerator(
            name = "machine_types_id_seq",
            sequenceName = "machine_types_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "machine_types_id_seq"
    )
    private Short id;

    @Size(min = 5, max = 25, message = "Наименование должно составлять от 5 до 25 символов")
    @Column(nullable = false, length = 25, name = "name")
    private String name;

    @OneToMany(mappedBy = "type")
    private Collection<RequestCode> typeRequestCodes;

    @OneToMany(mappedBy = "type")
    private Collection<MachineModel> typeMachineModels;

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

    public Collection<RequestCode> getTypeRequestCodes() {
        return typeRequestCodes;
    }

    public void setTypeRequestCodes(final Set<RequestCode> typeRequestCodes) {
        this.typeRequestCodes = typeRequestCodes;
    }

    public Collection<MachineModel> getTypeMachineModels() {
        return typeMachineModels;
    }

    public void setTypeMachineModels(final Set<MachineModel> typeMachineModels) {
        this.typeMachineModels = typeMachineModels;
    }
}
