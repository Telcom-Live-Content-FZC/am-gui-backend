package mbs.am.gui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBLAUDITLOG")
public class            AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TENANT_ID", length = 50)
    private String tenantId;

    @Column(name = "TABLE_NAME", nullable = false, length = 50)
    private String tableName;

    @Column(name = "RECORD_ID", nullable = false, length = 100)
    private String recordId;

    @Column(name = "ACTION", length = 20)
    private String action;

    @Column(name = "FIELD_CHANGED", length = 50)
    private String fieldChanged;

    @Column(name = "OLD_VALUE", length = 1000)
    private String oldValue;

    @Column(name = "NEW_VALUE", length = 1000)
    private String newValue;

    @Column(name = "CHANGED_BY", length = 100)
    private String changedBy;

    @Column(name = "CHANGED_AT", nullable = false)
    private LocalDateTime changedAt;
}