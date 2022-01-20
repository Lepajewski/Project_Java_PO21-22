package pl.put.poznan;

public class Product {
    int id;
    private String name;
    String type;
    private float price;
    private float weight;
    private int focalLength;
    private int aperture;
    //konstruktor
    public Product(int id, String name, String type, float price, float weight, int focalLength, int aperture) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.weight = weight;
        this.focalLength = focalLength;
        this.aperture = aperture;
    }
    //zwraca wszystkie parametry klasy produkt
    public String show() {
        return String.format("ID: %d\nNazwa: %s\nTyp: %s\nCena: %.2f\nWaga: %.2f\nOgniskowa: %d\nApertura: %d\n",
                id, name, type, price, weight, focalLength, aperture);
    }
    //zwraca wszystkie parametry klasy produkt w sposób umożliwiający łatwy zapis/odczyt z pliku
    public String write() {
        return String.format("%s;%d;%s;%.2f;%.2f;%d;%d;", type, id, name, price, weight, focalLength, aperture);
    }
}
