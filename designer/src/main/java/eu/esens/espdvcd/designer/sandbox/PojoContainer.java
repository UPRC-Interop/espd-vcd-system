package eu.esens.espdvcd.designer.sandbox;

/**
 * Created by ixuz on 3/8/16.
 */
public class PojoContainer<T> {
    private T pojo;

    public PojoContainer(T pojo) {
        setPojo(pojo);
    }

    public void setPojo(T pojo) {
        this.pojo = pojo;
    }

    public T getPojo() {
        return this.pojo;
    }
}
