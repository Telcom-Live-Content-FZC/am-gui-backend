package mbs.workflow.am.gui.exception;

import mbs.workflow.am.gui.repository.BaseRepository;

import javax.ejb.Stateless;

@Stateless
public class ErrorMessageRepository extends BaseRepository<ErrorMessageEntity,Long> {

    public ErrorMessageRepository() {
        super(ErrorMessageEntity.class);
    }


}
