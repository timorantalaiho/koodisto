/**
 *
 */
package fi.vm.sade.koodisto.service.business;

import fi.vm.sade.koodisto.model.KoodistoRyhma;
import fi.vm.sade.koodisto.model.KoodistoVersio;
import fi.vm.sade.koodisto.service.types.CreateKoodistoDataType;
import fi.vm.sade.koodisto.service.types.SearchKoodistosCriteriaType;
import fi.vm.sade.koodisto.service.types.UpdateKoodistoDataType;

import java.util.List;

/**
 * @author tommiha
 */
public interface KoodistoBusinessService {

    /**
     * Lists all koodisto joukko objects by kieli.
     * 
     * @param kieli
     * @return
     */
    List<KoodistoRyhma> listAllKoodistoRyhmas();

    /**
     * Deletes KoodistoVersio with given id and versio permanently
     * 
     * @param koodistoId
     * @param koodistoVersio
     */
    void delete(String koodistoUri, Integer koodistoVersio);

    KoodistoVersio createNewVersion(String koodistoUri);

    List<KoodistoVersio> searchKoodistos(SearchKoodistosCriteriaType searchCriteria);

    KoodistoVersio getLatestKoodistoVersio(String koodistoUri);

    KoodistoVersio getKoodistoVersio(String koodistoUri, Integer koodistoVersio);

    KoodistoVersio createKoodisto(List<String> koodistoRyhmaUris, CreateKoodistoDataType createKoodistoData);

    KoodistoVersio updateKoodisto(UpdateKoodistoDataType updateKoodistoData);

    KoodistoVersio createNewVersion(String koodistoUri, String koodiUri, boolean preserveOldRelations);

    boolean koodistoExists(String koodistoUri);

    boolean koodistoExists(String koodistoUri, Integer koodistoVersio);
}
