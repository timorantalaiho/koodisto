/**
 *
 */
package fi.vm.sade.koodisto.service.impl;

import fi.vm.sade.generic.service.conversion.SadeConversionService;
import fi.vm.sade.koodisto.service.GenericFault;
import fi.vm.sade.koodisto.service.KoodiAdminService;
import fi.vm.sade.koodisto.service.business.KoodiBusinessService;
import fi.vm.sade.koodisto.service.business.KoodistoBusinessService;
import fi.vm.sade.koodisto.service.types.CreateKoodiDataType;
import fi.vm.sade.koodisto.service.types.UpdateKoodiDataType;
import fi.vm.sade.koodisto.service.types.common.KoodiType;
import fi.vm.sade.koodisto.service.types.common.SuhteenTyyppiType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Arrays;
import java.util.List;

/**
 * Service interface implementation for KoodiAdminService
 *
 * @author Eetu Blomqvist
 *
 */
public class KoodiAdminServiceImpl implements KoodiAdminService {

    @Autowired
    private KoodiBusinessService koodiBusinessService;

    @Autowired
    private KoodistoBusinessService koodistoBusinessService;

    @Autowired
    private SadeConversionService conversionService;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_READ_UPDATE','ROLE_APP_KOODISTO_CRUD')")
    public KoodiType updateKoodi(UpdateKoodiDataType updateKoodiData) {
        return conversionService.convert(koodiBusinessService.updateKoodi(updateKoodiData), KoodiType.class);
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public KoodiBusinessService getKoodiService() {
        return koodiBusinessService;
    }

    public void setKoodiService(KoodiBusinessService koodiService) {
        this.koodiBusinessService = koodiService;
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_CRUD')")
    public void massCreate(String koodistoUri, List<UpdateKoodiDataType> koodiList) {
        koodiBusinessService.massCreate(koodistoUri, koodiList);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_CRUD')")
    public void deleteKoodiVersion(String koodiUri, int koodiVersio) {
        koodiBusinessService.delete(koodiUri, koodiVersio);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_READ_UPDATE','ROLE_APP_KOODISTO_CRUD')")
    public void addRelation(String ylaKoodi, String alaKoodi,
                            SuhteenTyyppiType suhteenTyyppi) throws GenericFault {
        fi.vm.sade.koodisto.model.SuhteenTyyppi st = fi.vm.sade.koodisto.model.SuhteenTyyppi.valueOf(suhteenTyyppi
                .name());
        koodiBusinessService.addRelation(ylaKoodi, Arrays.asList(alaKoodi), st, false);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_READ_UPDATE','ROLE_APP_KOODISTO_CRUD')")
    public void addRelationByAlakoodi(String ylaKoodi, List<String> alaKoodis,
                                      SuhteenTyyppiType suhteenTyyppi) throws GenericFault {
        fi.vm.sade.koodisto.model.SuhteenTyyppi st = fi.vm.sade.koodisto.model.SuhteenTyyppi.valueOf(suhteenTyyppi
                .name());
        koodiBusinessService.addRelation(ylaKoodi, alaKoodis, st, false);

    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_READ_UPDATE','ROLE_APP_KOODISTO_CRUD')")
    public void removeRelationByAlakoodi(String ylaKoodi, List<String> alaKoodis,
                                         SuhteenTyyppiType suhteenTyyppi) throws GenericFault {
        fi.vm.sade.koodisto.model.SuhteenTyyppi st = fi.vm.sade.koodisto.model.SuhteenTyyppi.valueOf(suhteenTyyppi
                .name());
        koodiBusinessService.removeRelation(ylaKoodi, alaKoodis, st, false);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_CRUD')")
    public KoodiType createKoodi(String koodistoUri, CreateKoodiDataType createKoodiData) {
        return conversionService.convert(koodiBusinessService.createKoodi(koodistoUri, createKoodiData),
                KoodiType.class);
    }

}
