package org.nahual.semillero.model;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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

    @Column(name = "EXITOSA")
    private Boolean exitosa = false;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "FECHA")
    private Date fecha;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable
            (name = "FEEDBACK_POSTULACION", joinColumns = {@JoinColumn(name = "POSTULACION_ID", referencedColumnName = "id")}
                    , inverseJoinColumns = {@JoinColumn(name = "FEEDBACK_ID", referencedColumnName = "id")})
    private Set<Feedback> feedbacks;


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

    public Boolean getExitosa() {
        return exitosa;
    }

    public void setExitosa(Boolean exitosa) {
        this.exitosa = exitosa;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
