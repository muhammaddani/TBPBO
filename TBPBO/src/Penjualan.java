public class Penjualan extends Transaksi implements IBayar {

    public Penjualan(String namaPelanggan, String noHp, String alamat, String kodeBarang, String namaBarang,
            double hargaBarang, int jumlahBeli) {
        super(namaPelanggan, noHp, alamat, kodeBarang, namaBarang, hargaBarang, jumlahBeli);
    }

    @Override
    public double hitungTotalBayar() {
        return hargaBarang * getJumlahBeli();
    }

    @Override
    public int getJumlahBeli() {
        return super.jumlahBeli;
    }

    @Override
    public void tampilkanDetailTransaksi() {
        super.tampilkanDetailTransaksi();

        System.out.println("Total Bayar\t: " + hitungTotalBayar());
        System.out.println("+++++++++++++++++++++++++++");
        System.out.println("Kasir\t\t: " + kasir);
    }

    public int getTransactionID() {
        return transactionID;
    }
}
