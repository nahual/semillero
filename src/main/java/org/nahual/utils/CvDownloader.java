package org.nahual.utils;


import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Notification;
import org.nahual.semillero.model.Egresado;

import java.io.FileInputStream;
import java.io.InputStream;

public class CvDownloader extends FileDownloader {

    public CvDownloader(Egresado egresado) {
        super(CvDownloader.createResource(egresado));
    }

    private static StreamResource createResource(final Egresado egresado) {
        return new StreamResource(new StreamResource.StreamSource() {
            InputStream inputStream = null;
            CvUtils cvUtils = SpringHelper.getBean("cvInfo", CvUtils.class);

            @Override
            public InputStream getStream() {
                try {
                    inputStream = new FileInputStream(cvUtils.getCvPath(egresado));
                } catch (Exception e) {
                    new Notification("El egresado no tiene un CV asociado.",
                            Notification.Type.WARNING_MESSAGE)
                            .show(Page.getCurrent());
                    e.printStackTrace();
                }

                return inputStream;
            }
        }, egresado.getCv());
    }
}
