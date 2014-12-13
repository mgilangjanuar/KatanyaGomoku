package Gomoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * Kelas yang merepresentasikan sebuah aksi dalam permainan gomoku yang
 * digunakan pada papan permainannnya. Pada kelas ini juga merupakan salah satu
 * kelas terpenting dalam permainan gomoku karena setiap kontrol permainan ada
 * di kelas ini.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */

public class ListenerGomoku implements ActionListener {
	private int rows;
	private int cols;
	private int wincount;
	private boolean turn;
	public boolean gameover;
	private Asset[][] assets;
	private Player white;
	private Player black;
	private JLabel status;
	private JLabel timer1;
	private JLabel timer2;
	private Timer swingTimer1;
	private Timer swingTimer2;
	private TimerGomoku listenerTimer1;
	private TimerGomoku listenerTimer2;
	private JButton giveup;
	private Asset assetPlayer1;
	private Asset assetPlayer2;
	private JTextField labelPlayer1;
	private JTextField labelPlayer2;
	private PlayMusic music;
	public JTextArea text;

	/**
	 * Konstruktor default dari kelas ListenerGomoku yang akan mendefinisikan
	 * setiap instance variabel dari kelas ini. Dan harus memberikan parameter
	 * object array 2 dimensi dari kelas Asset, object JLabel, dan nilai integer
	 * point untuk menang.
	 * 
	 * @param assets
	 *            Object array 2 dimensi dari kelas Asset.
	 * @param status
	 *            Object JLabel yang akan terus diubah isi textnya berdasarkan
	 *            keadaan tertentu.
	 * @param wincount
	 *            Jumlah poin untuk syarat pemenang dalam permainan.
	 */
	public ListenerGomoku(Asset[][] assets, JLabel status, int wincount) {
		this.assets = assets;
		this.status = status;
		this.wincount = wincount;
		text = new JTextArea();
		text.setEditable(false);
		turn = false;
		white = new Player(Color.WHITE, "putih");
		black = new Player(Color.BLACK, "hitam");

		timer1 = new JLabel("00:00", JLabel.CENTER);
		timer2 = new JLabel("00:00", JLabel.CENTER);
		timer1.setFont(new Font(Font.MONOSPACED, 0, 36));
		timer2.setFont(new Font(Font.MONOSPACED, 0, 36));
		listenerTimer1 = new TimerGomoku(timer1);
		listenerTimer2 = new TimerGomoku(timer2);
		swingTimer1 = new Timer(1000, listenerTimer1);
		swingTimer2 = new Timer(1000, listenerTimer2);
		giveup = new JButton("Nyerah");
		assetPlayer1 = new Asset();
		assetPlayer2 = new Asset();
		labelPlayer1 = new JTextField(black.getName(), 12);
		labelPlayer2 = new JTextField(white.getName(), 12);
		labelPlayer1.setHorizontalAlignment(JTextField.CENTER);
		labelPlayer2.setHorizontalAlignment(JTextField.CENTER);
		labelPlayer1.setBackground(null);
		labelPlayer2.setBackground(null);
		labelPlayer1.setBorder(null);
		labelPlayer2.setBorder(null);
		labelPlayer1.setEditable(false);
		labelPlayer2.setEditable(false);

		assetPlayer1.setClick(true);
		assetPlayer1.setEnabled(false);
		assetPlayer1.setPlayerColor(getBlack().getColor());

		assetPlayer2.setClick(true);
		assetPlayer2.setEnabled(false);
		assetPlayer2.setPlayerColor(getWhite().getColor());

		swingTimer1.start();

	}

