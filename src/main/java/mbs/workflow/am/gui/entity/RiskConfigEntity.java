package mbs.workflow.am.gui.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBLRISKCONFIG")
@Getter
@Setter
public class RiskConfigEntity {
    @Id
    @Column(name = "TENANT_ID", length = 50)
    private String tenantId;

    @Column(name = "MONITOR_WINDOW_HOURS", nullable = false)
    private Integer monitorWindowHours;

    @Column(name = "IS_ENGINE_ENABLED", nullable = false)
    private Integer isEngineEnabled;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "UPDATED_AT", insertable = false)
    private LocalDateTime updatedAt;
}