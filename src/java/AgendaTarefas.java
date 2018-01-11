

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
public class AgendaTarefas implements Serializable {

    public AgendaTarefas() {

    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "Titulo", nullable = true)
    private String Titulo;

    @Column(name = "Descricao", nullable = true)
    private String Descricao;

    @Column(name = "dateInicio", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateInicio;

    @Column(name = "DateTermino", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date DateTermino;

    @Column(name = "Status", nullable = true)
    private boolean Status;

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    public Date getDateInicio() {
        return dateInicio;
    }

    public void setDateInicio(Date dateInicio) {
        this.dateInicio = dateInicio;
    }

    public Date getDateTermino() {
        return DateTermino;
    }

    public void setDateTermino(Date DateTermino) {
        this.DateTermino = DateTermino;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgendaTarefas)) {
            return false;
        }
        AgendaTarefas other = (AgendaTarefas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AgendaTarefas{" + "id=" + id + ", Titulo=" + Titulo + ", Descricao=" + Descricao + ", dateInicio=" + dateInicio + ", DateTermino=" + DateTermino + '}';
    }

}