	/**
	 * Method override dari kelas java.awt.event.ActionListener yang
	 * merepresentasikan ketika asset di klik.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameover) {

			Asset cell = (Asset) e.getSource();
			cell.setClick(true);

			if (turn) {
				cell.setPlayerColor(white.getColor());
				cell.setName("O" + cell.getName());
				status.setText("Giliran " + black.getName() + " jalan.");
				assetPlayer1.setAssetColor(Color.CYAN);
				assetPlayer2.setAssetColor(null);
				swingTimer1.start();
				swingTimer2.stop();
			} else {
				cell.setPlayerColor(black.getColor());
				cell.setName("X" + cell.getName());
				status.setText("Giliran " + white.getName() + " jalan.");
				assetPlayer1.setAssetColor(null);
				assetPlayer2.setAssetColor(Color.CYAN);
				swingTimer1.stop();
				swingTimer2.start();
			}

			int x = Integer.parseInt(cell.getName().substring(1,
					cell.getName().indexOf(',')));
			int y = Integer.parseInt(cell.getName().substring(
					cell.getName().indexOf(',') + 1));

			cekMenang(x, y, turn ? "O" : "X");
			cell.setEnabled(false);
			cell.repaint();
			text.append((!turn ? black.getName() : white.getName())
					+ " move to " + cell.getName().substring(1)+ "\n");
			turn = !turn;
		}
	}

	/**
	 * Pengecekan pemenang secara garis horizontal.
	 * 
	 * @param x
	 *            Posisi koordinat asset dalam baris.
	 * @param y
	 *            Posisi koordinat asset dalam kolom.
	 * @param kodePemain
	 *            Menunjukkan pemain mana yang sedang di cek menang dalam
	 *            keadaan horizontal.
	 * @return Nilai integer yang merepresentasikan jumlah pemain yang berjajar
	 *         dalam garis horizontal.
	 */
	public int hor(int x, int y, String kodePemain) {
		int n = 0;
		int initialY = y;
		int initialY2 = 0;

		while (y >= 0
				&& y < cols
				&& this.assets[x][y++].getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialY >= 0
				&& initialY < cols
				&& this.assets[x][initialY--].getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
			initialY2 = initialY + 1;
		}

		if (n > wincount) {
			for (int i = 0; i < n - 1; i++) {
				assets[x][initialY2 + i].setAssetColor(Color.CYAN);
			}
		}

		return n;
	}

	/**
	 * Pengecekan pemenang secara garis vertikal.
	 * 
	 * @param x
	 *            Posisi koordinat asset dalam baris.
	 * @param y
	 *            Posisi koordinat asset dalam kolom.
	 * @param kodePemain
	 *            Menunjukkan pemain mana yang sedang di cek menang dalam
	 *            keadaan vertikal.
	 * @return Nilai integer yang merepresentasikan jumlah pemain yang berjajar
	 *         dalam garis vertikal.
	 */
	public int ver(int x, int y, String kodePemain) {
		int n = 0;
		int initialX = x;
		int initialX2 = 0;
		while (x >= 0
				&& x < rows
				&& this.assets[x++][y].getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialX >= 0
				&& initialX < rows
				&& this.assets[initialX--][y].getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
			initialX2 = initialX + 1;
		}

		if (n > wincount) {
			for (int i = 0; i < n - 1; i++) {
				assets[initialX2 + i][y].setAssetColor(Color.CYAN);
			}
		}

		return n;
	}

	/**
	 * Pengecekan pemenang secara garis diagonal kiri atas ke kanan bawah.
	 * 
	 * @param x
	 *            Posisi koordinat asset dalam baris.
	 * @param y
	 *            Posisi koordinat asset dalam kolom.
	 * @param kodePemain
	 *            Menunjukkan pemain mana yang sedang di cek menang dalam
	 *            keadaan diagonal jenis pertama.
	 * @return Nilai integer yang merepresentasikan jumlah pemain yang berjajar
	 *         dalam garis diagonal jenis kedua.
	 */
	public int dia1(int x, int y, String kodePemain) {
		int n = 0;
		int initialX = x;
		int initialY = y;
		int initialX2 = 0;
		int initialY2 = 0;
		while (y >= 0
				&& y < cols
				&& x >= 0
				&& x < rows
				&& this.assets[x++][y++].getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialY >= 0
				&& initialY < cols
				&& initialX >= 0
				&& initialX < rows
				&& this.assets[initialX--][initialY--].getName()
						.substring(0, 1).equals(kodePemain)) {
			++n;
			initialX2 = initialX + 1;
			initialY2 = initialY + 1;
		}

		if (n > wincount) {
			for (int i = 0; i < n - 1; i++) {
				assets[initialX2 + i][initialY2 + i].setAssetColor(Color.CYAN);
			}
		}

		return n;
	}

