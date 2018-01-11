

import br.edu.ifnmg.estagio.repositorios.Repositorio;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class DAOGenerico<T> implements Repositorio<T> {

    @PersistenceContext(name = "SistemaGerenciamentoEstagiosPU")
    protected EntityManager manager;
    private final Class tipo;
    String where = "";
    String orderby = "";
    String jpql = "select c from ";
    Map<String, Object> parametros = new HashMap<>();
    private HashMap<String, String> join = new HashMap<>();

    public DAOGenerico(Class t) {
        this.tipo = t;
    }

    @Override
    public Class getTipo() {
        return tipo;
    }

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Repositorio<T> Join(String campo, String alias) {
        join.put(campo, alias);
        return this;
    }

    public DAOGenerico<T> OrderBy(String campo, String order) {

        if (campo != null) {
            if (orderby.length() > 0) {
                orderby += ",";
            }

            orderby += "c." + campo + " " + order;
        }

        return this;
    }

    @Override
    public DAOGenerico<T> IgualA(String campo, Object valor) {

        if (valor == null || valor.toString().isEmpty()) {
            return this;
        }

        if (where.length() > 0) {
            where += " and ";
        }

        if (valor != null && !campo.contains(".")) {
            where = where + "c." + campo + " = :" + campo;
            parametros.put(campo, valor);
        }

        return this;
    }

    public DAOGenerico<T> Like(String campo, String valor) {

        if (valor == null || valor.isEmpty()) {
            return this;
        }

        if (where.length() > 0) {
            where += " and ";
        }

        if (valor != null && campo.contains(".")) {
            where = where + "c." + campo + " like '%" + valor + "%'";
        } else {
            where = where + "c." + campo + " like '%" + valor + "%'";
            //   where = where + campo + " like '%" + valor + "%'";
        }

        return this;
    }

    @Override
    public List<T> Buscar() {
        try {

            jpql += tipo.getSimpleName() + " c";

            if (join.size() > 0) {
                for (String key : join.keySet()) {

                    jpql += "  left join ";

                    jpql += (key) + (" ") + (join.get(key));

                    jpql += " on (c." + (key) + " = id)";

                }
            }

            if (where.length() > 0) {
                jpql = jpql + " where " + where;
            }

            if (orderby.length() > 0) {
                jpql = jpql + " order by " + orderby;
            }

            System.err.println("consulta1: " + jpql);

            Query consulta = manager.createQuery(jpql);

            for (String parametro : parametros.keySet()) {
                consulta.setParameter(parametro, parametros.get(parametro));
            }

            return consulta.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            where = "";
            jpql = "select c from ";
            orderby = "";
            parametros = new HashMap<>();
        }

    }

    @Override
    public boolean Salvar(T obj) {
        try {
            manager.merge(obj);
            manager.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public T Abrir(Long id) {
        return (T) manager.find(tipo, id);
    }

    @Override
    public boolean Apagar(T obj) {
        try {
            obj = (T) manager.merge(obj);
            manager.remove(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public T Abrir() {

        try {

            jpql += tipo.getSimpleName() + " c";

            if (where.length() > 0) {
                jpql = jpql + " where " + where;
            }

            if (orderby.length() > 0) {
                jpql = jpql + " order by " + orderby;
            }

            Query consulta = manager.createQuery(jpql);

            for (String parametro : parametros.keySet()) {
                consulta.setParameter(parametro, parametros.get(parametro));
            }

            return (T) consulta.getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            where = "";
            jpql = "select c from ";
            orderby = "";
            parametros = new HashMap<>();
        }

    }

}
