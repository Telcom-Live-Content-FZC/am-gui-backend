package mbs.workflow.am.gui.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardSummary {

    @SerializedName("total-evaluations")
    private long totalEvaluations;
    @SerializedName("allowed-count")
    private long allowedCount;
    @SerializedName("blocked-count")
    private long blockedCount;
    @SerializedName("quarantine-count")
    private long quarantinedCount;
    @SerializedName("block-rate-percentage")
    private double blockRatePercentage;

    @SerializedName("top-threats")
    private List<TopThreat> topThreats;
}