	/**
	 * Pengecekan pemenang secara garis diagonal kiri bawah ke kanan atas.
	 * 
	 * @param x
	 *            Posisi koordinat asset dalam baris.
	 * @param y
	 *            Posisi koordinat asset dalam kolom.
	 * @param kodePemain
	 *            Menunjukkan pemain mana yang sedang di cek menang dalam
	 *            keadaan diagonal jenis kedua.
	 * @return Nilai integer yang merepresentasikan jumlah pemain yang berjajar
	 *         dalam garis diagonal jenis kedua.
	 */
	public int dia2(int x, int y, String kodePemain) {
		int n = 0;
		int initialX = x;
		int initialY = y;
		int initialX2 = 0;
		int initialY2 = 0;
		while (y >= 0
				&& y < cols
				&& x >= 0
				&& x < rows
				&& this.assets[x++][y--].getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialY >= 0
				&& initialY < cols
				&& initialX >= 0
				&& initialX < rows
				&& this.assets[initialX--][initialY++].getName()
						.substring(0, 1).equals(kodePemain)) {
			++n;
			initialX2 = initialX + 1;
			initialY2 = initialY - 1;
		}

		if (n > wincount) {
			for (int i = 0; i < n - 1; i++) {
				assets[initialX2 + i][initialY2 - i].setAssetColor(Color.CYAN);
			}
		}

		return n;
	}

	/**
	 * Pengecekan pemenang secara keseluruhan yang menggabungkan seluruh method
	 * cek menang secara horizontal, vertikal, dan diagonal.
	 * 
	 * @param x
	 *            Posisi koordinat asset dalam baris.
	 * @param y
	 *            Posisi koordinat asset dalam kolom.
	 * @param kodePemain
	 *            Menunjukkan pemain mana yang sedang di cek menang.
	 */
	public void cekMenang(int x, int y, String kodePemain) {
		if (hor(x, y, kodePemain) > wincount
				|| ver(x, y, kodePemain) > wincount
				|| dia1(x, y, kodePemain) > wincount
				|| dia2(x, y, kodePemain) > wincount) {
			gameover = true;
			giveup.setEnabled(false);
			swingTimer1.stop();
			swingTimer2.stop();

			if (kodePemain.equals("X")) {
				assetPlayer1.setAssetColor(Color.CYAN);
				assetPlayer2.setAssetColor(null);
			} else {
				assetPlayer1.setAssetColor(null);
				assetPlayer2.setAssetColor(Color.CYAN);
			}

			status.setText("Selamat "
					+ (kodePemain.equals("X") ? getBlack().getName()
							: getWhite().getName()) + " menang!");

			int menit = kodePemain.equals("X") ? Integer.parseInt(timer1
					.getText().substring(0, timer1.getText().indexOf(':')))
					: Integer.parseInt(timer2.getText().substring(0,
							timer1.getText().indexOf(':')));
			int detik = kodePemain.equals("X") ? Integer.parseInt(timer1
					.getText().substring(timer1.getText().indexOf(':') + 1))
					: Integer.parseInt(timer2.getText().substring(
							timer1.getText().indexOf(':') + 1));

			JOptionPane.showMessageDialog(null, String.format(
					"Selamat %s menang dalam %d menit dan %d detik",
					(kodePemain.equals("X") ? getBlack().getName() : getWhite()
							.getName()), menit, detik));
			loadMusic();

		}
	}

	/**
	 * Method khusus untuk memutarkan musik ketika permainan selesai atau nilai
	 * dari gameover adalah true.
	 */
	public void loadMusic() {
		music = new PlayMusic("champions.wav");
		music.play();
	}

	/**
	 * Method accessor yang bertugas memberikan object Player dari pemain white
	 * (representasi dari pemain kedua).
	 * 
	 * @return Object white dari kelas Player yang sudah didefinisikan dalam
	 *         instance variabel.
	 */
	public Player getWhite() {
		return white;
	}

