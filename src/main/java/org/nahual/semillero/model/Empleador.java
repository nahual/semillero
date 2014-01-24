package org.nahual.semillero.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: fdv
 * Date: 1/24/14
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name="EMPLEADOR")
public class Empleador {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name="EMPRESA")
    private String empresa;

    @Column(name="CONTACTO")
    private String contacto;

    @Column(name="OBSERVACIONES")
    private String observaciones;

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
