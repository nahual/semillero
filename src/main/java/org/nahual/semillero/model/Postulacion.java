package org.nahual.semillero.model;


import javax.persistence.*;

@Entity
@Table(name = "POSTULACIONES")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EGRESADO_ID")
    private Egresado egresado;

    @ManyToOne
    @JoinColumn(name = "EMPLEADOR_ID")
    private Empleador empleador;

    @ManyToOne
    @JoinColumn(name = "BUSQUEDA_ID")
    private Busqueda busqueda;

    @Column(name = "ACTIVA")
    private Boolean activa;

    @Column(name = "DESCRIPCION")
    private String descripcion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empleador getEmpleador() {
        return empleador;
    }

    public void setEmpleador(Empleador empleador) {
        this.empleador = empleador;
    }

    public Busqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(Busqueda busqueda) {
        this.busqueda = busqueda;
    }

    public Egresado getEgresado() {
        return egresado;
    }

    public void setEgresado(Egresado egresado) {
        this.egresado = egresado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
    
}
