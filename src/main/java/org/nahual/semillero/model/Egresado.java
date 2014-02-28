package org.nahual.semillero.model;

import javax.persistence.*;


@Entity
@Table(name = "EGRESADO")
public class Egresado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "TELEFONOFIJO")
    private String telefonoFijo;

    @Column(name = "TELEFONOMOVIL")
    private String telefonoMovil;

    @Column(name = "EMAIL")
    private String correoElectronico;

    @Column(name = "CUATRIMESTRE")
    private String cuatrimestre;

    @Column(name = "NODO")
    private String nodo;

    @Column(name = "OBSERVACIONES")
    private String observaciones;

    @Column(name = "PATH_CV")
    private String path_cv;


    public String getPath_cv() {
        return path_cv;
    }

    public void setPath_cv(String path_cv) {
        this.path_cv = path_cv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(String cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
