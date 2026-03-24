package mbs.workflow.am.gui.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "TBLTENANTCONFIG", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"TENANT_ID", "KEY"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private TenantEntity tenant;

    @Column(name = "KEY", nullable = false, length = 100)
    private String key;

    @Lob
    @Column(name = "VALUE")
    private String value;
}
