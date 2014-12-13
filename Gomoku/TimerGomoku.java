package Gomoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Kelas yang merepresentasikan timer atau waktu permainan dalam papan gomoku.
 * Kelas ini menggunakan interface ActionListener untuk mengimplementasi method
 * actionPerformed-nya.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */

public class TimerGomoku implements ActionListener {
	private JLabel label;
	private Timer timer;

	/**
	 * Konstruktor default dalam kelas TimerGomoku yang harus mendefinisikan
	 * instance variabel bertipe JLabel dan Timer (dari package javax.swing).
	 * 
	 * @param label
	 *            Komponen label yang digunakan sebagai penampil timer yang
	 *            berupa JLabel.
	 */
	public TimerGomoku(JLabel label) {
		this.label = label;
	}

	/**
	 * Method override actionPerformed yang mengimplementasi format waktu pada
	 * timer di label.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		timer = (Timer) e.getSource();
		int mnt = Integer.parseInt(label.getText().substring(0,
				label.getText().indexOf(':')));
		int dtk = Integer.parseInt(label.getText().substring(
				label.getText().indexOf(':') + 1));

		if (dtk > 58) {
			label.setText(String.format("%02d:%02d", mnt + 1, 0));
		} else {
			label.setText(String.format("%02d:%02d", mnt, dtk + 1));
		}
	}
}
