package com.luanpaiva.observador_de_precos.modules.alerts.entity;

import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.alerts.enums.AlertType;
import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;
import com.luanpaiva.observador_de_precos.shared.auditable.AuditableEntity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Alert extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_id")
    private Monitoring monitoring;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private Boolean read;
}
