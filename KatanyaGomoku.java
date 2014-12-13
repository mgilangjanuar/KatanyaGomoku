import javax.swing.JOptionPane;

/**
 * Kelas tester yang merupakan representasi dari kelas pengguna object Gomoku.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */
public class KatanyaGomoku {
	public static void main(String[] args) {
		int result = JOptionPane.showConfirmDialog(null, "Play Online ?",
				"Katanya Gomoku", JOptionPane.YES_NO_CANCEL_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			GomokuOnline.Gomoku gom = new GomokuOnline.Gomoku(19, 19, 5);
			gom.viewBoard();
		} else if (result == JOptionPane.NO_OPTION) {
			Gomoku.Gomoku gom = new Gomoku.Gomoku(19, 19, 5);		
			gom.viewBoard();
		} else {
			System.exit(0);
		}

	}
}
