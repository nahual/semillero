package org.nahual.utils;


import org.nahual.semillero.model.Egresado;

import java.io.File;

public class CvUtils {

    private String absolutePath = new File("").getAbsolutePath();
    private String tmpFolder;
    private String cvFolder;

    public String getCvPath(Egresado egresado){
        if (egresado != null)
            return absolutePath + cvFolder + "/" + egresado.getId().toString() + "/" + egresado.getCv();

        return null;
    }

    public String getTmpPath(){
        String tmpPath = absolutePath + cvFolder + tmpFolder;

        return tmpPath;
    }

    public void setTmpFolder(String tmpFolder) {
        this.tmpFolder = tmpFolder;
    }

    public String getCvFolder() {
        return cvFolder;
    }

    public void setCvFolder(String cvFolder) {
        this.cvFolder = cvFolder;
    }
}
