package ui.programSection.dataBook;

import java.util.Date;

public class Return {
    private int idPeminjaman;
    private String idBuku;
    private Date tanggalPeminjaman;
    private Date tanggalKembali;
    private String judul;
    private String penulis;

    public Return(int idPeminjaman, String idBuku, Date tanggalPeminjaman, String judul, String penulis){
        this.idPeminjaman = idPeminjaman;
        this.idBuku = idBuku;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.judul = judul;
        this.penulis = penulis;
    }


    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public String getIdBuku() {
        return idBuku;
    }

    public Date getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public Date getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(Date tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }
}
