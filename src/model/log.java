package model;

import java.util.Date;

public class log {
    private int idpeminjaman;
    private String idBuku;
    private String judul;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String idUser;

    public log(int idpeminjaman, String idBuku,String judul, Date tanggalPinjam, Date tanggalKembali, String idUser){
        this.idpeminjaman = idpeminjaman;
        this.idBuku = idBuku;
        this.judul = judul;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.idUser = idUser;
    }

    public int getIdpeminjaman() {
        return idpeminjaman;
    }

    public String getIdBuku() {
        return idBuku;
    }

    public String getJudul() {
        return judul;
    }

    public Date getTanggalPinjam() {
        return tanggalPinjam;
    }

    public Date getTanggalKembali() {
        return tanggalKembali;
    }

    public String getIdUser() {
        return idUser;
    }
}
