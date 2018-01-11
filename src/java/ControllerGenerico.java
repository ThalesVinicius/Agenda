

import br.edu.ifnmg.estagio.repositorios.Repositorio;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Thales
 * @param <T>
 */
public class ControllerGenerico<T> {

    public ControllerGenerico() {

    }

    @Inject
    protected Conversation conversacao;

    public ControllerGenerico(String paginaListagem, String paginaEdicao) {
        this.paginaListagem = paginaListagem;
        this.paginaEdicao = paginaEdicao;
    }

    protected T entidade;
    protected T filtro;

    protected String paginaListagem;
    protected String paginaEdicao;

    private Repositorio<T> dao;

    protected Repositorio<T> getDao() {
        return dao;
    }

    protected void setDao(Repositorio<T> dao) {
        this.dao = dao;
    }

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }

    public T getFiltro() {
        return filtro;
    }

    public void setFiltro(T filtro) {
        this.filtro = filtro;
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

    public void salvar() {
        try {
            if (dao.Salvar(entidade)) {
                MensagemSucesso("Sucesso!", "Registro salvo com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(paginaEdicao);

            } else {
                MensagemErro("Erro!", "Consulte o administrador do sistema!");

            }
        } catch (Exception ex) {
            MensagemErro(ex.getMessage(), "Consulte o administrador do sistema!");

        }

        //  return paginaEdicao;
    }

    public void apagar() {
        try {
            if (dao.Apagar(entidade)) {
                conversacao.end();
                MensagemSucesso("Sucesso!", "Registro removido com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(paginaListagem);
            } else {
                MensagemErro("Erro!", "Consulte o administrador do sistema!");

            }
        } catch (Exception ex) {
            MensagemErro(ex.getMessage(), "Consulte o administrador do sistema!");

        }
    }

    public void voltar() {

        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(paginaListagem);

        } catch (IOException ex) {
            Logger.getLogger(ControllerGenerico.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String filtrar() {
        return paginaListagem;
    }

    public String novo() {
        conversacao.begin();
        return paginaEdicao;
    }

    public String abrir(T obj) {
        conversacao.begin();
        entidade = obj;

        return paginaEdicao;

    }

    public List<T> getListagem() {
        return dao.Buscar(filtro);
    }
    

}
