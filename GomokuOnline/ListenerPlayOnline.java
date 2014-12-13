package GomokuOnline;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.Timer;

import Gomoku.Asset;
import Gomoku.ListenerGomoku;

/**
 * Kelas yang merepresentasikan aksi dari permainan gomoku yang dimainkan secara
 * online. Kelas ini juga merupakan turunan dari kelas ListenerGomoku yang hanya
 * menambahkan fitur-fitur tertentu saja untuk dapat mengoneksikanya ke FTP.
 * Data ditransfer dalam format text ke FTP dan dibuat runtime agar permainan
 * gomoku pada komputer client mendapatkan update mengenai data game dari
 * komputer pemain lawan.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 *
 */
public class ListenerPlayOnline extends ListenerGomoku {

	private Online on;
	private Timer receiveData;
	private boolean firstPlayer;

	/*
		Ubah nilai variabel ini
	*/
	String hostname = "ftp.senardev.com"; // mendefinisikan FTP host
	String username = "admin"; // mendefinisikan username akun FTP
	String password = "tanggallahir"; // mendefinisikan password FTP
	String urlToGetFile = "http://semardev.com/game/"; // mendefinisikan alamat URL tempat upload data

	/**
	 * Konstruktor default dari kelas ListenerPlayOnline yang mendefinisikan
	 * private instance variable sekaligus mengoneksikannya dengan akun FTP.
	 * 
	 * @param assets
	 *            Object array 2 dimensi dari kelas Asset.
	 * @param status
	 *            Object JLabel yang akan terus diubah isi textnya berdasarkan
	 *            keadaan tertentu.
	 * @param wincount
	 *            Jumlah poin untuk syarat pemenang dalam permainan.
	 */
	public ListenerPlayOnline(Asset[][] assets, JLabel status, int wincount) {
		super(assets, status, wincount);
		text.append("connecting...\n");
		on = new Online(hostname, username, password);
		receiveData = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Timer localTimer = (Timer) e.getSource();
				if (!gameover) {
					try {
						text.append(on.log);
						String data = "";
						data = "gomoku" + getBlack().getName().toLowerCase()
								+ getWhite().getName().toLowerCase()
								+ getRows() + getCols() + ".txt";
						text.append("update data" + "\n");
						InputStreamReader isr = on
								.getFile(urlToGetFile
										+ data);
						Scanner in = new Scanner(isr);
						String dataOnServer = in.nextLine();
						String timer1 = in.nextLine();
						String timer2 = in.nextLine();
						if (dataOnServer.equals("null")) {
							if (firstPlayer) {
								localTimer.stop();
							} else {
								localTimer.start();
								setEnable(false);
							}
						} else {
							int i = Integer.parseInt(dataOnServer.substring(1,
									dataOnServer.indexOf(',')));
							int j = Integer.parseInt(dataOnServer.substring(
									dataOnServer.indexOf(',') + 1).trim());

							if (dataOnServer.charAt(0) == 'X') {
								if (!firstPlayer) {
									localTimer.stop();
									setEnable(true);
									text.append("get data from "
											+ getWhite().getName() + " in " + i
											+ "," + j + "\n");
									getTimer1().setText(timer1);
									getTimer2().setText(timer2);

									assets[i][j].setClick(true);
									assets[i][j].setPlayerColor(getBlack()
											.getColor());
									cekMenang(i, j, "X");

									setTurn(true);
									status.setText("Giliran "
											+ getWhite().getName() + " jalan.");
									getAssetPlayer1().setAssetColor(null);
									getAssetPlayer2().setAssetColor(Color.CYAN);
									getSwingTimer1().stop();
									getSwingTimer2().start();
								} else {
									setEnable(false);
								}

							} else if (dataOnServer.charAt(0) == 'O') {
								if (firstPlayer) {
									localTimer.stop();
									setEnable(true);
									text.append("get data from "
											+ getWhite().getName() + " in " + i
											+ "," + j + "\n");
									getTimer1().setText(timer1);
									getTimer2().setText(timer2);

									assets[i][j].setClick(true);
									assets[i][j].setPlayerColor(getWhite()
											.getColor());
									cekMenang(i, j, "O");

									setTurn(false);
									status.setText("Giliran "
											+ getBlack().getName() + " jalan.");
									getAssetPlayer1().setAssetColor(Color.CYAN);
									getAssetPlayer2().setAssetColor(null);
									getSwingTimer1().start();
									getSwingTimer2().stop();
								} else {
									setEnable(false);
								}

							}

							assets[i][j].setName(dataOnServer);
							in.close();
						}
					} catch (NullPointerException e1) {
						text.append("null pointer ln : 142 "
								+ getClass().getName() + "\n");
					} catch (NoSuchElementException e1) {
						text.append("no such element ln : 145 "
								+ getClass().getName() + "\n");
					} catch (Exception e1) {
						text.append("fatal error ln : 148 "
								+ getClass().getName() + "\n");
					}
				} else {
					localTimer.stop();
				}
			}
		});
	}

	/**
	 * Method override dari kelas java.awt.event.ActionListener yang
	 * merepresentasikan ketika asset di klik.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Asset cell = (Asset) e.getSource();
		receiveData.stop();
		PrintWriter pw;
		try {
			text.append("send data" + "\n");
			pw = new PrintWriter("data.txt");
			pw.println(cell.getName());
			pw.println(getTimer1().getText());
			pw.println(getTimer2().getText());
			pw.close();
			on.uploadFile("data.txt", "gomoku"
					+ getBlack().getName().toLowerCase()
					+ getWhite().getName().toLowerCase() + getRows()
					+ getCols() + ".txt", "game/");

		} catch (FileNotFoundException e1) {
			text.append("file not found for data.txt ln : 181 "
					+ getClass().getName() + "\n");
		} catch (NoSuchElementException e1) {
			text.append("no such element ln : 183 " + getClass().getName()
					+ "\n");
		} finally {
			receiveData.start();
		}
	}

	/**
	 * Method accessor yang mengembalikan object Timer dari kelas
	 * javax.swing.Timer yang telan mendefinisikan receiveData pada kelas ini.
	 * 
	 * @return Object Timer yang sebelumnya telah didefinisikan dalam variabel
	 *         receiveData.
	 */
	public Timer getReceiveData() {
		return receiveData;
	}

	/**
	 * Method accessor yang mengembalikan object Online dari package
	 * GomokuOnline yang telah didefinisikan sebelumnya dalam variable on.
	 * 
	 * @return Object on yang memiliki tipe object dari kelas Online.
	 */
	public Online getOn() {
		return on;
	}

	/**
	 * Method khusus yang berefungsi untuk menonaktifkan atau meaktifkan
	 * seluruh tombol untuk menangani masalah pemain pertama yang dapat
	 * menjalankan turn pemain kedua atau sebaliknya.
	 * 
	 * @param status
	 *            Nilai booelan yang mendefinisikan enable/disable object
	 *            button.
	 */
	public void setEnable(boolean status) {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				getAssets(i, j).setEnabled(status);
			}
		}
		getGiveup().setEnabled(status);
	}

	/**
	 * Method accessor untuk mengetahui apakan pemain adalah pemain pertama atau
	 * bukan. Akan mengembalikan nilai true bila pemain adalah pemain pertama
	 * dan false bila bukan pemain pertama.
	 * 
	 * @return Nilai boolean dari variabel firstPlayer yang telah didefinisikan
	 *         sebelumnya.
	 */
	public boolean isFirstPlayer() {
		return firstPlayer;
	}

	/**
	 * Method mutator yang akan mengubah nilai dari firstPlayer dengan nilai
	 * boolean.
	 * 
	 * @param firstPlayer
	 *            Nilai boolean baru yang akan mendefinisikan nilai dari
	 *            variabel firstPlayer sebelumnya.
	 */
	public void setFirstPlayer(boolean firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

}
