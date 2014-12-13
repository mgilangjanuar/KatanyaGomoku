package Gomoku;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * Kelas extra yang dibuat untuk meload file wav dan digunakan dalam permainan
 * gomoku saat selesei permainan.
 * 
 * @author muhammadgilangjanuar
 * @since 2014
 * @version 1.1.09
 */

public class PlayMusic {

	private File file;
	private AudioInputStream audio;
	private AudioFormat format;
	private SourceDataLine auline;
	private DataLine.Info info;
	private int nBytesRead;
	private byte[] abData;

	/**
	 * Konstructor default dari kelas PlayMusic yang disiapkan untuk
	 * mendefinisikan seluruh instance variable.
	 * 
	 * @param wavFile
	 *            Lokasi dari file music yang berekstensi wav diletakkan. Cukup
	 *            menulis nama file beserta ekstensi bila file diletakkan pada
	 *            satu folder dengan class PlayMusic yang sama.
	 */
	public PlayMusic(String wavFile) {
		try {
			file = new File(wavFile);
			audio = AudioSystem.getAudioInputStream(file);
			format = audio.getFormat();
			info = new DataLine.Info(SourceDataLine.class, format);
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception E) {
			System.out.println(E.getMessage());
		}
	}

	/**
	 * Method khusus untuk memutar lagu.
	 */
	public void play() {
		auline.start();
		nBytesRead = 0;
		abData = new byte[524288];
		while (nBytesRead != -1) {
			try {
				nBytesRead = audio.read(abData, 0, abData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (nBytesRead >= 0) {
				auline.write(abData, 0, nBytesRead);
			}
		}
	}

	/**
	 * Method khusus untuk menghentikan lagu.
	 */
	public void stop() {
		auline.stop();
	}
}
