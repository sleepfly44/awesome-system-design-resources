package taboola;

public class Var {
    String name;
    Double value;

    public Var(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String toString() {
        return "Var{name='" + name + "', value=" + value + "}";
    }

}
