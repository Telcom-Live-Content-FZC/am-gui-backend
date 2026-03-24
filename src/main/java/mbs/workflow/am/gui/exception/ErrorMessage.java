package mbs.workflow.am.gui.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private Long code;
    private String language;
    private String message;
    private int httpStatus;
    private int status;

}
