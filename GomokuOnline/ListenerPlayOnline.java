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
import javax.swing.JOptionPane;
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
	private String tempData;

	String host = "ftp.lalala.com";
	String user = "gilang";
	String pass = "tanggallahir";
	String path = "http://semardev.com/";

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
		on = new Online(host, user, pass);
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
						InputStreamReader isr = on.getFile(path + "game/"
								+ data);
						Scanner in = new Scanner(isr);
						String dataOnServer = in.nextLine();
						String timer1 = in.nextLine();
						String timer2 = in.nextLine();
						if (dataOnServer.equals("nyerah")) {
							JOptionPane.showMessageDialog(null, "Selamat Anda menang, lawan Anda telah menyerah.");
							System.exit(0);
						} else if (dataOnServer.equals("null")) {
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
							getAssets(i, j).setName(dataOnServer);
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
									setTurn(true);
									status.setText("Giliran "
											+ getWhite().getName() + " jalan.");
									getAssetPlayer1().setAssetColor(null);
									getAssetPlayer2().setAssetColor(Color.CYAN);
									getSwingTimer1().stop();
									getSwingTimer2().start();
									try {
										String data1 = in.nextLine();
										int x1 = Integer.parseInt(data1
												.substring(1,
														data1.indexOf(',')));
										int y1 = Integer.parseInt(data1
												.substring(data1.indexOf(',') + 1));
										String kode = in.nextLine();
										int z = Integer.parseInt(kode
												.substring(1));
										if (kode.charAt(0) == 'h') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1, y1 + k)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1, y1 + k).setClick(
														true);
												getAssets(x1, y1 + k)
														.setPlayerColor(
																getBlack()
																		.getColor());
											}
										} else if (kode.charAt(0) == 'v') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1 + k, y1)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1 + k, y1).setClick(
														true);
												getAssets(x1 + k, y1)
														.setPlayerColor(
																getBlack()
																		.getColor());
											}
										} else if (kode.charAt(0) == 'd') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1 + k, y1 + k)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1 + k, y1 + k)
														.setClick(true);
												getAssets(x1 + k, y1 + k)
														.setPlayerColor(
																getBlack()
																		.getColor());
											}
										} else if (kode.charAt(0) == 'b') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1 + k, y1 - k)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1 + k, y1 - k)
														.setClick(true);
												getAssets(x1 + k, y1 - k)
														.setPlayerColor(
																getBlack()
																		.getColor());
											}
										}
										getSwingTimer1().stop();
										getSwingTimer2().stop();
										setEnable(false);
										getAssetPlayer1().setAssetColor(
												Color.CYAN);
										getAssetPlayer2().setAssetColor(null);
										status.setText("Selamat "
												+ getBlack().getName()
												+ " menang!");
										JOptionPane
												.showMessageDialog(
														null,
														getBlack().getName()
																+ " menang! Yeay! Cie kalah ^^");
										loadMusic();
									} catch (Exception e1) {

									}
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
									setTurn(false);
									status.setText("Giliran "
											+ getBlack().getName() + " jalan.");
									getAssetPlayer1().setAssetColor(Color.CYAN);
									getAssetPlayer2().setAssetColor(null);
									getSwingTimer1().start();
									getSwingTimer2().stop();
									try {
										String data1 = in.nextLine();
										int x1 = Integer.parseInt(data1
												.substring(1,
														data1.indexOf(',')));
										int y1 = Integer.parseInt(data1
												.substring(data1.indexOf(',') + 1));
										String kode = in.nextLine();
										int z = Integer.parseInt(kode
												.substring(1));
										if (kode.charAt(0) == 'h') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1, y1 + k)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1, y1 + k).setClick(
														true);
												getAssets(x1, y1 + k)
														.setPlayerColor(
																getWhite()
																		.getColor());
											}
										} else if (kode.charAt(0) == 'v') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1 + k, y1)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1 + k, y1).setClick(
														true);
												getAssets(x1 + k, y1)
														.setPlayerColor(
																getWhite()
																		.getColor());
											}
										} else if (kode.charAt(0) == 'd') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1 + k, y1 + k)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1 + k, y1 + k)
														.setClick(true);
												getAssets(x1 + k, y1 + k)
														.setPlayerColor(
																getWhite()
																		.getColor());
											}
										} else if (kode.charAt(0) == 'b') {
											for (int k = 0; k < z - 1; k++) {
												getAssets(x1 + k, y1 - k)
														.setAssetColor(
																Color.CYAN);
												getAssets(x1 + k, y1 - k)
														.setClick(true);
												getAssets(x1 + k, y1 - k)
														.setPlayerColor(
																getWhite()
																		.getColor());
											}
										}
										getSwingTimer1().stop();
										getSwingTimer2().stop();
										setEnable(false);
										getAssetPlayer1().setAssetColor(null);
										getAssetPlayer2().setAssetColor(
												Color.CYAN);
										assets[i][j].setClick(true);
										assets[i][j].setPlayerColor(getWhite()
												.getColor());
										status.setText("Selamat "
												+ getWhite().getName()
												+ " menang!");
										JOptionPane
												.showMessageDialog(
														null,
														getWhite().getName()
																+ " menang! Yeay! Cie kalah ^^");
										loadMusic();
									} catch (Exception e1) {

									}
								} else {
									setEnable(false);
								}
							}
							in.close();
						}
					} catch (NullPointerException e1) {
						text.append("null pointer ln : 297 "
								+ getClass().getName() + "\n");
					} catch (NoSuchElementException e1) {
						text.append("no such element ln : 300 "
								+ getClass().getName() + "\n");
					} catch (Exception e1) {
						text.append("fatal error ln : 303 "
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
		if (!gameover) {
			receiveData.stop();
			Asset cell = (Asset) e.getSource();
			cell.setClick(true);
			tempData = cell.getName();
			if (!firstPlayer) {
				cell.setPlayerColor(getWhite().getColor());
				cell.setName("O" + cell.getName());
				getStatus().setText(
						"Giliran " + getBlack().getName() + " jalan.");
				getAssetPlayer1().setAssetColor(Color.CYAN);
				getAssetPlayer2().setAssetColor(null);
				getSwingTimer1().start();
				getSwingTimer2().stop();
			} else {
				cell.setPlayerColor(getBlack().getColor());
				cell.setName("X" + cell.getName());
				getStatus().setText(
						"Giliran " + getWhite().getName() + " jalan.");
				getAssetPlayer1().setAssetColor(null);
				getAssetPlayer2().setAssetColor(Color.CYAN);
				getSwingTimer1().stop();
				getSwingTimer2().start();
			}
			setTurn(!getTurn());
			cell.setEnabled(false);
			cell.repaint();
			text.append((firstPlayer ? getBlack().getName() : getWhite()
					.getName())
					+ " move to "
					+ cell.getName().substring(1)
					+ "\n");
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
				text.append("file not found for data.txt ln : 364 "
						+ getClass().getName() + "\n");
			} catch (NoSuchElementException e1) {
				text.append("no such element ln : 367 " + getClass().getName()
						+ "\n");
			} finally {
				receiveData.start();
			}
			int x = Integer.parseInt(cell.getName().substring(1,
					cell.getName().indexOf(',')));
			int y = Integer.parseInt(cell.getName().substring(
					cell.getName().indexOf(',') + 1));
			cekMenang(x, y, cell.getName().substring(0, 1));
		}
	}

	@Override
	public int hor(int x, int y, String kodePemain) {
		int n = 0;
		int initialY = y;
		int initialY2 = 0;

		while (y >= 0
				&& y < getCols()
				&& getAssets(x, y++).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialY >= 0
				&& initialY < getCols()
				&& getAssets(x, initialY--).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
			initialY2 = initialY + 1;
		}

		if (n > getWincount()) {
			receiveData.stop();
			for (int i = 0; i < n - 1; i++) {
				getAssets(x, initialY2 + i).setAssetColor(Color.CYAN);
			}
			PrintWriter pw;
			try {
				text.append("send data" + "\n");
				pw = new PrintWriter("data.txt");
				pw.println(tempData);
				pw.println(getTimer1().getText());
				pw.println(getTimer2().getText());
				pw.println(getAssets(x, initialY2).getName());
				pw.println("h" + n);
				pw.close();
				on.uploadFile("data.txt", "gomoku"
						+ getBlack().getName().toLowerCase()
						+ getWhite().getName().toLowerCase() + getRows()
						+ getCols() + ".txt", "game/");

			} catch (FileNotFoundException e1) {
				text.append("file not found for data.txt ln : 421 "
						+ getClass().getName() + "\n");
			} catch (NoSuchElementException e1) {
				text.append("no such element ln : 424 " + getClass().getName()
						+ "\n");
			}
		}

		return n;
	}

	@Override
	public int ver(int x, int y, String kodePemain) {
		int n = 0;
		int initialX = x;
		int initialX2 = 0;
		while (x >= 0
				&& x < getRows()
				&& getAssets(x++, y).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialX >= 0
				&& initialX < getRows()
				&& getAssets(initialX--, y).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
			initialX2 = initialX + 1;
		}

		if (n > getWincount()) {
			receiveData.stop();
			for (int i = 0; i < n - 1; i++) {
				getAssets(initialX2 + i, y).setAssetColor(Color.CYAN);
			}
			PrintWriter pw;
			try {
				text.append("send data" + "\n");
				pw = new PrintWriter("data.txt");
				pw.println(tempData);
				pw.println(getTimer1().getText());
				pw.println(getTimer2().getText());
				pw.println(getAssets(initialX2, y).getName());
				pw.println("v" + n);
				pw.close();
				on.uploadFile("data.txt", "gomoku"
						+ getBlack().getName().toLowerCase()
						+ getWhite().getName().toLowerCase() + getRows()
						+ getCols() + ".txt", "game/");

			} catch (FileNotFoundException e1) {
				text.append("file not found for data.txt ln : 472 "
						+ getClass().getName() + "\n");
			} catch (NoSuchElementException e1) {
				text.append("no such element ln : 475 " + getClass().getName()
						+ "\n");
			}
		}

		return n;
	}

	@Override
	public int dia1(int x, int y, String kodePemain) {
		int n = 0;
		int initialX = x;
		int initialY = y;
		int initialX2 = 0;
		int initialY2 = 0;
		while (y >= 0
				&& y < getCols()
				&& x >= 0
				&& x < getRows()
				&& getAssets(x++, y++).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialY >= 0
				&& initialY < getCols()
				&& initialX >= 0
				&& initialX < getRows()
				&& getAssets(initialX--, initialY--).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
			initialX2 = initialX + 1;
			initialY2 = initialY + 1;
		}

		if (n > getWincount()) {
			receiveData.stop();
			for (int i = 0; i < n - 1; i++) {
				getAssets(initialX2 + i, initialY2 + i).setAssetColor(
						Color.CYAN);
			}
			PrintWriter pw;
			try {
				text.append("send data" + "\n");
				pw = new PrintWriter("data.txt");
				pw.println(tempData);
				pw.println(getTimer1().getText());
				pw.println(getTimer2().getText());
				pw.println(getAssets(initialX2, initialY2).getName());
				pw.println("d" + n);
				pw.close();
				on.uploadFile("data.txt", "gomoku"
						+ getBlack().getName().toLowerCase()
						+ getWhite().getName().toLowerCase() + getRows()
						+ getCols() + ".txt", "game/");

			} catch (FileNotFoundException e1) {
				text.append("file not found for data.txt ln : 531 "
						+ getClass().getName() + "\n");
			} catch (NoSuchElementException e1) {
				text.append("no such element ln : 534 " + getClass().getName()
						+ "\n");
			}
		}

		return n;
	}

	@Override
	public int dia2(int x, int y, String kodePemain) {
		int n = 0;
		int initialX = x;
		int initialY = y;
		int initialX2 = 0;
		int initialY2 = 0;
		while (y >= 0
				&& y < getCols()
				&& x >= 0
				&& x < getRows()
				&& getAssets(x++, y--).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
		}
		while (initialY >= 0
				&& initialY < getCols()
				&& initialX >= 0
				&& initialX < getRows()
				&& getAssets(initialX--, initialY++).getName().substring(0, 1)
						.equals(kodePemain)) {
			++n;
			initialX2 = initialX + 1;
			initialY2 = initialY - 1;
		}

		if (n > getWincount()) {
			receiveData.stop();
			for (int i = 0; i < n - 1; i++) {
				getAssets(initialX2 + i, initialY2 - i).setAssetColor(
						Color.CYAN);
			}
			PrintWriter pw;
			try {
				text.append("send data" + "\n");
				pw = new PrintWriter("data.txt");
				pw.println(tempData);
				pw.println(getTimer1().getText());
				pw.println(getTimer2().getText());
				pw.println(getAssets(initialX2, initialY2).getName());
				pw.println("b" + n);
				pw.close();
				on.uploadFile("data.txt", "gomoku"
						+ getBlack().getName().toLowerCase()
						+ getWhite().getName().toLowerCase() + getRows()
						+ getCols() + ".txt", "game/");

			} catch (FileNotFoundException e1) {
				text.append("file not found for data.txt ln : 590 "
						+ getClass().getName() + "\n");
			} catch (NoSuchElementException e1) {
				text.append("no such element ln : 593 " + getClass().getName()
						+ "\n");
			}
		}

		return n;
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
	 * Method khusus yang berefungsi untuk menonaktifkan atau meaktifkan seluruh
	 * tombol untuk menangani masalah pemain pertama yang dapat menjalankan turn
	 * pemain kedua atau sebaliknya.
	 * 
	 * @param status
	 *            Nilai booelan yang mendefinisikan enable/disable object
	 *            button.
	 */
	public void setEnable(boolean status) {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				if (getAssets(i, j).getName().charAt(0) != 'X'
						&& getAssets(i, j).getName().charAt(0) != 'O') {
					getAssets(i, j).setEnabled(status);
				} else {
					getAssets(i, j).setEnabled(false);
				}
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
