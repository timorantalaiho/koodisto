package fi.vm.sade.koodisto.service.koodisto.rest.validator;

import fi.vm.sade.koodisto.dto.KoodiRelaatioListaDto;
import fi.vm.sade.koodisto.service.koodisto.rest.validator.Validatable.ValidationType;

public class CodeElementRelationListValidator implements RestValidator<KoodiRelaatioListaDto> {

    private static final KoodistoValidationException TO_THROW = new KoodistoValidationException("error.validation.codeelementrelationlist");

    @Override
    public void validate(KoodiRelaatioListaDto validatable, ValidationType type) {
        if (type == ValidationType.INSERT) {
            validateInsert(validatable);
        } else {
            validateUpdate(validatable);
        }
    }

    @Override
    public void validateInsert(KoodiRelaatioListaDto validatable) {
        ValidatorUtil.checkForNull(validatable, TO_THROW);
        ValidatorUtil.checkForBlank(validatable.getCodeElementUri(), TO_THROW);
        ValidatorUtil.checkCollectionIsNotNullOrEmpty(validatable.getRelations(), TO_THROW);
        ValidatorUtil.checkForBlank(validatable.getRelationType(), TO_THROW);
    }

    @Override
    public void validateUpdate(KoodiRelaatioListaDto validatable) {
        ValidatorUtil.checkForNull(validatable, TO_THROW);
        ValidatorUtil.checkForBlank(validatable.getCodeElementUri(), TO_THROW);
        ValidatorUtil.checkCollectionIsNotNullOrEmpty(validatable.getRelations(), TO_THROW);
        ValidatorUtil.checkForBlank(validatable.getRelationType(), TO_THROW);
    }
}
