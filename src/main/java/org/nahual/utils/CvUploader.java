package org.nahual.utils;

import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class CvUploader implements Upload.Receiver, Upload.SucceededListener {
    private String rootPath = new File("").getAbsolutePath();

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        // Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
            File tmpDir = new File(rootPath + "/tmp/");
            if (!tmpDir.exists()){
                tmpDir.mkdir();
            }

            File cv = new File(tmpDir.getPath() + "/" + filename);
            fos = new FileOutputStream(cv);

        } catch (final java.io.FileNotFoundException e) {
            new Notification("No se pudo acceder al directorio!",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Show the uploaded file in the image viewer
        // TODO: Avisar que la carga del archivo fue exitosa.
    }

};
