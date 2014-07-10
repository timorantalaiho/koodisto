package fi.vm.sade.koodisto.service.koodisto.rest;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.annotate.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.generic.service.conversion.SadeConversionService;
import fi.vm.sade.koodisto.dto.KoodistoRyhmaDto;
import fi.vm.sade.koodisto.model.JsonViews;
import fi.vm.sade.koodisto.model.KoodistoRyhma;
import fi.vm.sade.koodisto.service.business.KoodistoRyhmaBusinessService;
import fi.vm.sade.koodisto.service.business.UriTransliterator;

@Component
@Path("/codesgroup")
@PreAuthorize("isAuthenticated()")
@Api(value = "/rest/codesgroup", description = "Koodistoryhmät")
public class CodesGroupResource {
    protected final static Logger logger = LoggerFactory.getLogger(CodesGroupResource.class);

    @Autowired
    private KoodistoRyhmaBusinessService koodistoRyhmaBusinessService;

    @Autowired
    private SadeConversionService conversionService;

    @Autowired
    private UriTransliterator uriTransliterator;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView({ JsonViews.Basic.class })
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_READ','ROLE_APP_KOODISTO_READ_UPDATE','ROLE_APP_KOODISTO_CRUD')")
    @ApiOperation(
            value = "Palauttaa koodistoryhmän",
            notes = "",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Id on virheellinen"),
            @ApiResponse(code = 500, message = "Koodistoryhmää ei löydy kyseisellä id:llä")
    })
    public Response getCodesByCodesUri(
            @ApiParam(value = "Koodistoryhmän id") @PathParam("id") Long id) {
        if (id == null || id < 1) {
            logger.warn("Invalid parameter for rest call. id: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            KoodistoRyhma koodistoRyhma = koodistoRyhmaBusinessService.getKoodistoRyhmaById(id);
            return Response.status(Response.Status.OK).entity(conversionService.convert(koodistoRyhma, KoodistoRyhmaDto.class)).build();
        } catch (Exception e) {
            logger.warn("Error finding CodesGroup. id: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView({ JsonViews.Basic.class })
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_READ_UPDATE','ROLE_APP_KOODISTO_CRUD')")
    @ApiOperation(
            value = "Päivittää koodistoryhmää",
            notes = "",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 400, message = "Parametri on tyhjä"),
            @ApiResponse(code = 500, message = "Koodistoryhmää ei saatu päivitettyä")
    })
    public Response update(
            @ApiParam(value = "Koodistoryhmä") KoodistoRyhmaDto codesGroupDTO) {
        if (codesGroupDTO == null) {
            logger.warn("Invalid parameter for rest call. codesGroupDTO: " + codesGroupDTO);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            KoodistoRyhma koodistoRyhma = koodistoRyhmaBusinessService.updateKoodistoRyhma(codesGroupDTO);
            return Response.status(Response.Status.CREATED).entity(conversionService.convert(koodistoRyhma, KoodistoRyhmaDto.class)).build();
        } catch (Exception e) {
            logger.warn("Error while updating codesGroup. ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView({ JsonViews.Basic.class })
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_CRUD')")
    @ApiOperation(
            value = "Luo uuden koodistoryhmän",
            notes = "",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 400, message = "Parametri on tyhjä"),
            @ApiResponse(code = 500, message = "Koodistoryhmää ei saatu lisättyä")
    })
    public Response insert(
            @ApiParam(value = "Koodistoryhmä") KoodistoRyhmaDto codesGroupDTO) {
        if (codesGroupDTO == null) {
            logger.warn("Invalid parameter for rest call. codesGroupDTO: " + codesGroupDTO);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            codesGroupDTO.setKoodistoRyhmaUri(uriTransliterator.generateKoodistoGroupUriByMetadata((Collection) codesGroupDTO.getKoodistoRyhmaMetadatas()));
            KoodistoRyhma koodistoRyhma = koodistoRyhmaBusinessService.createKoodistoRyhma(codesGroupDTO);
            return Response.status(Response.Status.CREATED).entity(conversionService.convert(koodistoRyhma, KoodistoRyhmaDto.class)).build();
        } catch (Exception e) {
            logger.warn("Error while inserting codesGroup.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView({ JsonViews.Simple.class })
    @PreAuthorize("hasAnyRole('ROLE_APP_KOODISTO_CRUD')")
    @ApiOperation(
            value = "Poistaa koodistoryhmän",
            notes = "",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "OK"),
            @ApiResponse(code = 400, message = "Id on virheellinen."),
            @ApiResponse(code = 500, message = "Koodiryhmää ei saatu poistettua")
    })
    public Response delete(
            @ApiParam(value = "Koodistoryhmän URI") @PathParam("id") Long id) {
        if (id == null || id < 1) {
            logger.warn("Invalid parameter for rest call. id: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            koodistoRyhmaBusinessService.delete(id);
            return Response.status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            logger.warn("Error while removing codesGroup. id: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
