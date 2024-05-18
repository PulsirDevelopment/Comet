package net.pulsir.comet.utils.wrapper;

public interface IWrapper<K,V> {

    K wrap(V v);
    V unwrap(K k);

    String toString(V v);
    V to(String string);
}
