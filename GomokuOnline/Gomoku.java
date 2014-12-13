package GomokuOnline;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.commons.net.ftp.FTPFile;

/**
 * Kelas utama yang merepresentasikan permainan Gomoku atau catur jawa secara
 * online. Kelas ini merupakan modifikasi dari kelas Gomoku di package Gomoku.
 * Tujuan modifikasi adalah untuk memfungsikan semua fungsi untuk online untuk
 * dapat dimainkan dengan multiplayer dengan koneksi internet.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */

public class Gomoku extends JFrame {
	private Board board;
	private String pemain1;
	private String pemain2;

	/**
	 * Konstruktor default dari kelas Gomoku yang akan mendefinisikan semua
	 * instance variabelnya.
	 * 
	 * @param rows
	 *            Jumlah baris yang akan dibuat pada papan permainan.
	 * @param cols
	 *            Jumlah kolom yang akan dibuat pada papan permainan.
	 * @param wincount
	 *            Jumlah poin untuk syarat menangnya permainan.
	 */
	public Gomoku(int rows, int cols, int wincount) {
		pemain1 = "";
		pemain2 = "";
		board = new Board(rows, cols, wincount);
		add(board.getPanelAtas(), BorderLayout.NORTH);
		add(board.getPanelTengah(), BorderLayout.CENTER);
		add(board.getPanelBawah(), BorderLayout.SOUTH);
		add(board.getPanelKiri(), BorderLayout.WEST);
		add(board.getPanelKanan(), BorderLayout.EAST);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				board.getListener()
						.getOn()
						.remove("game/gomoku"
								+ board.getListener().getBlack().getName()
										.toLowerCase()
								+ board.getListener().getWhite().getName()
										.toLowerCase() + board.getRows()
								+ board.getCols() + ".txt");
				board.getListener().getOn().disconnect();
				try {
					PrintWriter pw = new PrintWriter("log file.txt");
					pw.println(new Timestamp(new Date().getTime()));
					pw.print(board.getListener().text.getText());
					pw.close();
				} catch (FileNotFoundException e1) {
					board.getListener().text
							.append("file not found exception ln : 95 "
									+ getClass().getName() + "\n");
				}
			}
		});
	}

	/**
	 * Method khusus yang akan mendefinisikan nama pemain bila pada permainan
	 * sebelumnya atau pada permainan yang baru launch sehingga belum memiliki
	 * nama.
	 */
	public void setPlayer() {
		pemain1 = JOptionPane.showInputDialog("Masukkan nama pemain1");
		if (pemain1 == null) {
			System.exit(0);
		}
		pemain2 = JOptionPane.showInputDialog("Masukkan nama pemain2");
		if (pemain2 == null) {
			System.exit(0);
		}

		if (pemain1.equals(""))
			pemain1 = "Anonim";
		if (pemain2.equals(""))
			pemain2 = "Anonim";
		if (pemain1.equals(pemain2)) {
			pemain1 += "1";
			pemain2 += "2";
		}

		int result = JOptionPane.showConfirmDialog(null,
				"Are you first player?", "Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			board.getListener().setFirstPlayer(true);
		} else {
			board.getListener().setFirstPlayer(false);
		}

		board.getListener().getBlack().setName(pemain1);
		board.getListener().getWhite().setName(pemain2);
		board.getListener().getLabel1().setText(pemain1);
		board.getListener().getLabel2().setText(pemain2);

		board.getListener().getTimer1().setText("00:00");
		board.getListener().getTimer2().setText("00:00");
	}

	/**
	 * Method khusus yang digunakan untuk menampilkan papan pada layar / screen.
	 */
	public void viewBoard() {
		MenuBar mb = new MenuBar();
		Menu file = new Menu("Menu");
		MenuItem save = new MenuItem("Save");
		MenuItem open = new MenuItem("Load");
		MenuItem refresh = new MenuItem("Refresh");
		MenuItem quit = new MenuItem("Quit");
		Menu config = new Menu("Configuration");
		MenuItem colorPin = new MenuItem("Color Player");
		MenuItem colorBoard = new MenuItem("Color Board");
		MenuItem boardSize = new MenuItem("Board Settings");
		Menu help = new Menu("Help");
		MenuItem about = new MenuItem("About");
		MenuItem log = new MenuItem("Log");

		if (pemain1.equals("") || pemain2.equals("")) {
			setPlayer();
		}
		board.setStatus(board.getStatus().getText() + pemain1
				+ " memulai permainan.");

		try {
			PrintWriter pw = new PrintWriter("data.txt");
			pw.println("null");
			pw.println("null");
			pw.println("null");
			pw.close();
			board.getListener()
					.getOn()
					.uploadFile(
							"data.txt",
							"gomoku" + pemain1.toLowerCase()
									+ pemain2.toLowerCase() + board.getRows()
									+ board.getCols() + ".txt", "game/");
			board.getListener().getReceiveData().start();
		} catch (FileNotFoundException e2) {
			board.getListener().text
					.append("file not found exception ln : 184 "
							+ getClass().getName() + "\n");
		}

		// Action listener untuk fungsi save
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Date date = new Date();
					String filename = "Gomoku" + board.getRows() + "x"
							+ board.getCols() + "["
							+ new Timestamp(date.getTime()) + "].txt";
					PrintWriter pw = new PrintWriter(filename);
					for (int i = 0; i < board.getRows(); i++) {
						for (int j = 0; j < board.getCols(); j++) {
							pw.print(board.getAsset(i, j).getName() + " ");
						}
						pw.println();
					}
					pw.println(board.getListener().getBlack().getName());
					pw.println(board.getListener().getWhite().getName());
					pw.println(board.getListener().getTimer1().getText());
					pw.println(board.getListener().getTimer2().getText());
					pw.println(board.getListener().getBlack().getColor()
							.getRGB());
					pw.println(board.getListener().getWhite().getColor()
							.getRGB());
					pw.println(board.getKotak1().getRGB());
					pw.println(board.getKotak2().getRGB());
					pw.close();
					board.getListener()
							.getOn()
							.uploadFile(
									filename,
									board.getRows()
											+ "x"
											+ board.getCols()
											+ board.getListener().getBlack()
													.getName().toLowerCase()
											+ board.getListener().getWhite()
													.getName().toLowerCase()
											+ ".txt", "");
					JOptionPane.showMessageDialog(null, "Game saved on cloud!");
				} catch (FileNotFoundException e1) {
					board.getListener().text.append("fie not found ln : 230 "
							+ getClass().getName() + "\n");
				}

			}
		});

		// Action listener khusus untuk fungsi load
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				class Load implements ListSelectionListener {
					String data;

					@Override
					public void valueChanged(ListSelectionEvent e) {
						JList files = (JList) e.getSource();
						if (!e.getValueIsAdjusting()) {
							List<String> selectedValuesList = files
									.getSelectedValuesList();
							data = selectedValuesList.toString();
						}
					}

				}

				JFrame frame = new JFrame("Load Game on Server");
				JPanel panel = new JPanel();
				JButton select = new JButton("Select");
				try {
					board.getListener().getOn().ftp.enterLocalPassiveMode();
					FTPFile[] files;
					files = board.getListener().getOn().ftp.listFiles();
					String[] filesname = new String[files.length];
					for (int i = 0; i < files.length; i++) {
						filesname[i] = files[i].getName();
					}
					JList files1 = new JList(filesname);
					Load listener = new Load();
					files1.addListSelectionListener(listener);

					frame.add(new JScrollPane(files1), BorderLayout.CENTER);
					select.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							InputStreamReader isr = board
									.getListener()
									.getOn()
									.getFile(
											board.getListener().urlToGetFile
													+ listener.data.substring(
															1,
															listener.data
																	.length() - 1));
							Scanner in = new Scanner(isr);

							int count = 0;
							for (int i = 0; i < board.getRows(); i++) {
								for (int j = 0; j < board.getCols(); j++) {
									board.getAsset(i, j).setName(in.next());

									if (board.getAsset(i, j).getName()
											.charAt(0) == 'X') {
										board.getAsset(i, j).setClick(true);
										board.getAsset(i, j).setPlayerColor(
												board.getListener().getBlack()
														.getColor());
										board.getListener().getAssets(i, j)
												.setEnabled(false);
										count++;
									} else if (board.getAsset(i, j).getName()
											.charAt(0) == 'O') {
										board.getAsset(i, j).setClick(true);
										board.getAsset(i, j).setPlayerColor(
												board.getListener().getWhite()
														.getColor());
										board.getListener().getAssets(i, j)
												.setEnabled(false);
										count++;
									} else {
										getBoard().getAsset(i, j).setClick(
												false);
									}
									board.getAsset(i, j).repaint();

								}
							}

							board.getListener().getBlack().setName(in.next());
							board.getListener().getWhite().setName(in.next());
							board.getListener()
									.getLabel1()
									.setText(
											board.getListener().getBlack()
													.getName());
							board.getListener()
									.getLabel2()
									.setText(
											board.getListener().getWhite()
													.getName());
							board.getListener().getTimer1().setText(in.next());
							board.getListener().getTimer2().setText(in.next());
							board.getListener().getSwingTimer1().stop();
							board.getListener().getSwingTimer2().stop();

							Color playerColor1 = new Color(in.nextInt());
							Color playerColor2 = new Color(in.nextInt());

							board.getListener().getBlack()
									.setColor(playerColor1);
							board.getListener().getWhite()
									.setColor(playerColor2);
							board.getListener().getAssetPlayer1()
									.setPlayerColor(playerColor1);
							board.getListener().getAssetPlayer2()
									.setPlayerColor(playerColor2);

							board.setKotak1(new Color(in.nextInt()));
							board.setKotak2(new Color(in.nextInt()));

							JOptionPane.showMessageDialog(null,
									"Melanjutkan pertandingan "
											+ board.getListener().getBlack()
													.getName()
											+ " Vs "
											+ board.getListener().getWhite()
													.getName());
							board.getStatus()
									.setText(
											"Giliran "
													+ (board.getListener()
															.getTurn() ? board
															.getListener()
															.getWhite()
															.getName() : board
															.getListener()
															.getBlack()
															.getName())
													+ " jalan.");

							String data = "null";
							if (count % 2 == 0) {
								board.getListener().setTurn(false);
								if (board.getListener().isFirstPlayer()) {
									board.getListener().getReceiveData().stop();
									board.getListener().setEnable(true);
									board.getStatus().setText(
											"Giliran "
													+ board.getListener()
															.getBlack()
															.getName()
													+ " jalan.");
								} else {
									board.getListener().getReceiveData()
											.start();
									board.getListener().setEnable(false);
									data = "O0,0";
								}
							} else {
								board.getListener().setTurn(true);
								if (!board.getListener().isFirstPlayer()) {
									board.getListener().getReceiveData().stop();
									board.getListener().setEnable(true);
									board.getStatus().setText(
											"Giliran "
													+ board.getListener()
															.getBlack()
															.getName()
													+ " jalan.");
								} else {
									board.getListener().getReceiveData()
											.start();
									board.getListener().setEnable(false);
									data = "X0,0";
								}
								try {
									PrintWriter pw = new PrintWriter("data.txt");
									pw.println(data);
									pw.println("00:00");
									pw.println("00:00");
									pw.close();
									board.getListener()
											.getOn()
											.uploadFile(
													"data.txt",
													"gomoku"
															+ board.getListener()
																	.getBlack()
																	.getName()
																	.toLowerCase()
															+ board.getListener()
																	.getWhite()
																	.getName()
																	.toLowerCase()
															+ board.getRows()
															+ board.getCols()
															+ ".txt", "game/");
								} catch (FileNotFoundException e2) {
									board.getListener().text
											.append("file not found exception ln : 431 "
													+ getClass().getName()
													+ "\n");
								}

							}

							if (board.getListener().getTurn()) {
								board.getListener().getSwingTimer2().start();
								board.getListener().getSwingTimer1().stop();
								board.getListener().getAssetPlayer1()
										.setAssetColor(null);
								board.getListener().getAssetPlayer2()
										.setAssetColor(Color.CYAN);
							} else {
								board.getListener().getSwingTimer1().start();
								board.getListener().getSwingTimer2().stop();
								board.getListener().getAssetPlayer1()
										.setAssetColor(Color.CYAN);
								board.getListener().getAssetPlayer2()
										.setAssetColor(null);
							}

							frame.dispose();
							in.close();
						}
					});
					frame.add(panel, BorderLayout.SOUTH);
					panel.add(select);
					frame.setSize(400, 200);
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setLocationRelativeTo(null);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				} catch (FileNotFoundException e1) {
					board.getListener().text.append("file not found ln : 489 "
							+ getClass().getName() + "\n");
				} catch (IOException e1) {
					board.getListener().text.append("io exception ln : 492 "
							+ getClass().getName() + "\n");
				} catch (Exception e1) {
					board.getListener().text.append("fatal error ln : 495 "
							+ getClass().getName() + "\n");
				}
				// }
			}
		});

		// Action lstener khusus untuk merubah warna pemain
		colorPin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFrame frameColor = new JFrame("Change color piece");
				JLabel label = new JLabel("Player 1    -    Player 2");
				JPanel panelAtas = new JPanel();
				JPanel panel = new JPanel(new GridLayout(1, 2));
				JButton confirm = new JButton("OK");
				final JButton kotak1 = new JButton();
				final JButton kotak2 = new JButton();

				kotak1.setBackground(board.getListener().getBlack().getColor());
				kotak2.setBackground(board.getListener().getWhite().getColor());
				kotak1.setOpaque(true);
				kotak1.setBorderPainted(false);
				kotak2.setOpaque(true);
				kotak2.setBorderPainted(false);

				kotak1.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							Color initialBackground = kotak1.getBackground();
							Color background = JColorChooser.showDialog(null,
									"Pilih warna", initialBackground);
							if (background.equals(Color.CYAN)) {
								JOptionPane.showMessageDialog(null,
										"Cyan is reserved color");
							} else if (background.equals(kotak2.getBackground())) {
								JOptionPane.showMessageDialog(null,
										"Warna player harus berbeda");
							} else if (background.equals(board.getKotak1())
									|| background.equals(board.getKotak2())) {
								JOptionPane.showMessageDialog(null,
										"Tidak bisa sama dengan warna board");
							} else if (background != null) {
								kotak1.setBackground(background);
								board.getListener().getBlack()
										.setColor(background);
								board.getListener().getAssetPlayer1()
										.setPlayerColor(background);
								for (int i = 0; i < board.getRows(); i++) {
									for (int j = 0; j < board.getCols(); j++) {
										if (board.getAsset(i, j).getName()
												.charAt(0) == 'X') {
											board.getAsset(i, j).setClick(true);
											board.getAsset(i, j)
													.setPlayerColor(background);
										}
									}
								}
							}
						} catch (NullPointerException err) {

						}
					}
				});

				kotak2.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							Color initialBackground = kotak1.getBackground();
							Color background = JColorChooser.showDialog(null,
									"Pilih warna", initialBackground);
							if (background.equals(Color.CYAN)) {
								JOptionPane.showMessageDialog(null,
										"Cyan is reserved color");
							} else if (background.equals(kotak1.getBackground())) {
								JOptionPane.showMessageDialog(null,
										"Warna player harus berbeda");
							} else if (background.equals(board.getKotak1())
									|| background.equals(board.getKotak2())) {
								JOptionPane.showMessageDialog(null,
										"Tidak bisa sama dengan warna board");
							} else if (background != null) {
								kotak2.setBackground(background);
								board.getListener().getWhite()
										.setColor(background);
								board.getListener().getAssetPlayer2()
										.setPlayerColor(background);
								for (int i = 0; i < board.getRows(); i++) {
									for (int j = 0; j < board.getCols(); j++) {
										if (board.getAsset(i, j).getName()
												.charAt(0) == 'O') {
											board.getAsset(i, j).setClick(true);
											board.getAsset(i, j)
													.setPlayerColor(background);
										}
									}
								}
							}
						} catch (NullPointerException err) {

						}
					}
				});

				confirm.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						frameColor.dispose();
					}
				});

				panel.add(kotak1);
				panel.add(kotak2);
				panelAtas.add(label);
				frameColor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frameColor.add(panelAtas, BorderLayout.NORTH);
				frameColor.add(panel);
				frameColor.add(confirm, BorderLayout.SOUTH);
				frameColor.setResizable(false);
				frameColor.setSize(200, 150);
				frameColor.setVisible(true);
				frameColor.setLocationRelativeTo(null);
			}
		});

		// Action listener khusus untuk merubah warna papan
		colorBoard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFrame frameColor = new JFrame("Change color board");
				JLabel label = new JLabel("Choose color to change");
				JPanel panelAtas = new JPanel();
				JPanel panel = new JPanel(new GridLayout(1, 2));
				JButton confirm = new JButton("OK");
				JButton kotak1 = new JButton();
				JButton kotak2 = new JButton();

				kotak1.setBackground(board.getKotak1());
				kotak1.setOpaque(true);
				kotak1.setBorderPainted(false);
				kotak2.setBackground(board.getKotak2());
				kotak2.setOpaque(true);
				kotak2.setBorderPainted(false);

				kotak1.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							Color initialBackground = kotak1.getBackground();
							Color background = JColorChooser.showDialog(null,
									"Pilih warna", initialBackground);
							if (background.equals(Color.CYAN)) {
								JOptionPane.showMessageDialog(null,
										"Cyan is reserved color");
							} else if (background.equals(board.getListener()
									.getBlack().getColor())
									|| background.equals(board.getListener()
											.getWhite().getColor())) {
								JOptionPane.showMessageDialog(null,
										"Tidak bisa sama dengan warna player");
							} else if (background != null) {
								kotak1.setBackground(background);
								board.setKotak1(background);
							}
						} catch (NullPointerException err) {

						}
					}
				});

				kotak2.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							Color initialBackground = kotak1.getBackground();
							Color background = JColorChooser.showDialog(null,
									"Pilih warna", initialBackground);
							if (background.equals(Color.CYAN)) {
								JOptionPane.showMessageDialog(null,
										"Cyan is reserved color");
							} else if (background.equals(board.getListener()
									.getBlack().getColor())
									|| background.equals(board.getListener()
											.getWhite().getColor())) {
								JOptionPane.showMessageDialog(null,
										"Tidak bisa sama dengan warna player");
							} else if (background != null) {
								kotak2.setBackground(background);
								board.setKotak2(background);
							}
						} catch (NullPointerException err) {

						}
					}
				});

				confirm.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						frameColor.dispose();
					}
				});

				panel.add(kotak1);
				panel.add(kotak2);
				panelAtas.add(label);
				frameColor.add(panelAtas, BorderLayout.NORTH);
				frameColor.add(panel);
				frameColor.add(confirm, BorderLayout.SOUTH);
				frameColor.setResizable(false);
				frameColor.setSize(200, 150);
				frameColor.setVisible(true);
				frameColor.setLocationRelativeTo(null);
				frameColor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		// Action listner khusus untuk fungsi refresh pada permainan
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String data = "";
					data = "gomoku"
							+ board.getListener().getBlack().getName()
									.toLowerCase()
							+ board.getListener().getWhite().getName()
									.toLowerCase() + board.getRows()
							+ board.getCols() + ".txt";

					InputStreamReader isr = board.getListener().getOn()
							.getFile(board.getListener().urlToGetFile + data);
					Scanner in = new Scanner(isr);
					String s = in.next();
					int i = Integer.parseInt(s.substring(1, s.indexOf(',')));
					int j = Integer.parseInt(s.substring(s.indexOf(',') + 1)
							.trim());

					if (s.charAt(0) == 'X') {
						if (!board.getListener().isFirstPlayer()) {
							board.getListener().setEnable(true);
						} else {
							board.getListener().setEnable(false);
						}

						board.getAsset(i, j).setClick(true);
						board.getAsset(i, j).setPlayerColor(
								board.getListener().getBlack().getColor());
						board.getListener().cekMenang(i, j, "X");

						board.getListener().setTurn(true);
						board.getStatus().setText(
								"Giliran "
										+ board.getListener().getWhite()
												.getName() + " jalan.");
						board.getListener().getAssetPlayer1()
								.setAssetColor(null);
						board.getListener().getAssetPlayer2()
								.setAssetColor(Color.CYAN);
						board.getListener().getSwingTimer1().stop();
						board.getListener().getSwingTimer2().start();
					} else if (s.charAt(0) == 'O') {
						if (board.getListener().isFirstPlayer()) {
							board.getListener().setEnable(true);
						} else {
							board.getListener().setEnable(false);
						}

						board.getAsset(i, j).setClick(true);
						board.getAsset(i, j).setPlayerColor(
								board.getListener().getWhite().getColor());
						board.getListener().cekMenang(i, j, "O");

						board.getListener().setTurn(false);
						board.getStatus().setText(
								"Giliran "
										+ board.getListener().getBlack()
												.getName() + " jalan.");
						board.getListener().getAssetPlayer1()
								.setAssetColor(Color.CYAN);
						board.getListener().getAssetPlayer2()
								.setAssetColor(null);
						board.getListener().getSwingTimer1().start();
						board.getListener().getSwingTimer2().stop();
					}

					board.getAsset(i, j).setName(s);
					in.close();

				} catch (NullPointerException e1) {
					board.getListener().text
							.append("null pointer exception ln : 776 "
									+ getClass().getName() + "\n");
				} catch (NoSuchElementException e1) {
					board.getListener().text
							.append("no such element exception ln : 780 "
									+ getClass().getName() + "\n");
				} catch (Exception e1) {
					board.getListener().text.append("fatal error ln : 783 "
							+ getClass().getName() + "\n");
				}

			}
		});

		// Action listener khusus untuk fungsi quit
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null,
						"Mau udahan? :(", "Quit Confirmation",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					board.getListener()
							.getOn()
							.remove("game/gomoku"
									+ board.getListener().getBlack().getName()
											.toLowerCase()
									+ board.getListener().getWhite().getName()
											.toLowerCase() + board.getRows()
									+ board.getCols() + ".txt");
					board.getListener().getOn().disconnect();
					try {
						PrintWriter pw = new PrintWriter("log file.txt");
						pw.println(new Timestamp(new Date().getTime()));
						pw.print(board.getListener().text.getText());
						pw.close();
					} catch (FileNotFoundException e1) {
						board.getListener().text
								.append("file not found exception ln : 815 "
										+ getClass().getName() + "\n");
					}
					System.exit(0);
				}
			}
		});

		// Action listener khusus untuk menampilkan layar about
		about.addActionListener(new ActionListener() {
			JFrame frame;
			JPanel panel;
			JTextPane text;

			@Override
			public void actionPerformed(ActionEvent e) {
				frame = new JFrame("About");
				panel = new JPanel();
				text = new JTextPane();
				frame.setLayout(new GridLayout(1, 2));

				panel.add(text);

				SimpleAttributeSet attribs = new SimpleAttributeSet();
				StyleConstants.setAlignment(attribs,
						StyleConstants.ALIGN_CENTER);
				text.setParagraphAttributes(attribs, true);

				text.setEditable(false);
				text.setBackground(null);
				text.setBorder(null);
				text.setOpaque(false);
				text.setText("===========================================================\nABOUT PROGRAMMER"
						+ "\n==========================================================="
						+ "\nMuhammad Gilang Januar\n1406543845\n\n\n"
						+ "==========================================================\nABOUT \""
						+ getTitle()
						+ "\"\n=========================================================="
						+ "\nGOMOKU is a modification game of \nTic Tac Toe. And \""
						+ getTitle()
						+ "\" v1.1.09\nis a modification game of GOMOKU :p\n\nFeatures :\n"
						+ "1. Load and save in local and cloud\n"
						+ "2. Change color board and piece\n"
						+ "3. Change board size and win point\n"
						+ "4. ONLINE!\n" + "And others");

				frame.add(panel);
				frame.setVisible(true);
				frame.setSize(500, 350);
				frame.setResizable(false);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}
		});

		// Action listener khusus untuk mengatur ukuran papan dan poin menang
		boardSize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Config Board");
				JPanel panel = new JPanel(new GridLayout(3, 2));
				JLabel rows = new JLabel("Rows : ", JLabel.RIGHT);
				JLabel cols = new JLabel("Cols : ", JLabel.RIGHT);
				JLabel wincount = new JLabel("Count for win : ", JLabel.RIGHT);
				JTextField textRows = new JTextField(board.getRows() + "", 10);
				JTextField textCols = new JTextField(board.getCols() + "", 10);
				JTextField textCount = new JTextField(board.getListener()
						.getWincount() + "", 10);
				JButton ok = new JButton("OK");
				ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							int rows = Integer.parseInt(textRows.getText()
									.trim());
							int cols = Integer.parseInt(textCols.getText()
									.trim());
							int wincount = Integer.parseInt(textCount.getText()
									.trim());
							board.setRows(rows);
							board.setCols(cols);
							board.getListener().setWincount(wincount);
							if (rows < 1 || cols < 1 || wincount < 1) {
								throw new RuntimeException("Number format ");
							}
						} catch (RuntimeException e1) {
							board.getListener().text
									.append("runtime exception ln : 905 "
											+ getClass().getName() + "\n");
							dispose();
						}
						dispose();
						Gomoku gom = new Gomoku(board.getRows(), board
								.getCols(), board.getListener().getWincount());
						gom.setPemain1(board.getListener().getBlack().getName());
						gom.setPemain2(board.getListener().getWhite().getName());
						gom.getBoard().setKotak1(board.getKotak1());
						gom.getBoard().setKotak2(board.getKotak2());
						gom.getBoard()
								.getListener()
								.getLabel1()
								.setText(
										board.getListener().getLabel1()
												.getText());
						gom.getBoard()
								.getListener()
								.getLabel2()
								.setText(
										board.getListener().getLabel2()
												.getText());

						gom.viewBoard();
						frame.dispose();
					}
				});

				panel.add(rows);
				panel.add(textRows);
				panel.add(cols);
				panel.add(textCols);
				panel.add(wincount);
				panel.add(textCount);

				frame.add(panel);
				frame.add(ok, BorderLayout.SOUTH);

				frame.setVisible(true);
				frame.setSize(250, 150);
				frame.setResizable(false);
				frame.setLocationRelativeTo(null);
			}
		});

		log.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Log File");
				JTextArea text = new JTextArea();
				JScrollPane sp = new JScrollPane(board.getListener().text);

				frame.add(sp);
				frame.setVisible(true);
				frame.setSize(250, 350);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setResizable(false);
				frame.setLocationRelativeTo(null);
			}
		});

		file.add(save);
		file.add(open);
		file.add(refresh);
		file.add(quit);

		config.add(colorPin);
		config.add(colorBoard);
		config.add(boardSize);

		help.add(log);
		help.add(about);

		mb.add(file);
		mb.add(config);
		mb.add(help);

		setMenuBar(mb);
		setTitle("Katanya Gomoku");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setLocationRelativeTo(null);
	}

	/**
	 * Method accessor yang mengembalikan sebuah object dari kelas Board yang
	 * merepresentasikan papan dalam permainan gomoku.
	 * 
	 * @return Object Board yang telah didefinisikan sebelumnya pada
	 *         konstruktor.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Method accessor untuk mengetahui nama pemain pertama.
	 * 
	 * @return String dari pemain1 yang merepresentasikan nama dari pemain
	 *         pertama.
	 */
	public String getPemain1() {
		return pemain1;
	}

	/**
	 * Method mutator yang berfungsi untuk merubah nama pemain1 dalam bentuk
	 * String.
	 * 
	 * @param pemain1
	 *            Nama baru dari pemain1 yang bertipe String yang akan
	 *            mendefinisikan nama pemain1 sebelumnya.
	 */
	public void setPemain1(String pemain1) {
		this.pemain1 = pemain1;
		board.getListener().getBlack().setName(pemain1);
	}

	/**
	 * Method accessor untuk mengetahui nama pemain kedua.
	 * 
	 * @return String dari pemain2 yang merepresentasikan nama dari pemain
	 *         kedua.
	 */
	public String getPemain2() {
		return pemain2;
	}

	/**
	 * Method mutator yang berfungsi untuk merubah nama pemain2 dalam bentuk
	 * String.
	 * 
	 * @param pemain2
	 *            Nama baru dari pemain2 yang bertipe String yang akan
	 *            mendefinisikan nama pemain2 sebelumnya.
	 */
	public void setPemain2(String pemain2) {
		this.pemain2 = pemain2;
		board.getListener().getWhite().setName(pemain2);
	}
}
