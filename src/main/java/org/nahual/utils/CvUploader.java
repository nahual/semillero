package org.nahual.utils;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import org.nahual.semillero.model.Egresado;

import java.io.*;

/* TODO: Pasar la lógica de directorios por completo a la clase CvUtils o crear una similar exclusiva para eso */
/* TODO: Verificar funcionamiento en diferentes sistemas operativos. Por la sintaxis de los paths (escapes, etc) */
public class CvUploader implements Upload.Receiver, Upload.SucceededListener {
    private String cvRootPath;
    private String tmpPath;
    private File cvTmp;
    private File cv;
    private String filename;
    private CvUtils cvUtils;

    public CvUploader(){
        cvUtils = SpringHelper.getBean("cvInfo", CvUtils.class);
        cvRootPath = cvUtils.getCvRootPath();
        tmpPath = cvUtils.getTmpPath();
    }

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        this.filename = filename;

        // upload stream
        FileOutputStream fos = null;
        try {
            // Verificar si existe cvRootPath
            File cvsPath = new File(cvRootPath);
            if (!cvsPath.exists()) {
                cvsPath.mkdir();
            }
            // Verificar si existe la carpeta temporal
            File tmpDir = new File(tmpPath);
            if (!tmpDir.exists()) {
                tmpDir.mkdir();
            }
            // Borrar archivos basura en carpeta temporal
            if (cvTmp != null && cvTmp.exists()) {
                cvTmp.delete();
            }

            cvTmp = new File(cvUtils.getTmpPath(filename));
            fos = new FileOutputStream(cvTmp);

        } catch (final java.io.FileNotFoundException e) {
            new Notification("No se pudo acceder al directorio!",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos;
    }

    /* Se reubica el archivo de la carpeta temporal hacia la ubicación definitiva */
    public void relocateFileFor(Egresado egresado) {
        try {
            // Verifica que exista el directorio del egresado
            File egresadoDir = new File(cvUtils.getEgresadoFolder(egresado));
            if (!egresadoDir.exists()) {
                egresadoDir.mkdir();
            } else {
                // Si existía un cv anterior, se reemplaza por el nuevo
                cv = new File(cvUtils.getCvPath(egresado));
                if (cv.exists())
                    cv.delete();
            }

            if (filename != null) {
                cvTmp = new File(cvUtils.getTmpPath(filename));
                cv = new File(cvUtils.getCvPath(egresado, filename));
                copyFile(cvTmp, cv);
                egresado.setCv(cv.getName());
            }

        } catch (final java.io.FileNotFoundException e) {
            new Notification("No se pudo acceder al directorio!",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
        } catch (Exception e) {
            new Notification("Error en la lectura de archivos!",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
        }
    }

    /* Función auxiliar para copiar un archivo de un lugar a otro: sourceFile -> destFile*/
    private void copyFile(File sourceFile, File destFile)
            throws IOException {
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new FileInputStream(sourceFile);
            outStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            inStream.close();
            outStream.close();

            //delete the original file
            sourceFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        new Notification("El archivo se cargó correctamente.",
                Notification.Type.HUMANIZED_MESSAGE)
                .show(Page.getCurrent());
    }

};
