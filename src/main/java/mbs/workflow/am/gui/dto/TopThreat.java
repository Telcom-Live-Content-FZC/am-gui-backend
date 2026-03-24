package mbs.workflow.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopThreat {

    private String key;
    private String description;
    @SerializedName("trigger-count")
    private long triggerCount;

}
