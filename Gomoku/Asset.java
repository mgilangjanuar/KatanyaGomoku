package Gomoku;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

/**
 * Kelas yang merepresentasikan komponen pembentuk papan dalam permainan Gomoku
 * yang juga merupakan modifikasi dari kelas javax.swing.JButton. Pada kelas ini
 * nantinya akan dibuatkan array 2 dimensi pada kelas Board untuk
 * merepresentasikan bentuk dari sebuah papan permainan dalam gomoku yang
 * sesungguhnya.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */

public class Asset extends JButton {
	private boolean click;
	private Color playerColor;
	private Color assetColor;

	/**
	 * Konstruktor default pada kelas Asset yang mendefinisikan nilai awal semua
	 * instance variable.
	 */
	public Asset() {
		click = false;
		playerColor = null;
		assetColor = null;
	}

	/**
	 * Method yang override paintComponent dari kelas JComponent untuk membuat
	 * bentuk oval atau lingkaran yang memiliki warna sesuai warna pemain ketika
	 * variable click bernilai true.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setOpaque(true);
		setBorderPainted(false);
		setBackground(assetColor);
		if (click) {
			g.setColor(playerColor);
			g.fillOval(getHorizontalAlignment(), getVerticalAlignment(),
					getWidth(), getHeight());
		}else{
			g.setColor(null);
		}
		repaint();
	}

	/**
	 * Method accessor untuk mendapatkan nilai dari click.
	 * 
	 * @return Nilai boolean click.
	 */
	public boolean isClick() {
		return click;
	}

	/**
	 * Method mutator untuk merubah nilai dari variabel click.
	 * 
	 * @param click
	 *            Nilai boolean baru yang digunakan untuk mengubah nilai click.
	 */
	public void setClick(boolean click) {
		this.click = click;
	}

	/**
	 * Method accessor untuk mendapatkan warna pemain.
	 * 
	 * @return Warna pemain dari kelas Asset.
	 */
	public Color getPlayerColor() {
		return playerColor;
	}

	/**
	 * Method mutator untuk merubah warna pemain.
	 * 
	 * @param playerColor
	 *            Warna pemain baru yang menggantikan atau mengubah warna pemain
	 *            sebelumnya.
	 */
	public void setPlayerColor(Color playerColor) {
		this.playerColor = playerColor;
	}

	/**
	 * Method accessor untuk mendapatkan warna dari asset.
	 * 
	 * @return Object Player dari kelas Asset.
	 */
	public Color getAssetColor() {
		return assetColor;
	}

	/**
	 * Method mutator untuk merubah warna asset.
	 * 
	 * @param assetColor
	 *            Warna asset baru yang digunakan untuk merubah warna asset
	 *            sebelumnya.
	 */
	public void setAssetColor(Color assetColor) {
		this.assetColor = assetColor;
	}

}
