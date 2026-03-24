package mbs.workflow.am.gui.dto;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantKeyRegistryDto {

    private Long id;
    private String kid;
    private String algorithm;
    @SerializedName("public-key")
    private String publicKey;
    private String status;

}
