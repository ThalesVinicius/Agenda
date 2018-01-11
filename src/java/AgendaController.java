

import br.edu.ifnmg.estagio.entidades.AgendaTarefas;
import br.edu.ifnmg.estagio.repositorios.AgendaTarefasRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author thales
 */
@Named(value = "agendaController")
@ViewScoped
public class AgendaController implements Serializable {

    @EJB
    private AgendaTarefasRepositorio repositorio;

    private AgendaTarefas agenda;
    private ScheduleModel eventModel;
    private List<AgendaTarefas> listaEventos;

    //  private ScheduleEvent event = new DefaultScheduleEvent();
    /////////////////////////////////////////////
    @PostConstruct
    public void inicializar() {
        agenda = new AgendaTarefas();
        eventModel = new DefaultScheduleModel();

        try {
            listaEventos = repositorio.Buscar();
        } catch (Exception ex) {

        }

        for (AgendaTarefas at : listaEventos) {
            DefaultScheduleEvent dsm = new DefaultScheduleEvent();
            dsm.setEndDate(at.getDateTermino());
            dsm.setStartDate(at.getDateInicio());
            dsm.setTitle(at.getTitulo());
            dsm.setData(at.getId());
            dsm.setAllDay(true);
            dsm.setEditable(true);
            dsm.setStyleClass(at.getDescricao());

            eventModel.addEvent(dsm);
            if (at.isStatus() == true) {
                dsm.setStyleClass("style1");
            } else if (at.isStatus() == false) {
                dsm.setStyleClass("style2");
            }
        }
    }

    public void OnSelect(SelectEvent selectEvent) {
        ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();

        for (AgendaTarefas at : listaEventos) {
            if (at.getId() == (long) event.getData()) {
                agenda = at;
                break;
            }

        }
    }

    public void newEvent(SelectEvent selectEvent) {
        ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        agenda = new AgendaTarefas();
        agenda.setDateInicio(new java.sql.Date(event.getStartDate().getTime()));
        agenda.setDateTermino(new java.sql.Date(event.getEndDate().getTime()));

    }

    public void salvar() {
        if (agenda.getId() == null) {
            if (agenda.getDateInicio().getTime() <= agenda.getDateTermino().getTime()) {

                try {
                    repositorio.Salvar(agenda);
                    inicializar();
                    MensagemSucesso("Sucesso !!!", "Compromisso salvo com sucesso");
                } catch (Exception ex) {
                    MensagemErro("Erro!", "Consulte o administrador do sistema!");
                }
                agenda = new AgendaTarefas();
            } else {
                inicializar();
                MensagemErro("Erro!", "Altere a data. Data de TÉRMINO anterior á Data de INICIO");
            }
        } else {
            if (agenda.getDateInicio().getTime() <= agenda.getDateTermino().getTime()) {

                try {
                    repositorio.Salvar(agenda);
                    inicializar();
                    MensagemSucesso("Sucesso !!!", "Compromisso atualizado com sucesso");

                } catch (Exception ex) {
                    MensagemErro("Erro!", "Consulte o administrador do sistema!");
                }

                agenda = new AgendaTarefas();

            } else {
                inicializar();
                MensagemErro("Erro!", "Altere a data. Data de TÉRMINO anterior á Data de INICIO");
            }
        }

    }

    public void Mover(ScheduleEntryMoveEvent eventoMovido) {
        for (AgendaTarefas at : listaEventos) {
            if (at.getId() == (long) eventoMovido.getScheduleEvent().getData()) {
                this.agenda = at;

                try {
                    repositorio.Salvar(agenda);
                    inicializar();
                    MensagemSucesso("Sucesso !!!", "Compromisso movido com sucesso");
                    break;
                } catch (Exception ex) {
                    MensagemErro("Erro!", "Consulte o administrador do sistema!");
                }
                agenda = new AgendaTarefas();
            }

        }
    }

    public void Aumentar(ScheduleEntryResizeEvent eventoAumentado) {
        for (AgendaTarefas at : listaEventos) {
            if (at.getId() == (long) eventoAumentado.getScheduleEvent().getData()) {
                this.agenda = at;

                try {
                    repositorio.Salvar(agenda);
                    inicializar();
                    MensagemSucesso("Sucesso !!!", "Compromisso alterado com sucesso");
                    break;
                } catch (Exception ex) {
                    MensagemErro("Erro!", "Consulte o administrador do sistema!");
                }
                agenda = new AgendaTarefas();
            }

        }
    }

    public void excluir() {
        if (agenda.getId() != null) {
            try {
                repositorio.Apagar(agenda);
                inicializar();
                MensagemSucesso("Sucesso !!!", "Compromisso excluído com sucesso");

            } catch (Exception ex) {
                MensagemErro("Erro!", "Consulte o administrador do sistema!");
            }
            agenda = new AgendaTarefas();
        }

    }

    public boolean controlarInterface() {
        return agenda.getId() != null;
    }

    public AgendaTarefas getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaTarefas agenda) {
        this.agenda = agenda;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public List<AgendaTarefas> getListaEventos() {
        return listaEventos;
    }

    public void setListaEventos(List<AgendaTarefas> listaEventos) {
        this.listaEventos = listaEventos;
    }

    protected void MensagemSucesso(String titulo, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, titulo);
        context.addMessage(null, m);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    protected void MensagemErro(String titulo, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, titulo);
        context.addMessage(null, m);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

}
