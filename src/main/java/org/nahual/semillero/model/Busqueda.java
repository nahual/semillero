package org.nahual.semillero.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by fdviosteam on 06/03/14.
 */
@Entity
@Table(name = "BUSQUEDA")
public class Busqueda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLEADOR_ID")
    private Empleador empleador;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "FECHA_INICIO")
    private Date fechaInicio;

    @Column(name = "FECHA_FIN")
    private Date fechaFin;

    @Column(name = "ACTIVA", columnDefinition = "SMALLINT DEFAULT 1")
    private boolean activa;

    @Column(name = "FICTICIA")
    private boolean ficticia = false;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    public Empleador getEmpleador() {
        return empleador;
    }

    public void setEmpleador(Empleador empleador) {
        this.empleador = empleador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFicticia() {
        return ficticia;
    }

    public void setFicticia(boolean ficticia) {
        this.ficticia = ficticia;
    }

    @Override
    public String toString() {
        if (!ficticia){
            return titulo + " - " + empleador;
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Busqueda busqueda = (Busqueda) o;

        if (id != null ? !id.equals(busqueda.id) : busqueda.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

