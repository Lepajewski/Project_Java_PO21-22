package pl.put.poznan;

public class Achromatic extends Product {
    protected int lens;
    //konstruktor, najpierw zwraca się do klasy wyższej - Produkt
    public Achromatic(int id, String name, String type, float price, float weight, int focalLength, int aperture, int lens) {
        super(id, name, type, price, weight, focalLength, aperture);
        this.lens = lens;
    }
    //zwraca parametry, jest przesłaniana w klasie Product
    public String show() {
        return super.show() + String.format("Liczba soczewek: %d\n", lens);
    }
    //zwraca parametry w formacie bazy danych, jest przesłaniana w klasie Product
    public String write() {
        return super.write() + String.format("%d", lens);
    }
}
