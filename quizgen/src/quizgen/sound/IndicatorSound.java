/**
 * IndicatorSound.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * <code>IndicatorSound</code> plays a different sound for correct
 * and incorrect answers.
 * 
 * @author Ann Marie Steichmann
 *
 */
public class IndicatorSound {
	
	private AudioStream cas;
	private AudioStream ias;

	public IndicatorSound() {
		
		initSounds();
	}
	
	private void initSounds() {
		
		try {
			InputStream cis = new FileInputStream( "sounds/chimes.wav" );
			cas = new AudioStream( cis );
			InputStream iis = new FileInputStream( "sounds/chord.wav" );
			ias = new AudioStream( iis );
		} catch (FileNotFoundException e) {
			System.out.println("Unable to load sound file: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Unable to initialize audio stream: " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Play a sound for a correct answer
	 */
	public void playCorrectSound() {
		
		AudioPlayer.player.start( cas );
	}
	
	/**
	 * Play a sound for an incorrect answer
	 */
	public void playIncorrectSound() {
	
		AudioPlayer.player.start( ias );
	}
		
	/**
	 * TEST MAIN
	 * @param args
	 */
	public static void main( String[] args ) {
		
		IndicatorSound sound = new IndicatorSound();
		sound.playCorrectSound();
		
		try {
			Thread.sleep( 2000 );
		} catch (InterruptedException e) {
			// ignore
		}
		
		sound.playIncorrectSound();

		try {
			Thread.sleep( 2000 );
		} catch (InterruptedException e) {
			// ignore
		}
		
		sound = new IndicatorSound();
		sound.playCorrectSound();
	}
}
