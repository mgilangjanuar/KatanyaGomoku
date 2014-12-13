package Gomoku;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Kelas yang merepresentasikan papan permainan yang digunakan dalam permainan
 * Gomoku. Kelas ini akan menggunakan object assets yang merupakan object 2
 * dimensi dari kelas Asset sebagai komponen utama pembentuknya. Dan juga
 * mendefinisikan seluruh panel yang akan digunakan sebagai pembentuk papan
 * nantinya juga akan ditambahkan pada kelas Gomoku sebagai komponen pembentuk
 * utamanya.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */

public class Board {
	private int rows;
	private int cols;
	private JPanel panelAtas;
	private JPanel panelTengah;
	private JPanel panelBawah;
	private JPanel panelKiri;
	private JPanel panelKanan;
	private JLabel status;
	private Asset[][] assets;
	private Color kotak1;
	private Color kotak2;
	private ListenerGomoku listener;

	/**
	 * Konstruktor dari kelas Board yang menerima custom jumlah baris, kolom,
	 * dan jumlah poin sebagai syarat untuk menang. Dan semua instance variabel
	 * didefinisikan dalam konstruktor ini.
	 * 
	 * @param rows
	 *            Mendefinisikan jumlah baris pada papan permainan gomoku.
	 * @param cols
	 *            Mendefinisikan jumlah kolom pada papan permainan gomoku.
	 * @param wincount
	 *            Mendefinisikan jumlah poin untuk syarat pemain dikatakan
	 *            menang.
	 */
	public Board(int rows, int cols, int wincount) {
		this.rows = rows;
		this.cols = cols;
		kotak1 = new Color(211, 211, 211);
		kotak2 = new Color(119, 136, 153);
		panelTengah = new JPanel(new GridLayout(rows, cols));
		panelBawah = new JPanel();
		panelAtas = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelKiri = new JPanel(new GridLayout(3, 1));
		panelKanan = new JPanel(new GridLayout(3, 1));
		status = new JLabel("Permainan dimulai. ");
		assets = new Asset[rows][cols];
		listener = new ListenerGomoku(assets, status, wincount);
		listener.setRows(rows);
		listener.setCols(cols);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				assets[i][j] = new Asset();
				assets[i][j].setName(i + "," + j);

				if ((i + j) % 2 == 0) {
					assets[i][j].setAssetColor(kotak1);
				} else {
					assets[i][j].setAssetColor(kotak2);
				}

				assets[i][j].addActionListener(listener);
				panelTengah.add(assets[i][j]);
			}
		}

		// menambahkan actionlistener tombol giveup
		listener.getGiveup().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(null, (listener
						.getTurn() ? listener.getWhite().getName() : listener
						.getBlack().getName()) + " nyerah?", "Confirm",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					listener.loadMusic();
					listener.setGameover(true);
					listener.getGiveup().setEnabled(false);
					listener.getSwingTimer1().stop();
					listener.getSwingTimer2().stop();

					int menit = listener.getTurn() ? Integer
							.parseInt(listener
									.getTimer1()
									.getText()
									.substring(
											0,
											listener.getTimer1().getText()
													.indexOf(':'))) : Integer
							.parseInt(listener
									.getTimer2()
									.getText()
									.substring(
											0,
											listener.getTimer1().getText()
													.indexOf(':')));
					int detik = listener.getTurn() ? Integer
							.parseInt(listener
									.getTimer1()
									.getText()
									.substring(
											listener.getTimer1().getText()
													.indexOf(':') + 1))
							: Integer.parseInt(listener
									.getTimer2()
									.getText()
									.substring(
											listener.getTimer1().getText()
													.indexOf(':') + 1));

					JOptionPane.showMessageDialog(null, String.format(
							"Selamat %s menang dalam %d menit dan %d detik.",
							listener.getTurn() ? listener.getBlack().getName()
									: listener.getWhite().getName(), menit,
							detik));

				}
			}
		});

		listener.getAssetPlayer1().setAssetColor(Color.CYAN);
		panelAtas.add(listener.getGiveup());
		panelBawah.add(status);
		panelKiri.add(listener.getTimer1());
		panelKiri.add(listener.getAssetPlayer1());
		panelKiri.add(listener.getLabel1());
		panelKanan.add(listener.getTimer2());
		panelKanan.add(listener.getAssetPlayer2());
		panelKanan.add(listener.getLabel2());
	}
	
	/**
	 * Method accessor untuk mendapatkan listener yang telah didefinisikan
	 * sebagai instance variabel.
	 * 
	 * @return ListenerGomoku yang digunakan pada kelas Board.
	 */
	public ListenerGomoku getListener() {
		return listener;
	}

	/**
	 * Method accessor untuk mendapatkan array 2 dimensi dari asset.
	 * 
	 * @return Object array 2 dimensi dari asset.
	 */
	public Asset[][] getAssets() {
		return assets;
	}

	/**
	 * Method accessor untuk mendapatkan Asset dengan posisi tertentu.
	 * 
	 * @param x
	 *            Posisi asset dalam baris.
	 * @param y
	 *            Posisi asset dalam kolom.
	 * @return Object Asset berdasarkan parameter.
	 */
	public Asset getAsset(int x, int y) {
		return assets[x][y];
	}

	/**
	 * Method mutator untuk mengubah object array 2 dimensi dari assets.
	 * 
	 * @param assets
	 *            Object array 2 dimensi baru dari assets
	 */
	public void setAssets(Asset[][] assets) {
		this.assets = assets;
	}

	/**
	 * Method accessor untuk mendapatkan JLabel dari status text.
	 * 
	 * @return Object JLabel dari status.
	 */
	public JLabel getStatus() {
		return status;
	}

	/**
	 * Method mutator untuk mengubah status dengan object JLabel baru.
	 * 
	 * @param status
	 *            Object status baru yang akan menggantikan object status
	 *            sebelumnya yang berbentuk JLabel.
	 */
	public void setStatus(JLabel status) {
		this.status = status;
	}

	/**
	 * Method mutator untuk mengubah isi text dari status.
	 * 
	 * @param status
	 *            String baru untuk mengubah isi text dari text sebelumnya yang
	 *            ada di status.
	 */
	public void setStatus(String status) {
		this.status.setText(status);
	}

	/**
	 * Method accessor untuk mendapatkan object panel tengah yang berupa JPanel.
	 * 
	 * @return Object JPanel dari panel tengah.
	 */
	public JPanel getPanelTengah() {
		return panelTengah;
	}

	/**
	 * Method mutator untuk menggantikan panel tengah dengan panel tengah
	 * lainnya yang masih berupa JPanel.
	 * 
	 * @param panelTengah
	 *            Object panel tengah yang baru dan bertipe JPanel.
	 */
	public void setPanelTengah(JPanel panelTengah) {
		this.panelTengah = panelTengah;
	}

	/**
	 * Method accessor untuk mendapatkan panel bawah yang berupa object JPanel.
	 * 
	 * @return Object panel bawah yang bertipe JPanel.
	 */
	public JPanel getPanelBawah() {
		return panelBawah;
	}

	/**
	 * Method mutator untuk mengubah panel bawah dengan object panel bawah
	 * lainnya yang masih bertipe JPanel.
	 * 
	 * @param panelBawah
	 *            Object panel bawah yang baru yang akan menggantikan object
	 *            panel bawah sebelumnya yang memiliki tipe JPanel.
	 */
	public void setPanelBawah(JPanel panelBawah) {
		this.panelBawah = panelBawah;
	}

	/**
	 * Method accessor untuk mendapatkan panel atas yang bertipe JPanel.
	 * 
	 * @return Object panel atas dalam bentuk JPanel.
	 */
	public JPanel getPanelAtas() {
		return panelAtas;
	}

	/**
	 * Method mutator dari panel atas untuk mengubah panel atas dengan object
	 * JPanel lainnya.
	 * 
	 * @param panelAtas
	 *            Object baru yang akan mendefinisikan object panel atas
	 *            sebelumnya.
	 */
	public void setPanelAtas(JPanel panelAtas) {
		this.panelAtas = panelAtas;
	}

	/**
	 * Method accessor yang mengembalikan object JPanel dari panel kiri.
	 * 
	 * @return Panel kiri yang memiliki tipe JPanel.
	 */
	public JPanel getPanelKiri() {
		return panelKiri;
	}

	/**
	 * Method mutator dari panel kiri untuk mengubah atau menggantikan panel
	 * kiri dengan object JPanel lainnya.
	 * 
	 * @param panelKiri
	 *            Panel kiri baru yang akan mendefinisikan object panel kiri
	 *            sebelumnya yang bertipa JPanel.
	 */
	public void setPanelKiri(JPanel panelKiri) {
		this.panelKiri = panelKiri;
	}

	/**
	 * Method accessor dari panel kanan yang mengembalikan object JPanel dari
	 * panel kanan.
	 * 
	 * @return Object JPanel dari panel kanan.
	 */
	public JPanel getPanelKanan() {
		return panelKanan;
	}

	/**
	 * Method accessor dari panel kanan yang digunakan untuk mengubah object
	 * tersebut dengan object JPanel lainnya.
	 * 
	 * @param panelKanan
	 *            Panel kanan baru yang akan mendefinisikan object panel kanan
	 *            sebelumnya yang memiliki tipe JPanel.
	 */
	public void setPanelKanan(JPanel panelKanan) {
		this.panelKanan = panelKanan;
	}

	/**
	 * Method accessor yang mengembalikan warna dari kotak jenis pertama.
	 * 
	 * @return Kotak jenis pertama yang bertipe Color.
	 */
	public Color getKotak1() {
		return kotak1;
	}

	/**
	 * Method mutator yang mengubah seluruh warna dari array assets yang
	 * memiliki jenis pertama.
	 * 
	 * @param kotak1
	 *            Warna dari kotak jenis pertama yang baru yang akan
	 *            mendefinisikan warna dari kotak jenis pertama sebelumnya.
	 */
	public void setKotak1(Color kotak1) {
		this.kotak1 = kotak1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if ((i + j) % 2 == 0) {
					assets[i][j].setAssetColor(kotak1);
				}
			}
		}
	}

	/**
	 * Method accessor yang mengembalikan warna dari kotak jenis kedua.
	 * 
	 * @return Kotak jenis kedua yang bertipe Color.
	 */
	public Color getKotak2() {
		return kotak2;
	}

	/**
	 * Method mutator yang mengubah seluruh warna dari array assets yang
	 * memiliki jenis kedua.
	 * 
	 * @param kotak2
	 *            Warna dari kotak jenis kedua yang baru yang akan
	 *            mendefinisikan warna dari kotak jenis kedua sebelumnya.
	 */
	public void setKotak2(Color kotak2) {
		this.kotak2 = kotak2;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if ((i + j) % 2 == 1) {
					assets[i][j].setAssetColor(kotak2);
				}
			}
		}
	}

	/**
	 * Method accessor yang akan mengembalikan nilai integer dari banyaknya
	 * baris pada papan permainan.
	 * 
	 * @return Jumlah baris yang ada di papan.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Method mutator yang digunakan untuk mengubah banyaknya baris pada papan.
	 * 
	 * @param rows
	 *            Jumlah baris baru yang akan mendefinisikan jumlah baris
	 *            sebelumnya.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Method accessor yang akan mengembalikan nilai integer dari banyaknya
	 * kolom pada papan permainan.
	 * 
	 * @return Jumlah kolom yang ada di papan.
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Method mutator yang digunakan untuk mengubah banyaknya kolom pada papan.
	 * 
	 * @param cols
	 *            Jumlah baris baru yang akan mendefinisikan jumlah kolom
	 *            sebelumnya.
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}
}
