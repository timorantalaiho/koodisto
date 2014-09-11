package fi.vm.sade.koodisto.service.koodisto.rest.validator;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import fi.vm.sade.koodisto.model.Tila;
import fi.vm.sade.koodisto.model.constraint.exception.KoodistoValidatorRuntimeException;
import fi.vm.sade.koodisto.service.business.exception.KoodistoNimiEmptyException;
import fi.vm.sade.koodisto.service.business.exception.MetadataEmptyException;
import static org.junit.Assert.assertEquals;

public class ValidatorUtilTest {

    private final static KoodistoValidationException ERROR = new KoodistoValidationException("error");

    @Test(expected = KoodistoValidationException.class)
    public void throwsExceptionWhenGivenStringIsEmpty() {
        ValidatorUtil.checkForBlank("", ERROR);
    }

    @Test
    public void throwsExceptionWithGivenMessageWhenGivenStringIsNull() {
        try {
            ValidatorUtil.checkForBlank(null, ERROR);
        } catch (KoodistoValidationException e) {
            assertEquals(ERROR.getMessage(), e.getMessage());
        }
    }

    @Test
    public void throwsExceptionWithGivenMessageWhenGivenStringIsEmpty() {
        try {
            ValidatorUtil.checkForBlank("", ERROR);
        } catch (KoodistoValidationException e) {
            assertEquals(ERROR.getMessage(), e.getMessage());
        }
    }

    @Test
    public void throwsExceptionWithGivenMessageWhenGivenStringHasOnlySpaces() {
        try {
            ValidatorUtil.checkForBlank("    ", ERROR);
        } catch (KoodistoValidationException e) {
            assertEquals(ERROR.getMessage(), e.getMessage());
        }
    }

    @Test
    public void throwsExceptionWithGivenMessageWhenObjectThatIsCheckedIsNull() {
        try {
            ValidatorUtil.checkForNull((Tila) null, ERROR);
        } catch (KoodistoValidationException e) {
            assertEquals(ERROR.getMessage(), e.getMessage());
        }
    }

    @Test
    public void throwsExceptionWithGivenMessageWhenStartDateIsAfterEndDate() {
        try {
            ValidatorUtil.checkBeginDateBeforeEndDate(new Date(), new Date(0L), ERROR);
        } catch (KoodistoValidationException e) {
            assertEquals(ERROR.getMessage(), e.getMessage());
        }
    }

    @Test(expected = KoodistoNimiEmptyException.class)
    public void throwsGivenExceptionWhenCheckingForBlank() {
        ValidatorUtil.checkForBlank("  ", new KoodistoNimiEmptyException(ERROR));
    }

    @Test(expected = KoodistoValidatorRuntimeException.class)
    public void throwsGivenExceptionWhenCheckingForNull() {
        ValidatorUtil.checkForNull(null, new KoodistoValidatorRuntimeException(ERROR));
    }

    @Test(expected = MetadataEmptyException.class)
    public void throwsExceptionWithNullCollection() {
        ValidatorUtil.checkCollectionIsNotNullOrEmpty(null, new MetadataEmptyException(ERROR));
    }

    @Test(expected = MetadataEmptyException.class)
    public void throwsExceptionWithEmptyCollection() {
        ValidatorUtil.checkCollectionIsNotNullOrEmpty(new ArrayList<String>(), new MetadataEmptyException(ERROR));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWithNullInteger() {
        ValidatorUtil.checkForGreaterThan((Integer) null, 1, new IllegalArgumentException());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWithIntegerThatIsNotGreaterThanTheValueGiven() {
        ValidatorUtil.checkForGreaterThan(1, 1, new IllegalArgumentException());
    }
}