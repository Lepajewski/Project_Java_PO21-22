package pl.put.poznan;

public class Apochromat extends Achromatic {
    private final String lensCoating;
    //konstruktor, najpierw zwraca się do klasy wyższej - Apochromat
    public Apochromat(int id, String name, String type, float price, float weight, int focalLength, int aperture, int lens, String lensCoating) {
        super(id, name, type, price, weight, focalLength, aperture, lens);
        this.lensCoating = lensCoating;
    }
    //zwraca parametry, jest przesłaniana w klasie Achromatic
    public String show() {
        return super.show() + String.format("Powłoka soczewek: %s\n", lensCoating);
    }
    //zwraca parametry w formacie bazy danych, jest przesłaniana w klasie Achromatic
    public String write() {
        return super.write() + String.format(";%s", lensCoating);
    }
}
