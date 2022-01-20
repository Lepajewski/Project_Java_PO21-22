package pl.put.poznan;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import static javax.swing.JFileChooser.SAVE_DIALOG;

public class TelscopeShop extends JFrame {
    private JPanel mainPanel;
    private JTable productTable;
    private JButton addButton;
    private JPanel addPanel;
    private JButton confirmProduct;
    private JRadioButton newtonRadioButton;
    private JRadioButton cassegrainRadioButton;
    private JRadioButton achromatRadioButton;
    private JRadioButton apochromatRadioButton;
    private JPanel managePanel;
    private JTextField nameTF;
    private JTextField priceTF;
    private JTextField weightTF;
    private JTextField apertureTF;
    private JTextField focalTF;
    private JScrollPane scrollPane;
    private JLabel productCountLabel;
    private JTextField param1TF;
    private JTextField param2TF;
    private JLabel param1Label;
    private JLabel param2Label;
    private JButton deleteProduct;
    private JTextField removeProductTF;
    private JButton removeButton;
    private JPanel removePanel;
    private JLabel removeProductLabel;
    private JLabel errorRemoveLabel;
    private JCheckBox updateProductFlag;
    private JTextField updateProductIdTF;
    private JLabel updateProductLabel;
    private JButton deleteAllProducts;
    private JButton showProduct;
    private JTextArea productShow;
    private JPanel showProductPanel;
    private JButton closeProductShowButton;
    private JButton saveButton;
    private JButton wczytajProduktyButton;
    private final ButtonGroup telescopeTypes = new ButtonGroup();
    private DefaultTableModel model;
    private final List<Product> productList = new ArrayList<>();
    //inicjalizuje sklep i action listenery do wszystkich interaktywnych obiektów
    public TelscopeShop() {
        initPanel();
        addButton.addActionListener(a -> addProduct());
        confirmProduct.addActionListener(a -> confirmAddingProduct());
        newtonRadioButton.addActionListener(a -> getTelescopeType());
        cassegrainRadioButton.addActionListener(a -> getTelescopeType());
        achromatRadioButton.addActionListener(a -> getTelescopeType());
        apochromatRadioButton.addActionListener(a -> getTelescopeType());
        nameTF.addActionListener(a -> priceTF.grabFocus());
        priceTF.addActionListener(a -> weightTF.grabFocus());
        weightTF.addActionListener(a -> apertureTF.grabFocus());
        apertureTF.addActionListener(a -> focalTF.grabFocus());
        focalTF.addActionListener(a -> param1TF.grabFocus());
        param1TF.addActionListener(a -> param2TF.grabFocus());
        param2TF.addActionListener(a -> confirmAddingProduct());
        deleteProduct.addActionListener(a -> deleteProduct());
        removeButton.addActionListener(a -> confirmProductRemovalShow());
        updateProductFlag.addActionListener(a -> toggleUpdateFieldVisibility());
        deleteAllProducts.addActionListener(a -> deleteAllProducts());
        showProduct.addActionListener(a -> selectProduct());
        closeProductShowButton.addActionListener(a -> closeProductShow());
        saveButton.addActionListener(a -> {
            try {
                saveProducts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        wczytajProduktyButton.addActionListener(a -> {
            try {
                readProducts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    //inicjalizacja głównego panelu programu
    private void initPanel() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sklep z teleskopami");
        setMinimumSize(new Dimension(900,700));
        setVisible(true);
        scrollPane.setMinimumSize(new Dimension(550, 450));
        String[] colNames = {"Typ", "Id", "Nazwa", "Cena"};
        model = new DefaultTableModel(colNames, 0);
        productTable.setModel(model);
        productTable.setAutoCreateRowSorter(true);
        productTable.setDefaultEditor(Object.class, null);
        managePanel.setVisible(true);
        removePanel.setVisible(false);
        showProductPanel.setVisible(false);
        errorRemoveLabel.setVisible(false);
        addPanel.setVisible(false);
        telescopeTypes.add(newtonRadioButton);
        telescopeTypes.add(cassegrainRadioButton);
        telescopeTypes.add(achromatRadioButton);
        telescopeTypes.add(apochromatRadioButton);
        param1TF.setVisible(false);
        param2TF.setVisible(false);
        updateProductIdTF.setVisible(false);
        updateProductLabel.setVisible(false);
    }
    //obsługuje widoczność panelu z dodawaniem/aktualizacją produktu
    private void addProduct() {
        managePanel.setVisible(false);
        addPanel.setVisible(true);
        confirmProduct.setText("Sprawdź i dodaj");
        updateProductFlag.setSelected(false);
        updateProductIdTF.setText("");
    }
    //walidacja wprowadzonych parametrów
    //oraz zapis danych do listy produktów i do tabeli
    private void confirmAddingProduct() {
        managePanel.setVisible(true);
        addPanel.setVisible(false);
        resetColor();
        boolean isProductValid = true;
        int type = getTelescopeType();
        String typeName = "";
        String name = nameTF.getText();
        String priceStr = priceTF.getText();
        String weightStr = weightTF.getText();
        String apertureStr = apertureTF.getText();
        String focalStr = focalTF.getText();
        String param1Str = param1TF.getText();
        String param2Str = param2TF.getText();
        float price = 0, weight = 0;
        int aperture = 0, focal = 0, id;
        int param1 = 0;

        switch (type) {
            case 0: {
                typeName = "Newton";
                break;
            }
            case 1: {
                typeName = "Schmidt-Cassegrain";
                break;
            }
            case 2: {
                typeName = "Achromat";
                break;
            }
            case 3: {
                typeName = "Apochromat";
                break;
            }
            default: {
                newtonRadioButton.setBackground(Color.red);
                cassegrainRadioButton.setBackground(Color.red);
                achromatRadioButton.setBackground(Color.red);
                apochromatRadioButton.setBackground(Color.red);
                telescopeTypes.clearSelection();
                isProductValid = false;
                addProduct();
                break;
            }
        }

        if (Objects.equals(nameTF.getText(), "") || nameTF.getText().contains(";")) {
            nameTF.setBackground(Color.red);
            isProductValid = false;
            if (param1TF.isVisible())
                addProduct();
        }
        if (param2TF.isVisible() && Objects.equals(param2TF.getText(), "") || param2TF.getText().contains(";")) {
            param2TF.setBackground(Color.red);
            param2TF.setText("");
            isProductValid = false;
            addProduct();
        }
        try {
            assert priceStr != null;
            price = Float.parseFloat(priceStr);
            if (price < 0.0) throw new Exception();
        } catch (Exception e) {
            priceTF.setBackground(Color.red);
            priceTF.setText("");
            isProductValid = false;
            addProduct();
        }
        try {
            assert weightStr != null;
            weight = Float.parseFloat(weightStr);
            if (weight < 0.0) throw new Exception();
        } catch (Exception e) {
            weightTF.setBackground(Color.red);
            weightTF.setText("");
            isProductValid = false;
            addProduct();
        }
        try {
            assert apertureStr != null;
            aperture = Integer.parseInt(apertureStr);
            if (aperture < 0) throw new Exception();
        } catch (Exception e) {
            apertureTF.setBackground(Color.red);
            apertureTF.setText("");
            isProductValid = false;
            addProduct();
        }
        try {
            assert focalStr != null;
            focal = Integer.parseInt(focalStr);
            if (focal < 0) throw new Exception();
        } catch (Exception e) {
            focalTF.setBackground(Color.red);
            focalTF.setText("");
            isProductValid = false;
            addProduct();
        }
        if (param1TF.isVisible()) {
            try {
                assert param1Str != null;
                param1 = Integer.parseInt(param1Str);
                if (param1 < 0) throw new Exception();
            } catch (Exception e) {
                param1TF.setBackground(Color.red);
                param1TF.setText("");
                isProductValid = false;
                addProduct();
            }
        }

        if (isProductValid) {
            sortById();
            id = assignId();
            Product n = new Product(-1,"","",-1,-1,-1,-1);
            switch (type) {
                case 0: {
                    n = new Newtonian(id, name, typeName, price, weight, focal, aperture, param1);
                }
                case 1: {
                    n = new Cassegrain(id, name, typeName, price, weight, focal, aperture, param1, param2Str);
                }
                case 2: {
                    n = new Achromatic(id, name, typeName, price, weight, focal, aperture, param1);
                }
                case 3: {
                    n = new Apochromat(id, name, typeName, price, weight, focal, aperture, param1, param2Str);
                }
            }
            Object[] row = {typeName, id, name, price};

            if (updateProductFlag.isSelected() && !Objects.equals(updateProductIdTF.getText(), "")) {
                    try {
                        int idx = Integer.parseInt(updateProductIdTF.getText());
                        sortById();
                        if(productList.removeIf(p -> p.id == idx)) {
                            n.id = idx;
                            productList.add(n);
                            row[1] = idx;
                            int i;
                            for (i = 0; i < model.getRowCount(); i++) {
                                if ((int) model.getValueAt(i, 1) == idx) {
                                    model.setValueAt(row[0], i, 0);
                                    model.setValueAt(row[1], i, 1);
                                    model.setValueAt(row[2], i, 2);
                                    model.setValueAt(row[3], i, 3);
                                    break;
                                }
                            }
                            updateProductLabel.setText("Podaj ID produktu:");
                            toggleUpdateFieldVisibility();
                        }
                    } catch (Exception e) {
                        updateProductLabel.setText("Niepoprawne ID produktu:");
                        updateProductIdTF.setBackground(Color.red);
                        updateProductIdTF.setText("");
                        addProduct();
                    }
                    confirmProduct.setText("Sprawdź i zaktualizuj");
            } else {
                clearTextFields();
                confirmProduct.setText("Sprawdź i dodaj");
                productList.add(n);
                model.addRow(row);
                productCountLabel.setText(String.format("Produktów: %d", productList.size()));
            }
        }

    }
    //przywraca domyślny kolor - biały - do wszystkich pól tekstowych
    private void resetColor() {
        priceTF.setBackground(Color.white);
        nameTF.setBackground(Color.white);
        weightTF.setBackground(Color.white);
        apertureTF.setBackground(Color.white);
        focalTF.setBackground(Color.white);
        param1TF.setBackground(Color.white);
        param2TF.setBackground(Color.white);
        updateProductIdTF.setBackground(Color.white);
        newtonRadioButton.setBackground(mainPanel.getBackground());
        cassegrainRadioButton.setBackground(mainPanel.getBackground());
        achromatRadioButton.setBackground(mainPanel.getBackground());
        apochromatRadioButton.setBackground(mainPanel.getBackground());
    }
    //czyści pola do wprowadzania parametrów
    private void clearTextFields() {
        telescopeTypes.clearSelection();
        nameTF.setText("");
        priceTF.setText("");
        weightTF.setText("");
        apertureTF.setText("");
        focalTF.setText("");
        param1TF.setText("");
        param2TF.setText("");
    }
    //zwraca typ wybranego teleskopu w formie liczby
    private int getTelescopeType() {
        if (newtonRadioButton.isSelected()) {
            toggleVisibleParamFields("Liczba luster:");
            return 0;
        } else if (cassegrainRadioButton.isSelected()) {
            toggleVisibleParamFields("Liczba luster:", "Powłoka luster");
            return 1;
        }
        else if (achromatRadioButton.isSelected()) {
            toggleVisibleParamFields("Liczba soczewek:");
            return 2;
        }
        else if (apochromatRadioButton.isSelected()) {
            toggleVisibleParamFields("Liczba soczewek:", "Powłoka soczewek");
            return 3;
        }
        return -1;
    }
    //przełącza widoczność pola z pierwszym paramterem
    //(dla klas Newton i Achromatic)
    private void toggleVisibleParamFields(String p1) {
        param1Label.setVisible(true);
        param1TF.setVisible(true);
        param1Label.setText(p1);
        param2Label.setText("");
        param2TF.setVisible(false);
    }
    //przełącza widoczność pól obsługujących wprowadzanie parametrów
    //pierwszego i drugiego (dla klas Cassegrain oraz Apochromat)
    private void toggleVisibleParamFields(String p1, String p2) {
        param1Label.setVisible(true);
        param2Label.setVisible(true);
        param1TF.setVisible(true);
        param2TF.setVisible(true);
        param1Label.setText(p1);
        param2Label.setText(p2);
    }
    //przypisuje pierwsze najniższe wolne ID
    private int assignId() {
        int id = 0;
        sortById();
        for (Product p : productList) {
            if (id == p.id ) {
                id++;
            }
        }
        return id;
    }
    //obsługuje widoczność panelu z wyborem produktu do usunięcia
    private void deleteProduct() {
        managePanel.setVisible(false);
        removePanel.setVisible(true);
        errorRemoveLabel.setVisible(false);
        removeButton.setText("Usuń");
    }
    //walidacja wprowadzonych danych o usuwanym produkcie
    //lub walidacja wprowadzonych danych o produkcie wybranym do wyświetlenia
    //głównie obsługuje wyświetlany tekst i widoczność pól
    private void confirmProductRemovalShow() {
        String idStr = removeProductTF.getText();
        if (!Objects.equals(idStr, "")) {
            try {
                int id = Integer.parseInt(idStr);
                if (Objects.equals(removeButton.getText(), "Wyświetl")) {
                    showProductById(id);
                } else if (Objects.equals(removeButton.getText(), "Usuń")) {
                    deleteProductByID(id);
                    errorRemoveLabel.setVisible(false);
                    managePanel.setVisible(true);
                    removePanel.setVisible(false);
                }
                removeProductTF.setBackground(Color.white);
                removeProductTF.setText("");
                removeProductLabel.setText("Podaj ID produktu:");
            } catch (Exception e) {
                errorRemoveLabel.setVisible(true);
                removeProductLabel.setText("Niepoprawne ID produktu.");
                removeProductTF.setBackground(Color.red);
            }
        }
        else {
            removeProductLabel.setText("Podaj ID produktu: ");
            errorRemoveLabel.setVisible(false);
            managePanel.setVisible(true);
            removePanel.setVisible(false);
        }
    }
    //usuwa produkt o wybranym id z listy i z modelu tabeli
    private void deleteProductByID(int id) {
        productList.removeIf(p -> p.id == id);
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if ((int) model.getValueAt(i, 1) == id)
                model.removeRow(i);
        }
    }
    //przełącza widoczność pola z wyborem aktualizacji produktu
    private void toggleUpdateFieldVisibility() {
        if (updateProductFlag.isSelected()) {
            updateProductIdTF.setVisible(true);
            updateProductLabel.setVisible(true);
        } else {
            updateProductIdTF.setVisible(false);
            updateProductLabel.setVisible(false);
        }

    }
    //sortuje listę produktów rosnąco według id
    private void sortById() {
        productList.sort((o1, o2) -> {
            if (o1.id > o2.id) return 1;
            return 0;
        });
    }
    //usuwa wszystkie produkty ze sklepu
    private void deleteAllProducts() {
        productCountLabel.setText("Produktów: 0");
        productList.clear();
        model.setRowCount(0);
    }
    //pokazuje panel z polem do wybrania produktu, który chcemy wyświetlić
    private void selectProduct() {
        managePanel.setVisible(false);
        removePanel.setVisible(true);
        errorRemoveLabel.setVisible(false);
        removeProductLabel.setText("Podaj ID produktu do wyświetlenia:");
        removeButton.setText("Wyświetl");
    }
    //pokazuje panel z szczegółowymi parametrami produktu
    private void showProductById(int id){
        int pos = -1;
        for (int i = 0; i < productList.size(); i++)
            if (productList.get(i).id == id) {
                pos = i;
                break;
            }
        if (pos == -1) return;

        productShow.setText(productList.get(pos).show());
        removePanel.setVisible(false);
        showProductPanel.setVisible(true);
    }
    //chowa panel z wyświetlanym produktem
    private void closeProductShow() {
        showProductPanel.setVisible(false);
        managePanel.setVisible(true);
        productShow.setText("");
    }
    //zapisuje bieżące produkty do pliku
    private void saveProducts() throws IOException {
        JFrame saveDialogFrame = new JFrame();
        JFileChooser fileChooserDialog = new JFileChooser();
        fileChooserDialog.setAcceptAllFileFilterUsed(false);
        fileChooserDialog.setDialogTitle("Wybierz plik do zapisania");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Plik z bazą sklepu *.shop", "shop");
        fileChooserDialog.addChoosableFileFilter(filter);
        fileChooserDialog.setCurrentDirectory(new File("/home/lukaszk/Documents/PO/laby/projekt Java/"));

        int userSelection = fileChooserDialog.showSaveDialog(saveDialogFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooserDialog.getSelectedFile();

            if (fileToSave.exists() && fileChooserDialog.getDialogType() == SAVE_DIALOG) {
                int res = JOptionPane.showConfirmDialog(this, "Wybrany plik już istnieje, nadpisać?", "Plik już istnieje", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (res) {
                    case JOptionPane.YES_OPTION: {
                        FileWriter writer = new FileWriter(fileToSave);
                        fileChooserDialog.approveSelection();
                        for (Product p : productList) {
                            writer.write(p.write() + "\n");
                        }
                        writer.close();
                        return;
                    }
                    case JOptionPane.NO_OPTION:
                    case JOptionPane.CLOSED_OPTION:
                    case JOptionPane.CANCEL_OPTION:
                        return;
                }
            }
            FileWriter writer = new FileWriter(fileToSave);
            for (Product p : productList) {
                writer.write(p.write() + "\n");
            }
            writer.close();
        }
    }
    //otwiera plik oraz wczytuje dane o produktach
    private void readProducts() throws IOException {
        JFrame readDialogFrame = new JFrame();
        JFileChooser fileChooserDialog = new JFileChooser();
        fileChooserDialog.setAcceptAllFileFilterUsed(false);
        fileChooserDialog.setDialogTitle("Wybierz plik do otwarcia");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki z bazą sklepu *.shop", "shop");
        fileChooserDialog.addChoosableFileFilter(filter);
        fileChooserDialog.setCurrentDirectory(new File("/home/lukaszk/Documents/PO/laby/projekt Java/")); //działa tylko u mnie

        int userSelection = fileChooserDialog.showOpenDialog(readDialogFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            deleteAllProducts();
            String fileToRead = fileChooserDialog.getSelectedFile().getAbsolutePath();
            BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
            String line = reader.readLine();
            while (line != null) {
                assertProduct(line);
                line = reader.readLine();
            }
            reader.close();
        }
    }
    //dodaje produkty z pliku do modelu tabeli oraz do listy z produktami
    private void assertProduct(String data) {
        String[] parameters = new String[9];
        int idx = 0;
        do
        {
            parameters[idx] = data.substring(0, data.indexOf(';'));
            data = data.replaceFirst(parameters[idx]+';', "");
            idx++;
        } while (data.contains(";"));
        parameters[idx] = data;

        switch (parameters[0]) {
            case "Newton":
            {
                productList.add(new Newtonian(Integer.parseInt(parameters[1]), parameters[2], parameters[0],
                        Float.parseFloat(parameters[3]), Float.parseFloat(parameters[4]),
                        Integer.parseInt(parameters[5]), Integer.parseInt(parameters[6]), Integer.parseInt(parameters[7])));
                Object[] row = {parameters[0], Integer.parseInt(parameters[1]), parameters[2], Float.parseFloat(parameters[3])};
                model.addRow(row);
                break;
            }
            case "Schmidt-Cassegrain": {
                productList.add(new Cassegrain(Integer.parseInt(parameters[1]), parameters[2], parameters[0],
                        Float.parseFloat(parameters[3]), Float.parseFloat(parameters[4]),
                        Integer.parseInt(parameters[5]), Integer.parseInt(parameters[6]), Integer.parseInt(parameters[7]), parameters[8]));
                Object[] row = {parameters[0], Integer.parseInt(parameters[1]), parameters[2], Float.parseFloat(parameters[3])};
                model.addRow(row);
                break;
            }
            case "Achromat": {
                productList.add(new Achromatic(Integer.parseInt(parameters[1]), parameters[2], parameters[0],
                        Float.parseFloat(parameters[3]), Float.parseFloat(parameters[4]),
                        Integer.parseInt(parameters[5]), Integer.parseInt(parameters[6]), Integer.parseInt(parameters[7])));
                Object[] row = {parameters[0], Integer.parseInt(parameters[1]), parameters[2], Float.parseFloat(parameters[3])};
                model.addRow(row);
                break;
            }
            case "Apochromat": {
                productList.add(new Apochromat(Integer.parseInt(parameters[1]), parameters[2], parameters[0],
                        Float.parseFloat(parameters[3]), Float.parseFloat(parameters[4]),
                        Integer.parseInt(parameters[5]), Integer.parseInt(parameters[6]), Integer.parseInt(parameters[7]), parameters[8]));
                Object[] row = {parameters[0], Integer.parseInt(parameters[1]), parameters[2], Float.parseFloat(parameters[3])};
                model.addRow(row);
                break;
            }
        }
    }
}
