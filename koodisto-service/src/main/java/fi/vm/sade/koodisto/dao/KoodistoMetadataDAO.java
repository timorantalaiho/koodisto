/**
 *
 */
package fi.vm.sade.koodisto.dao;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.koodisto.model.KoodistoMetadata;

import java.util.List;

/**
 * @author tommiha
 */
public interface KoodistoMetadataDAO extends JpaDAO<KoodistoMetadata, Integer> {
    List<KoodistoMetadata> listAllByKoodisto(String koodistoUri);

    boolean nimiExistsForSomeOtherKoodisto(String koodistoUri, String nimi);

    boolean nimiExists(String nimi);
}
