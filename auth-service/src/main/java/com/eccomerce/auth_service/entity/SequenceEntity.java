package com.eccomerce.auth_service.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEQUENCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SequenceEntity {
    @Id
    @Column(name = "SEQUENCE_PARAM")
    private String sequenceParam;
    @Column(name = "SEQUENCE_VALUE")
    private Long sequenceValue;

    public String getId() {
        return sequenceParam;
    }

    public void setId(String sequenceParam) {
        this.sequenceParam = sequenceParam;
    }
}
