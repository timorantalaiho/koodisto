package fi.vm.sade.koodisto.dto;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonView;

import fi.vm.sade.koodisto.model.JsonViews;
import fi.vm.sade.koodisto.model.SuhteenTyyppi;
import fi.vm.sade.koodisto.model.Tila;

public class KoodiChangesDto {
    
    @JsonView(JsonViews.Basic.class)
    public final String muutosTila;
    
    @JsonView(JsonViews.Basic.class)
    public final Integer viimeisinVersio;
    
    @JsonView(JsonViews.Basic.class)
    public final List<SimpleKoodiMetadataDto> muuttuneetTiedot;
    
    @JsonView(JsonViews.Basic.class)
    public final List<SimpleCodeElementRelation> lisatytKoodinSuhteet;
    
    @JsonView(JsonViews.Basic.class)
    public final List<SimpleCodeElementRelation> poistetutKoodinSuhteet;
    
    @JsonView(JsonViews.Basic.class)
    public final Date viimeksiPaivitetty;
    
    @JsonView(JsonViews.Basic.class)
    public final Date voimassaAlkuPvm;
    
    @JsonView(JsonViews.Basic.class)
    public final Date voimassaLoppuPvm;
    
    @JsonView(JsonViews.Basic.class)
    public final Tila tila;
    
    public KoodiChangesDto(String muutosTila, Integer viimeisinVersio, List<SimpleKoodiMetadataDto> muuttuneetTiedot,
            List<SimpleCodeElementRelation> lisatytKoodinSuhteet, List<SimpleCodeElementRelation> poistetutKoodinSuhteet, Date viimeksiPaivitetty,
            Date voimassaAlkuPvm, Date voimassaLoppuPvm, Tila tila) {
        this.muutosTila = muutosTila;
        this.viimeisinVersio = viimeisinVersio;
        this.muuttuneetTiedot = muuttuneetTiedot;
        this.lisatytKoodinSuhteet = lisatytKoodinSuhteet;
        this.poistetutKoodinSuhteet = poistetutKoodinSuhteet;
        this.viimeksiPaivitetty = viimeksiPaivitetty;
        this.voimassaAlkuPvm = voimassaAlkuPvm;
        this.voimassaLoppuPvm = voimassaLoppuPvm;
        this.tila = tila;
    }

    public static class SimpleCodeElementRelation {
        
        @JsonView(JsonViews.Basic.class)
        public final String koodiUri;

        @JsonView(JsonViews.Basic.class)
        public final Integer versio;

        @JsonView(JsonViews.Basic.class)
        public final SuhteenTyyppi suhteenTyyppi;

        @JsonView(JsonViews.Basic.class)
        public final boolean lapsiKoodi;
        
        public SimpleCodeElementRelation(String koodiUri, Integer versio, SuhteenTyyppi suhteenTyyppi, boolean lapsiKoodi) {
            this.koodiUri = koodiUri;
            this.versio = versio;
            this.suhteenTyyppi = suhteenTyyppi;
            this.lapsiKoodi = lapsiKoodi;
        }
        
    }
    
    
}
