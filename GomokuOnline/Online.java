package GomokuOnline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Kelas utama yang merepresentasikan sebuah fungsi khusus untuk melakukan
 * koneksi dengan FTP. Disebut kelas utama karena dengan membuat object dari
 * kelas ini berarti telah melakukan komunikasi langsung dengan FTP sehingga
 * permainan bisa dilakukan secara online.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */
public class Online {
	public FTPClient ftp;
	public String log = "";

	/**
	 * Konstruktor default bila parameter untuk object dari kelas Online tidak
	 * didefinisikan saat objek dibuat.
	 */
	public Online() {
		ftp = null;
	}

	/**
	 * Konstruktor dari kelas Online yang mendefinisikan String untuk server,
	 * username, dan password di definisikan dalam parameter saat objek dibuat.
	 * 
	 * @param host
	 *            Lihat method connect().
	 * @param user
	 *            Lihat method connect().
	 * @param pwd
	 *            Lihat method connect().
	 */
	public Online(String host, String user, String pwd) {
		ftp = null;
		conect(host, user, pwd);
	}

	/**
	 * Method utama dalam kelas Online yang bertugas menyambungkan koneksi
	 * dengan FTP untuk memulai melakukan operasi lainnya semisal upload,
	 * getFile, remove, dan lain-lain.
	 * 
	 * @param host
	 *            Sebuah string yang merepresentasikan server host FTP yang
	 *            digunakan misal "ftp.semardev.com".
	 * @param user
	 *            String yang merepresentasikan nama user dari pengguna FTP.
	 * @param pwd
	 *            String yang mendefinisikan password dari user pemilik FTP.
	 */
	public void conect(String host, String user, String pwd) {
		ftp = new FTPClient();
		int reply;
		try {
			ftp.connect(host);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new Exception("Exception in connecting to FTP Server");
			}
			ftp.login(user, pwd);
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
		} catch (Exception e) {
			log = "connection problem and fatal error on connecting server ln 81 "
					+ getClass().getName() + "\n";
		}
	}

	/**
	 * Method khusus yang digunakan untuk melakukan komunikasi dengan FTP dalam
	 * bentuk upload sebuah file.
	 * 
	 * @param localFileFullName
	 *            Sebuah string yang merepresentasikan nama file atau alamat
	 *            dari file tersebut berada dalam komputer client.
	 * @param fileName
	 *            String yang mendefinisikan nama file ketika sudah diupload di
	 *            FTP.
	 * @param hostDir
	 *            String yang mendefinisikan nama directory pada FTP yang akan
	 *            digunakan sebagai directory upload file lokal. Format
	 *            penulisannya menggunakan tanda "/" di akhir nama directory
	 *            misal "project/".
	 */
	public void uploadFile(String localFileFullName, String fileName,
			String hostDir) {
		try (InputStream input = new FileInputStream(
				new File(localFileFullName))) {
			this.ftp.storeFile(hostDir + fileName, input);
		} catch (FileNotFoundException e) {
			log = "file not found on upoad data ln 108 " + getClass().getName()
					+ "\n";
		} catch (IOException e) {
			log = "fatal error on upload data ln 111 " + getClass().getName()
					+ "\n";
		}
	}

	/**
	 * Method khusus yang bertugas untuk memutuskan koneksi dengan FTP.
	 */
	public void disconnect() {
		if (this.ftp.isConnected()) {
			try {
				this.ftp.logout();
				this.ftp.disconnect();
			} catch (IOException e) {
				log = "fatal error on disconnect ln 125 "
						+ getClass().getName() + "\n";
			}
		}
	}

	/**
	 * Method khusus untuk melakukan komunikasi dengan FTP dalam bentuk
	 * mendapatkan file dari FTP tersebut dan mengembalikannya dalam bentuk
	 * InputStreamReader.
	 * 
	 * @param urlFile
	 *            String yang mendefinisikan alamat URL file lengkap dengan
	 *            protokolnya (http://, https://) misal
	 *            "http://semardev.com/file.txt".
	 * @return Sebuah object InputStreamReader yang dapat difungsikan dengan
	 *         BufferedReader atau Scanner dalam penggunaannya.
	 */
	public InputStreamReader getFile(String urlFile) {
		URL url;
		InputStreamReader isr = null;
		try {
			url = new URL(urlFile);
			URLConnection con = url.openConnection();
			isr = new InputStreamReader(con.getInputStream());
		} catch (FileNotFoundException e) {
			log = "error file not found on get file ln 151 "
					+ getClass().getName() + "\n";
		} catch (Exception e) {
			log = "fatal error on get file ln 154 " + getClass().getName()
					+ "\n";
		}finally{
			log="";
		}
		return isr;
	}

	/**
	 * Method khusus untuk menghapus file tertentu dalam FTP.
	 * 
	 * @param fileToDelete
	 *            Nama file yang akan dihapus pada FTP.
	 */
	public void remove(String fileToDelete) {
		boolean deleted;
		try {
			deleted = ftp.deleteFile(fileToDelete);
			if (!deleted) {
				log = "failed remove data ln 171 " + getClass().getName()
						+ "\n";
				throw new IOException();
			}
		} catch (IOException e) {
			log = "fatal error on remove data ln 176 " + getClass().getName()
					+ "\n";
		}
	}

}