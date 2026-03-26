package mbs.am.gui.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBLRISKCONFIG")
@Getter
@Setter
public class RiskConfigEntity {
    @Id
    @Column(name = "TENANT_ID", length = 50)
    private Long tenantId;

    @Column(name = "MONITOR_WINDOW_HOURS", nullable = false)
    private Integer monitorWindowHours;

    @Column(name = "IS_ENGINE_ENABLED", nullable = false)
    private Integer isEngineEnabled;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "UPDATED_AT", insertable = false)
    private LocalDateTime updatedAt;
}