/**
 *
 */
package fi.vm.sade.koodisto.model;

import fi.vm.sade.generic.model.BaseEntity;
import fi.vm.sade.koodisto.common.util.FieldLengths;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author
 */
@Entity
@Table(name = KoodistonSuhde.TABLE_NAME, uniqueConstraints = @UniqueConstraint(name = "UK_" + KoodistonSuhde.TABLE_NAME + "_01", columnNames = {
        KoodistonSuhde.ALAKOODISTO_COLUMN_NAME, KoodistonSuhde.YLAKOODISTO_COLUMN_NAME, KoodistonSuhde.SUHTEEN_TYYPPI_COLUMN_NAME }))
@org.hibernate.annotations.Table(appliesTo = KoodistonSuhde.TABLE_NAME, comment = "Määrittää kahden koodiston välisen suhteen. Suhteen tyyppi voi olla SISALTYY tai RINNASTEINEN.")
@Cacheable
public class KoodistonSuhde extends BaseEntity {

    private static final long serialVersionUID = -8875747407128912635L;

    public static final String TABLE_NAME = "koodistonSuhde";

    public static final String YLAKOODISTO_COLUMN_NAME = "ylakoodistoVersio_id";
    public static final String ALAKOODISTO_COLUMN_NAME = "alakoodistoVersio_id";
    public static final String SUHTEEN_TYYPPI_COLUMN_NAME = "suhteenTyyppi";

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = YLAKOODISTO_COLUMN_NAME, nullable = false)
    private KoodistoVersio ylakoodistoVersio;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ALAKOODISTO_COLUMN_NAME, nullable = false)
    private KoodistoVersio alakoodistoVersio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = SUHTEEN_TYYPPI_COLUMN_NAME, nullable = false, length = FieldLengths.DEFAULT_FIELD_LENGTH)
    private SuhteenTyyppi suhteenTyyppi;

    public KoodistoVersio getYlakoodistoVersio() {
        return ylakoodistoVersio;
    }

    public void setYlakoodistoVersio(KoodistoVersio ylakoodistoVersio) {
        this.ylakoodistoVersio = ylakoodistoVersio;
    }

    public KoodistoVersio getAlakoodistoVersio() {
        return alakoodistoVersio;
    }

    public void setAlakoodistoVersio(KoodistoVersio alakoodistoVersio) {
        this.alakoodistoVersio = alakoodistoVersio;
    }

    public SuhteenTyyppi getSuhteenTyyppi() {
        return suhteenTyyppi;
    }

    public void setSuhteenTyyppi(SuhteenTyyppi suhteenTyyppi) {
        this.suhteenTyyppi = suhteenTyyppi;
    }

}