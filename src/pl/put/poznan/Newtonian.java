package pl.put.poznan;

public class Newtonian extends Product {
    private int mirrors;
    //konstruktor, najpierw zwraca się do klasy wyższej - Produkt
    public Newtonian(int id, String name, String type, float price, float weight, int focalLength, int aperture, int mirrors) {
        super(id, name, type, price, weight, focalLength, aperture);
        this.mirrors = mirrors;
    }
    //zwraca parametry, jest przesłaniana w klasie Product
    public String show() {
        return super.show() + String.format("Liczba zwierciadeł: %d\n", mirrors);
    }
    //zwraca parametry w formacie bazy danych, jest przesłaniana w klasie Product
    public String write() {
        return super.write() + String.format("%d", mirrors);
    }
}
