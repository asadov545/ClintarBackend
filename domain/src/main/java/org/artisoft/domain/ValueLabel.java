package org.artisoft.domain;

public class ValueLabel<V, L> {
    private V value;
    private L label;

    public ValueLabel() {
    }

    public ValueLabel(V value, L label) {
        this.value = value;
        this.label = label;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public L getLabel() {
        return label;
    }

    public void setLabel(L label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ValueLabel{" +
                "value=" + value +
                ", label=" + label +
                '}';
    }
}
