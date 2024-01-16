package project.database.objects;

public class Class {
    private String id;
    private int numero;
    private String sezione;
    private String indirizzo;

    public Class(String id, int numero, String sezione, String indirizzo) {
        this.id = id;
        this.numero = numero;
        this.sezione = sezione;
        this.indirizzo = indirizzo;
    }

    public String getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getSezione() {
        return sezione;
    }

    public String getIndirizzo() {
        return indirizzo;
    }
}
