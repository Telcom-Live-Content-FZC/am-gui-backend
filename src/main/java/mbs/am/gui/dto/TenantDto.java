package mbs.am.gui.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDto {
    private Long id;
    private String name;
    private String issuer;
    private String status;
    private List<TenantKeyRegistryDto> keys;
    private List<TenantConfigDto> configs;
}
