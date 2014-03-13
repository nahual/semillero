package org.nahual.semillero.model;

import javax.persistence.*;
import java.util.Set;

/**
 * User: fdv
 * Date: 1/24/14
 * Time: 9:46 AM
 */

@Entity
@Table(name = "EMPLEADOR")
public class Empleador {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "EMPRESA")
    private String empresa;

    @Column(name = "CONTACTO")
    private String contacto;

    @OneToMany
    @JoinTable(
            name="OBSERVACIONES_EMPLEADOR",
            joinColumns = @JoinColumn( name="EMPLEADOR_ID"),
            inverseJoinColumns = @JoinColumn( name="OBSERVACION_ID")
    )
    private Set<Observacion> observaciones;

    @Column(name = "ACTIVO")
    private Boolean activo;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Observacion> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(Set<Observacion> observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Empleador empleador = (Empleador) o;

        if (!id.equals(empleador.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
