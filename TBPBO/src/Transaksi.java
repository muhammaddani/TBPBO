public class Transaksi {
private String namaPelanggan;
private String noHp;
private String namaBarang;
protected double hargaBarang;
protected int jumlahBeli;
private String namaSuperMarket = "Noar market";
private String alamat;
private String kodeBarang;
protected String kasir = "Dani";
protected int transactionID;

    public Transaksi(String namaPelanggan, String noHp, String alamat, String kodeBarang, String namaBarang,
            double hargaBarang, int jumlahBeli) {
        this.namaPelanggan = namaPelanggan; 
        this.noHp = noHp;
        this.alamat = alamat;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
        this.jumlahBeli = jumlahBeli; 
    }

    public Transaksi() {
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public int getJumlahBeli() {
        return jumlahBeli;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public double getHargaBarang() {
        return hargaBarang;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public void tampilkanDetailTransaksi() {
        System.out.println("DATA PELANGGAN");
        System.out.println("---------------------------");
        System.out.println("Nama Pelanggan\t: " + namaPelanggan);
        System.out.println("No HP\t\t: " + noHp);
        System.out.println("Alamat\t\t: " + alamat);
        System.out.println("+++++++++++++++++++++++++++");
        System.out.println("DATA PEMBELIAN BARANG");
        System.out.println("---------------------------");
        System.out.println("Kode barang\t: " + kodeBarang);
        System.out.println("Nama Barang\t: " + namaBarang);
        System.out.println("Harga Barang\t: " + hargaBarang);
        System.out.println("Jumlah Beli\t: " + jumlahBeli);
    }
}