	/**
	 * Method mutator yang akan menggantikan object white dengan object lain
	 * yang sama dari kelas Player.
	 * 
	 * @param white
	 *            Object dari kelas Player yang akan mendefinisikan object white
	 *            sebelumnya.
	 */
	public void setWhite(Player white) {
		this.white = white;
	}

	/**
	 * Method accessor yang bertugas memberikan object Player dari pemain hitam
	 * (representasi dari pemain pertama).
	 * 
	 * @return Object black dari kelas Player yang sudah didefinisikan dalam
	 *         instance variabel.
	 */
	public Player getBlack() {
		return black;
	}

	/**
	 * Method mutator yang akan menggantikan object black dengan object lain
	 * yang sama dari kelas Player.
	 * 
	 * @param black
	 *            Object dari kelas Player yang akan mendefinisikan object black
	 *            sebelumnya.
	 */
	public void setBlack(Player black) {
		this.black = black;
	}

	/**
	 * Method accessor untuk mengetahui keadaan nilai dari turn yang ada di
	 * instance variable yang telah didefinisikan.
	 * 
	 * @return nilai boolean dari variabel turn.
	 */
	public boolean getTurn() {
		return turn;
	}

	/**
	 * Method mutator untuk mengubah nilai dari variabel turn yang bernilai
	 * boolean.
	 * 
	 * @param turn
	 *            Nilai boolean baru yang akan menggantikan nilai dari turn
	 *            sebelumnya.
	 */
	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	/**
	 * Method accessor untuk mengembalikan object JLabel dari timer1 yang
	 * merepresentasikan waktu permainannya pemain 1.
	 * 
	 * @return Object JLabel dari variabel timer1.
	 */
	public JLabel getTimer1() {
		return timer1;
	}

	/**
	 * Method mutator yang bertujuan untuk mengubah object JLabel dari timer1
	 * dengan object lainnya yang masih memiliki tipe object yang sama yaitu
	 * dari kelas JLabel.
	 * 
	 * @param timer1
	 *            Object JLabel baru yang akan mendefinisikan ulang variabel
	 *            timer1.
	 */
	public void setTimer1(JLabel timer1) {
		this.timer1 = timer1;
	}

	/**
	 * Method accessor untuk mengembalikan object JLabel dari timer2 yang
	 * merepresentasikan waktu permainannya pemain 2.
	 * 
	 * @return Object JLabel dari variabel timer2.
	 */
	public JLabel getTimer2() {
		return timer2;
	}

	/**
	 * Method mutator yang bertujuan untuk mengubah object JLabel dari timer2
	 * dengan object lainnya yang masih memiliki tipe object yang sama yaitu
	 * dari kelas JLabel.
	 * 
	 * @param timer2
	 *            Object JLabel baru yang akan mendefinisikan ulang variabel
	 *            timer2.
	 */
	public void setTimer2(JLabel timer2) {
		this.timer2 = timer2;
	}

	/**
	 * Method accessor yang akan mengambil atau mengembalikan object Timer dari
	 * kelas javax.swing.Timer
	 * 
	 * @return Object Timer dari swingTimer1.
	 */
	public Timer getSwingTimer1() {
		return swingTimer1;
	}

	/**
	 * Method mutator yang akan mengubah object swingTimer1 dengan object
	 * lainnya dari kelas javax.swing.Timer
	 * 
	 * @param swingTimer1
	 *            Object Timer baru yang akan mendefinisikan ulang variable
	 *            swingTimer1.
	 */
	public void setSwingTimer1(Timer swingTimer1) {
		this.swingTimer1 = swingTimer1;
	}

	/**
	 * Method accessor yang akan mengambil atau mengembalikan object Timer dari
	 * kelas javax.swing.Timer
	 * 
	 * @return Object Timer dari swingTimer2.
	 */
	public Timer getSwingTimer2() {
		return swingTimer2;
	}

	/**
	 * Method mutator yang akan mengubah object swingTimer2 dengan object
	 * lainnya dari kelas javax.swing.Timer
	 * 
	 * @param swingTimer1
	 *            Object Timer baru yang akan mendefinisikan ulang variable
	 *            swingTimer2.
	 */
	public void setSwingTimer2(Timer swingTimer1) {
		this.swingTimer1 = swingTimer1;
	}

