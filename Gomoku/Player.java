package Gomoku;

import java.awt.Color;

/**
 * Kelas yang merepresentasikan pemain/player di dalam permainan gomoku.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */

public class Player {
	private Color color;
	private String name;

	/**
	 * Konstruktor default dari kelas Player yang mendefinisikan warna dan nama.
	 * 
	 * @param color
	 *            Mendefinisikan warna bidak pemain.
	 * @param name
	 *            Mendefinisikan nama dari pemain.
	 */
	public Player(Color color, String name) {
		super();
		this.color = color;
		this.name = name;
	}

	/**
	 * Method accessor untuk mendapatkan warna pemain.
	 * 
	 * @return Warna pemain.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Method mutator untuk mengubah warna pemain.
	 * 
	 * @param color
	 *            Warna baru yang ingin menggantikan warna pemain.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Method accessor untuk mendapatkan nama pemain.
	 * 
	 * @return Nama pemain.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method mutator untuk mengubah nama pemain.
	 * 
	 * @param name
	 *            Nama pemain baru yang ingin menggantikan nama pemain.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
