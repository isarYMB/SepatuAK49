package com.hp.ali.ecomerceapp.ChartAdapter;

public class ChartProduct {
    private int idCart;
    private int id;
    private String merek;
    private String brand;
    private String deskripsi;
    private int harga;
    private int diskon;
    private String gambar;
    private String ukuran;

    public ChartProduct(int idCart,int id, String merek, String brand, String deskripsi,int harga,int diskon, String gambar, String ukuran) {
        this.idCart = idCart;
        this.id = id;
        this.merek = merek;
        this.brand = brand;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.diskon = diskon;
        this.gambar = gambar;
        this.ukuran = ukuran;
    }

    public int getIdCart() {
        return idCart;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getHarga(){
        return harga;
    }

    public int getDiskon(){
        return diskon;
    }

    public String getGambar() {
        return gambar;
    }
    public String getUkuran() {
        return ukuran;
    }
}