	/**
	 * Method accessor untuk mendapatkan object Asset dari cellPemain1.
	 * 
	 * @return Object dari kelas Asset pada cellPemain1.
	 */
	public Asset getAssetPlayer1() {
		return assetPlayer1;
	}

	/**
	 * Method accessor untuk mendapatkan object Asset dari cellPemain2.
	 * 
	 * @return Object dari kelas Asset pada cellPemain2.
	 */
	public Asset getAssetPlayer2() {
		return assetPlayer2;
	}

	/**
	 * Method accessor untuk mendapatkan object JTextField dari label1 yang
	 * merepresentasikan nama pemain pertama pada panel kiri.
	 * 
	 * @return Object label1 yang bertipe JTextField.
	 */
	public JTextField getLabel1() {
		return labelPlayer1;
	}

	/**
	 * Method accessor untuk mendapatkan object JTextField dari label2 yang
	 * merepresentasikan nama pemain kedua pada panel kanan.
	 * 
	 * @return Object label2 yang bertipe JTextField.
	 */
	public JTextField getLabel2() {
		return labelPlayer2;
	}

	/**
	 * Method accessor untuk mendapatkan banyaknya poin untuk syarat menang.
	 * 
	 * @return Nilai integer yang merepresentasikan jumlah poin menang.
	 */
	public int getWincount() {
		return wincount;
	}

	/**
	 * Method mutator untuk merubah nilai poin yang merepresentasikan poin
	 * syarat untuk menang.
	 * 
	 * @param wincount
	 *            Nilai integer baru yang akan mendefinisikan nilai wincount
	 *            yang sebelumnya.
	 */
	public void setWincount(int wincount) {
		this.wincount = wincount;
	}

	/**
	 * Method accessor untuk mendapatkan nilai dari jumlah baris pada papan
	 * permainan.
	 * 
	 * @return Nilai integer yang merepresentasikan jumlah dari baris pada
	 *         papan.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Method mutator untuk merubah nilai dari rows yang merepresentasikan
	 * banyaknya baris pada papan permainan.
	 * 
	 * @param rows
	 *            Nilai integer baru yang akan mendefinisikan nilai rows
	 *            sebelumnya.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Method accessor untuk mendapatkan nilai dari jumlah kolom pada papan
	 * permainan.
	 * 
	 * @return Nilai integer yang merepresentasikan jumlah dari kolom pada
	 *         papan.
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Method mutator untuk merubah nilai dari cols yang merepresentasikan
	 * banyaknya kolom pada papan permainan.
	 * 
	 * @param cols
	 *            Nilai integer baru yang akan mendefinisikan nilai cols
	 *            sebelumnya.
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}

	/**
	 * Method accessor yang akan mengembalikan nilai boolean dari gameover.
	 * 
	 * @return Nilai boolean (T/F) dari gameover yang merepresentasikan bahwa
	 *         permainan belum/telah berakhir.
	 */
	public boolean isGameover() {
		return gameover;
	}

	/**
	 * Method mutator untuk merubah nilai dari boolean gameover.
	 * 
	 * @param gameover
	 *            Nilai boolean baru yang nantinya akan mendefinisikan nilai
	 *            boolean dari gameover sebelumnya.
	 */
	public void setGameover(boolean gameover) {
		this.gameover = gameover;
	}

	/**
	 * Method accessor yang mengembalikan sebuah object JButton dari tombol
	 * giveup yang merepestasikan pemain akan menyerah.
	 * 
	 * @return Object JButton dari tombol giveup.
	 */
	public JButton getGiveup() {
		return giveup;
	}

	/**
	 * Method accessor dari assets yang mengembalikan sebuah object Asset
	 * berdasarkan lokasi baris dan kolomnya.
	 * 
	 * @param x
	 *            Lokasi Asset dalam assets yang berada pada index baris.
	 * @param y
	 *            Lokasi Asset dalam assets yang berada pada index kolom.
	 * @return Object Asset yang berada pada assets sesuai posisi yang
	 *         didefinisikan dalam parameter.
	 */
	public Asset getAssets(int x, int y) {
		return assets[x][y];
	}

}
