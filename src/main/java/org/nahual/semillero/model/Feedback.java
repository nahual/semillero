package org.nahual.semillero.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jose Sanchez on 3/27/14.
 */
@Entity
@Table(name = "FEEDBACK")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FECHA")
    private Date fecha;

    @Column(name = "TEXTO")
    private String texto;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
