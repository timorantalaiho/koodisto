package fi.vm.sade.koodisto.service.business.exception;

import fi.vm.sade.generic.service.exception.SadeBusinessException;

public class KoodiOptimisticLockingException extends SadeBusinessException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_KEY = KoodiOptimisticLockingException.class.getCanonicalName();

    public KoodiOptimisticLockingException() {
        super();
    }

    public KoodiOptimisticLockingException(String message, Throwable cause) {
        super(message, cause);
    }

    public KoodiOptimisticLockingException(String message) {
        super(message);
    }

    public KoodiOptimisticLockingException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

}
