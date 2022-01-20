package pl.put.poznan;

public class Cassegrain extends Newtonian {
    private final String mirrorCoating;
    //konstruktor, najpierw zwraca się do klasy wyższej - Newtonian
    public Cassegrain(int id, String name, String type, float price, float weight, int focalLength, int aperture, int mirrors, String mirrorCoating) {
        super(id, name, type, price, weight, focalLength, aperture, mirrors);
        this.mirrorCoating = mirrorCoating;
    }
    //zwraca parametry, jest przesłaniana w klasie Newtonian
    public String show() {
        return super.show() + String.format("Powłoka zwierciadeł: %s\n", mirrorCoating);
    }
    //zwraca parametry w formacie bazy danych, jest przesłaniana w klasie Newtonian
    public String write() {
        return super.write() + String.format(";%s", mirrorCoating);
    }
}
