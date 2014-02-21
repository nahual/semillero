package org.nahual.semillero.model;


import javax.persistence.*;

@Entity
@Table(name = "CUATRIMESTRES")
public class Cuatrimestre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CUATRIMESTRE")
    private String cuatrimestre;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(String cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

}
