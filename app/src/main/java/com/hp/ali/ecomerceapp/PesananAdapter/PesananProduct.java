package com.hp.ali.ecomerceapp.PesananAdapter;

public class PesananProduct {
    private int id;
    private String merek;
    private String brand;
    private String harga;
    private String diskon;
    private String status;
    private String deskripsi;
    private String total;
    private String jumlah_pesanan;
    private String gambar;
    private String verifikasi;
    private String metodebayar;
    private String ukuran;


    public PesananProduct(int id, String merek, String brand,String harga,String diskon,
                          String status,String deskripsi, String total,String jumlah_pesanan,
                          String gambar, String verifikasi,String metodebayar,String ukuran) {
        this.id = id;
        this.merek = merek;
        this.brand = brand;
        this.status = status;
        this.harga = harga;
        this.diskon = diskon;
        this.deskripsi = deskripsi;
        this.total = total;
        this.gambar = gambar;
        this.jumlah_pesanan = jumlah_pesanan;
        this.verifikasi = verifikasi;
        this.metodebayar = metodebayar;
        this.ukuran = ukuran;
    }

    public int getId() {
        return id;
    }

    public String getMerek() {
        return merek;
    }

    public String getBrand() {
        return brand;
    }

    public String getStatus() {
        return status;
    }

    public String getHarga(){
        return harga;
    }
    public String getDiskon(){
        return diskon;
    }

    public String getDeskripsi(){
        return deskripsi;
    }

    public String getJumlah_pesanan() {
        return jumlah_pesanan;
    }

    public String getTotal() {
        return total;
    }

    public String getGambar() {
        return gambar;
    }

    public String getVerifikasi() {
        return verifikasi;
    }
    public String getMetodebayar() {
        return metodebayar;
    }


    public String getUkuran() {
        return ukuran;
    }
}
