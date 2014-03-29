package org.nahual.utils;


import org.nahual.semillero.model.Egresado;

import java.io.File;

public class CvUtils {

    private String absolutePath = new File("").getAbsolutePath();

    /* Estos datos son cargados desde el archivo config.local.properties */
    private String tmpFolder;
    private String cvFolder;


    /* Ubicación del directorio donde se guardan todos los cvs */
    public String getCvRootPath() {
        return absolutePath + cvFolder;
    }

    /* Ubicación del directorio temporal */
    public String getTmpPath() {
        String tmpPath = getCvRootPath() + tmpFolder;

        return tmpPath;
    }

    /* Ubicación del cv del egresado */
    public String getCvPath(Egresado egresado) {
        if (egresado != null)
            return getCvRootPath() + "/" + egresado.getId().toString() + "/" + egresado.getCv();

        throw null;
    }

    /* Ubicación de la carpeta donde se guarda el cv del egresado */
    public String getEgresadoFolder(Egresado egresado) {
        if (egresado != null)
            return getCvRootPath() + "/" + egresado.getId().toString();

        return null;
    }

    public String getCvPath(Egresado egresado, String newFilename) {
        return getEgresadoFolder(egresado) + "/" + newFilename;
    }

    public String getTmpPath(String filename) {
        String tmpPath = getTmpPath() + "/" + filename;

        return tmpPath;
    }

    public void setTmpFolder(String tmpFolder) {
        this.tmpFolder = tmpFolder;
    }

    public void setCvFolder(String cvFolder) {
        this.cvFolder = cvFolder;
    }
}
