

import java.util.List;

public interface Repositorio<T> {

    public boolean Salvar(T obj);

    public T Abrir(Long id);

    public boolean Apagar(T obj);

    public List<T> Buscar(T filtro);

    public T Abrir();

    public Repositorio<T> IgualA(String campo, Object valor);

    public Class getTipo();

    public List<T> Buscar();

    public Repositorio<T> Join(String campo, String alias);
}
