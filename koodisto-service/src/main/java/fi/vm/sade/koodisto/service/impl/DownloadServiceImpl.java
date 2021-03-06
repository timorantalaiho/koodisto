package fi.vm.sade.koodisto.service.impl;

import fi.vm.sade.koodisto.service.DownloadService;
import fi.vm.sade.koodisto.service.GenericFault;
import fi.vm.sade.koodisto.service.business.DownloadBusinessService;
import fi.vm.sade.koodisto.service.types.common.ExportImportFormatType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.jws.WebParam;

/**
 * User: kwuoti
 * Date: 8.4.2013
 * Time: 11.30
 */
public class DownloadServiceImpl implements DownloadService {
    @Autowired
    private DownloadBusinessService downloadBusinessService;

    @Override
    public DataHandler download(@WebParam(name = "koodistoUri", targetNamespace = "") String koodistoUri,
                                @WebParam(name = "koodistoVersio", targetNamespace = "") int koodistoVersio,
                                @WebParam(name = "exportFormat", targetNamespace = "") ExportImportFormatType exportFormat,
                                @WebParam(name = "encoding", targetNamespace = "") String encoding) throws GenericFault {
        return downloadBusinessService.download(koodistoUri, koodistoVersio, exportFormat, encoding);
    }
}
