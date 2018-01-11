

import br.edu.ifnmg.estagio.entidades.AgendaTarefas;
import br.edu.ifnmg.estagio.repositorios.AgendaTarefasRepositorio;
import java.util.List;
import javax.ejb.Singleton;

@Singleton
public class AgendaTarefasDAO extends DAOGenerico<AgendaTarefas> implements AgendaTarefasRepositorio {

    public AgendaTarefasDAO() {
        super(AgendaTarefas.class);
    }

    @Override
    public List<AgendaTarefas> Buscar(AgendaTarefas filtro) {

        return IgualA("dateInicio", filtro.getDateInicio())
                .IgualA("DateTermino", filtro.getDateTermino())
                .IgualA("Titulo", filtro.getTitulo())
                .IgualA("id", filtro.getId())
                .Buscar();
    }

}
