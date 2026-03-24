package mbs.workflow.am.gui.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBLMESSAGES_")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageEntity {

    @Id
    private long code;
    private String language;
    private String message;

    @Column(name = "HTTP_STATUS")
    private Integer httpStatus;

    private Integer status;
}
