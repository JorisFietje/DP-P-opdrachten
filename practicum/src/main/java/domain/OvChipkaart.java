package domain;


import java.sql.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaart {

    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;

    private Reiziger reiziger;

    private List<Product> producten = new ArrayList<>();

    public OvChipkaart() {
    }

    public OvChipkaart(int kaartNummer, Date geldigTot, int klasse, double saldo) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }


    public boolean voegToeProduct(Product product) {
        return product != null && product.voegToeOVChipkaart(this);
    }


    public boolean verwijderProduct(Product product) {
        return product != null && product.verwijderOVChipkaart(this);
    }


    private String productenAlsTekst() {
        if (producten == null || producten.isEmpty()) {
            return "geen producten";
        }
        StringBuilder sb = new StringBuilder(producten.size() + " producten: ");
        for (int i = 0; i < producten.size(); i++) {
            sb.append(i > 0 ? ", " : "").append(producten.get(i).getNaam());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String reizigerInfo = (reiziger == null)
                ? "geen reiziger"
                : "reiziger #" + reiziger.getReizigerId();
        return String.format(Locale.ROOT, "OvChipkaart {#%d, klasse %d, saldo %.2f, geldig tot %s, %s, %s}",
                kaartNummer, klasse, saldo, geldigTot, reizigerInfo, productenAlsTekst());
    }
}
