package mbs.am.gui.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBLRISKTHRESHOLD")
@Getter
@Setter
public class RiskThresholdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TENANT_ID", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "MIN_SCORE", nullable = false)
    private Integer minScore;

    @Column(name = "MAX_SCORE", nullable = false)
    private Integer maxScore;

    @Column(name = "ACTION", nullable = false, length = 50)
    private String action;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}