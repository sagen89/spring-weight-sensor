package com.mycompany.spring_pc_scope.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "RECORD")
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // private long sequenceNumber;

    // private int period;
    @Column(name = "`value`")
    private int value;

    // @ManyToOne
    // @JoinColumn(name = "signal_id")
    // private SignalEntity signalEntity;

}
