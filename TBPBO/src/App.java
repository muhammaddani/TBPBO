import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;

public class App {
    private static String namaSuperMarket = "Noar Market"; 
    private static String inUname;                    
    private static String uname = "daninoar";               
    private static String inPass;
    private static String pass = "daniganteng123";             
    private static String captcha = "q1w2E3";
    private static String inCaptcha;
    static String url = "jdbc:mysql://localhost:3306/tugaspbo";
    static String dbuser = "root";
    static String dbpassword = "";

    private static Map<Integer, Transaksi> transaksiMap = new HashMap<>(); 
    
    static Date date = new Date(); 
    static SimpleDateFormat tanggal = new SimpleDateFormat("E dd/MM/yyyy"); 
    static SimpleDateFormat jam = new SimpleDateFormat("hh:mm:ss z"); 

    public static void main(String[] args) throws Exception {
        Transaksi tes = new Transaksi();   
        try {
            try (Scanner scan = new Scanner(System.in)) {
                
                while (inUname != uname || inPass != pass) { 
                    System.out.print("\nUsername\t: ");                 
                    inUname = scan.nextLine();                       
                    System.out.print("Password\t: ");
                    inPass = scan.nextLine();

                 
                    if (inUname.equals(uname) && inPass.equals(pass)) { 
                        System.out.println("\nKode Captcha " + captcha);
                       
                        while (inCaptcha != captcha) {                   
                            System.out.print("Entry Captcha\t:");
                            inCaptcha = scan.nextLine();

                          
                            if (inCaptcha.equalsIgnoreCase(captcha)) { 
                                boolean exitProgram = false;
                               
                                while (!exitProgram) {  
                                    System.out.println("\nPilih Menu:");
                                    System.out.println("1. Buat Data");
                                    System.out.println("2. Lihat Data");
                                    System.out.println("3. Lihat Data Berdasarkan ID");
                                    System.out.println("4. Update Data");
                                    System.out.println("5. Hapus Data");
                                    System.out.println("6. Tutup program\n");

                                   
                                    int choice = scan.nextInt();
                                    scan.nextLine();
                                    switch (choice) { 
                                        case 1:
                                           
                                            System.out.print("Masukkan Nama Pelanggan\t\t: ");
                                            String namaPelanggan = scan.nextLine();

                                            System.out.print("Masukkan No HP\t\t\t: ");
                                            String noHp = scan.nextLine();

                                            System.out.print("Masukkan Alamat\t\t\t: ");
                                            String alamat = scan.nextLine();

                                            System.out.print("Masukkan Kode Barang\t\t: ");
                                            String kodeBarang = scan.nextLine();

                                            System.out.print("Masukkan Nama Barang\t\t: ");
                                            String namaBarang = scan.nextLine();

                                           
                                            double hargaBarang = 0;
                                            boolean valid = false; 
                                            
                                            while (!valid) { 
                                                try {
                                                    System.out.print("Masukkan Harga Barang\t\t: ");
                                                    hargaBarang = scan.nextDouble();
                                                    scan.nextLine();
                                                    
                                                    new Transaksi("", "", "", namaBarang, namaBarang, hargaBarang, 0); 
                                                    valid = true; 
                                                } catch (InputMismatchException | IllegalArgumentException e) { 
                                                    System.out.println( 
                                                            "Masukan tidak valid. Harga barang harus berupa angka."); 
                                                    scan.nextLine();
                                                }
                                            }

                                            int jumlahBeli;
                                            do {
                                                try {
                                                    System.out.print("Masukkan Jumlah Beli \t\t: ");
                                                    jumlahBeli = scan.nextInt();
                                                    

                                                    if (jumlahBeli <= 0) {
                                                        System.out.println("Jumlah beli harus lebih besar dari 0.");
                                                        jumlahBeli = -1;; 
                                                    }
                                                    
                                                    new Transaksi("", "", "", namaBarang, namaBarang, 0, jumlahBeli); 
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Masukan tidak valid. Jumlah beli harus berupa angka.");
                                                    scan.nextLine();  
                                                    jumlahBeli = -1; 
                                                } catch (IllegalArgumentException e) {
                                                    System.out.println(e.getMessage());
                                                    jumlahBeli = -1; 
                                                }
                                            } while (jumlahBeli == -1);

                                            Penjualan penjualan = new Penjualan(namaPelanggan, noHp, alamat, kodeBarang, namaBarang, hargaBarang, jumlahBeli);
                                            saveToDatabase(penjualan);
                                            updateInDatabase(penjualan);

                                            System.out.println("\n" + namaSuperMarket.toUpperCase());
                                            System.out.println("Tanggal\t\t: " + tanggal.format(date));
                                            System.out.println("Waktu\t\t: " + jam.format(date));
                                            System.out.println("===========================\n");
                                            penjualan.tampilkanDetailTransaksi();
                                            break;

                                        case 2:
                                            retrieveFromDatabase();
                                            break;

                                        case 3:
                                            System.out.print("Masukkan ID transaksi yang ingin dilihat: ");
                                            int retrieveTransactionId = scan.nextInt();
                                            scan.nextLine();

                                            if (isTransactionIdValid(retrieveTransactionId)) { 
                                                Transaksi retrievedTransaction = getTransactionByIdFromDatabase(retrieveTransactionId); 
                                                if (retrievedTransaction != null) { 
                                                    transaksiMap.put(retrieveTransactionId, retrievedTransaction); 
                                                    retrieveTransactionById(retrieveTransactionId); 
                                                } else {
                                                    System.out.println("Transaksi dengan ID " + retrieveTransactionId
                                                            + " tidak ditemukan di database.");
                                                }
                                            } else {
                                                System.out.println("Transaction ID tidak valid atau tidak ditemukan.");
                                            }
                                            break;

                                        case 4:
                                            System.out.print("Masukkan ID transaksi yang ingin diupdate: ");
                                            int updIdTransaksi = scan.nextInt();
                                            scan.nextLine();

                                            if (isTransactionIdValid(updIdTransaksi)) {
                                                System.out.print("Masukkan Nama Pelanggan\t: ");
                                                String updNamaPelanggan = scan.nextLine();

                                                System.out.print("Masukkan No HP\t\t: ");
                                                String updNoHp = scan.nextLine();

                                                System.out.print("Masukkan Alamat\t\t: ");
                                                String updAlamat = scan.nextLine();

                                                System.out.print("Masukkan Kode Barang\t: ");
                                                String updKodeBarang = scan.nextLine();

                                                System.out.print("Masukkan Nama Barang\t: ");
                                                String updNamaBarang = scan.nextLine();

                                                System.out.print("Masukkan Harga Barang\t: ");
                                                double updHargaBarang = scan.nextDouble();

                                                System.out.print("Masukkan Jumlah Beli\t: ");
                                                int updJumlahBeli = scan.nextInt();

                                                Penjualan updPenjualan = new Penjualan(updNamaPelanggan, updNoHp, updAlamat, updKodeBarang, updNamaBarang, updHargaBarang, updJumlahBeli);
                                                updPenjualan.setTransactionID(updIdTransaksi);
                                                updateInDatabase(updPenjualan);

                                                System.out.println("Data berhasil diupdate di database.");
                                            } else {
                                                System.out.println("Transaction ID tidak valid atau tidak ditemukan.");
                                            }
                                            break;

                                        case 5:
                                            System.out.print("Masukkan ID transaksi yang ingin dihapus: ");
                                            int hapusIdTransaksi = scan.nextInt();
                                            scan.nextLine();
                                            if (isTransactionIdValid(hapusIdTransaksi)) { 
                                                deleteFromDatabase(hapusIdTransaksi); 
                                                System.out.println("Data berhasil dihapus dari database.");
                                            } else {
                                                System.out.println("Transaction ID tidak valid atau tidak ditemukan.");
                                            }
                                            break;

                                        case 6:
                                            try (Connection connection = DriverManager.getConnection(url, dbuser, dbpassword)){
                                            String sql = "DELETE FROM transaksi";
                                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                                preparedStatement.executeUpdate(sql);
                                            }
                                        }
                                            exitProgram = true;
                                            break;

                                        default:
                                            System.out.println("Pilihan tidak valid.");
                                            break;
                                    }
                                }
                                break;
                            } else {
                                System.out.println("\nKode captcha salah");
                            }
                        }
                        break;
                    } else {
                        System.out.println("Username atau Password salah");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private static void saveToDatabase(Penjualan penjualan) {
        try (Connection connection = DriverManager.getConnection(url, dbuser, dbpassword)) { 
            String query = "INSERT INTO transaksi (nama_pelanggan, no_hp, alamat, kode_barang, nama_barang, harga_barang, jumlah_beli) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, penjualan.getNamaPelanggan()); 
                preparedStatement.setString(2, penjualan.getNoHp());          
                preparedStatement.setString(3, penjualan.getAlamat());        
                preparedStatement.setString(4, penjualan.getKodeBarang());
                preparedStatement.setString(5, penjualan.getNamaBarang());
                preparedStatement.setDouble(6, penjualan.getHargaBarang());
                preparedStatement.setInt(7, penjualan.getJumlahBeli());
                preparedStatement.executeUpdate(); 
            }
        } catch (Exception e) {
            System.out.println("Error saat menyimpan ke database: " + e.getMessage());
        }
    }

    private static void retrieveFromDatabase() {
        try (Connection connection = DriverManager.getConnection(url, dbuser, dbpassword)) { 
            String query = "SELECT * FROM transaksi"; 
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) { 
                    while (resultSet.next()) { 
                        System.out.println("\nTransaction ID\t: " + resultSet.getInt("id"));
                        System.out.println("Nama Pelanggan\t: " + resultSet.getString("nama_pelanggan"));
                        System.out.println("No HP\t\t: " + resultSet.getString("no_hp"));
                        System.out.println("Alamat\t\t: " + resultSet.getString("alamat"));
                        System.out.println("Kode Barang\t: " + resultSet.getString("kode_barang"));
                        System.out.println("Nama Barang\t: " + resultSet.getString("nama_barang"));
                        System.out.println("Harga Barang\t: " + resultSet.getDouble("harga_barang"));
                        System.out.println("Jumlah Beli\t: " + resultSet.getInt("jumlah_beli"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error saat mengambil dari database: " + e.getMessage());
        }
    }

    private static void updateInDatabase(Penjualan penjualan) {
        try (Connection connection = DriverManager.getConnection(url, dbuser, dbpassword)) {
            String query = "UPDATE transaksi SET nama_pelanggan=?, no_hp=?, alamat=?, kode_barang=?, nama_barang=?, harga_barang=?, jumlah_beli=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(8, penjualan.getTransactionID());
                preparedStatement.setString(1, penjualan.getNamaPelanggan());
                preparedStatement.setString(2, penjualan.getNoHp());
                preparedStatement.setString(3, penjualan.getAlamat());
                preparedStatement.setString(4, penjualan.getKodeBarang());
                preparedStatement.setString(5, penjualan.getNamaBarang());
                preparedStatement.setDouble(6, penjualan.getHargaBarang());
                preparedStatement.setInt(7, penjualan.getJumlahBeli());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error saat mengupdate di database: " + e.getMessage());
        }
    }

    private static boolean isTransactionIdValid(int transactionID) {
        try (Connection connection = DriverManager.getConnection(url, dbuser, dbpassword)) {
            String query = "SELECT * FROM transaksi WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) { 
                preparedStatement.setInt(1, transactionID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); 
                }
            }
        } catch (Exception e) {
            System.out.println("Error saat memeriksa transaction ID: " + e.getMessage());
            return false;
        }
    }

    private static void deleteFromDatabase(int transactionID) {
        try (Connection connection = DriverManager.getConnection(url, dbuser, dbpassword)) {
            String query = "DELETE FROM transaksi WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, transactionID);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error saat menghapus dari database: " + e.getMessage());
        }
    }

    private static Transaksi getTransactionByIdFromDatabase(int transactionID) {
        try (Connection connection = DriverManager.getConnection(url, dbuser, dbpassword)) {
            String query = "SELECT * FROM transaksi WHERE id = ?"; 
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) { 
                preparedStatement.setInt(1, transactionID); 
                try (ResultSet resultSet = preparedStatement.executeQuery()) { 
                    if (resultSet.next()) { 
                        String namaPelanggan = resultSet.getString("nama_pelanggan");
                        String noHp = resultSet.getString("no_hp");
                        String alamat = resultSet.getString("alamat");
                        String kodeBarang = resultSet.getString("kode_barang");
                        String namaBarang = resultSet.getString("nama_barang");
                        double hargaBarang = resultSet.getDouble("harga_barang");
                        int jumlahBeli = resultSet.getInt("jumlah_beli");

                        Penjualan penjualan = new Penjualan(namaPelanggan, noHp, alamat, kodeBarang, namaBarang, hargaBarang, jumlahBeli);
                        penjualan.setTransactionID(transactionID); 

                        return penjualan; 
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error saat mengambil ID Transaksi dari database: " + e.getMessage());
        }
        return null; 
    }

    private static void retrieveTransactionById(int transactionID) {
        Transaksi transaksi = transaksiMap.get(transactionID); 
        if (transaksi != null) { 
            transaksi.tampilkanDetailTransaksi(); 
        } else {
            System.out.println("Transaksi dengan ID " + transactionID + " tidak ditemukan di dalam map.");
        }
    }

}

