package mbs.am.gui.exception;

import mbs.am.gui.repository.BaseRepository;

import javax.ejb.Stateless;

@Stateless
public class ErrorMessageRepository extends BaseRepository<ErrorMessageEntity,Long> {

    public ErrorMessageRepository() {
        super(ErrorMessageEntity.class);
    }


}
