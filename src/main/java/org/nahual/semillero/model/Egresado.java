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
    private Long telefonoFijo;

    @Column(name = "TELEFONOMOVIL")
    private Long telefonoMovil;

    @Column(name = "EMAIL")
    private String correoElectronico;

    @Column(name = "CUATRIMESTRE")
    private String cuatrimestre;

    @Column(name = "NODO")
    private String nodo;

    @Column(name = "OBSERVACIONES")
    private String observaciones;

    // TODO: Agregar CV


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

    public Long getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(Long telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public Long getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(Long telefonoMovil) {
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
