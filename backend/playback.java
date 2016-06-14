package backend;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sun.audio.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.Dimension;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.DefaultTableModel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import java.sql.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/*
public class playback implements Runnable
{
	public String audioFilePath;
	public boolean isPlaying = false;
	public Thread thrd;
	public playback(String AudioFilePath)
	{
		audioFilePath = AudioFilePath;
	}
	public void run() 
	{
		try 
		{
			FileInputStream fis = new FileInputStream(audioFilePath);
			Player playMP3 = new Player(fis);
			if(isPlaying == false)
			{
				playMP3.play();
				isPlaying = true;
			}
			else
			{
				playMP3.close();
				isPlaying = false;
			}
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
*/

public class playback implements Runnable
{
	public Player playMP3;
	public boolean isPlaying = false;
	//Thread thrd;
	public playback(boolean IsPlaying, Player playMp3)
	{
		System.out.println("init player");
		playMP3 = playMp3;
		System.out.println("init boolean isPlaying");
		isPlaying = IsPlaying;
		//thrd = new Thread();
		//thrd.start();
	}
	public void run()
	{
		try {
			if(isPlaying == false)
			{

				System.out.println("playing");
				isPlaying = true;
				playMP3.play();
			}
			else
			{
				System.out.println("closing");
				isPlaying = false;
				playMP3.close();
			}
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}