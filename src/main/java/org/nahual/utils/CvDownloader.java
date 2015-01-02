package org.nahual.utils;


import com.vaadin.data.hbnutil.ContainerFilter;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Notification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.nahual.semillero.model.Egresado;

import java.io.FileInputStream;
import java.io.InputStream;

public class CvDownloader extends FileDownloader {

    private Egresado egresado;
    private CvUtils cvUtils;
    private String dummyFilename = "dl";

    public CvDownloader(Egresado egresado) {
        super(new StreamResource(null, null));
        this.setResource(dummyFilename, createResource());
        this.egresado = egresado;
        this.cvUtils = SpringHelper.getBean("cvInfo", CvUtils.class);
    }

    private StreamResource createResource() {
        StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                InputStream inputStream = null;
                final HbnContainer<Egresado> hbn = new HbnContainer<Egresado>(Egresado.class, SpringHelper.getSession());

                // Updating object
                hbn.addContainerFilter(new ContainerFilter("id") {
                    @Override
                    public Criterion getFieldCriterion(String fullPropertyName) {
                        return Restrictions.eq(fullPropertyName, egresado.getId());
                    }
                });

                for (Object itemId : hbn.getItemIds()) {
                    egresado = hbn.getItem(itemId).getPojo();
                    ((StreamResource)getResource(dummyFilename)).setFilename(egresado.getCv());
                }

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
        }, dummyFilename);

        return resource;
    }

}
