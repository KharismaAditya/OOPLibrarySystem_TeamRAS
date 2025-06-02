package ui.programSection.dataBook;


public class Book {
    private String judul;
    private String penulis;
    private int stok;
    private String idBuku;

    public Book(String judul, String penulis, int stok, String idBuku){
        this.judul = judul;
        this.penulis = penulis;
        this.stok = stok;
        this.idBuku = idBuku;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public int getStok() {
        return stok;
    }

    public String getIdBuku() {
        return idBuku;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
