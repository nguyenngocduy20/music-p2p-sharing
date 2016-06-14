/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author htc
 */
public class Files {
    public static List<String> listFile = new ArrayList<String>(); 
    //String name_1;
    public static int size;
    // Ham loc file mp3
    // Is MP3 return 1, else 0, if error -1
    public static int FilterFile()
    {
        return 0;
    }
    
    // Doc file trong folder goi FillterFile
    public static void ReadFileInFolder()
    {

		File currentDirectory = new File(new File(".").getAbsolutePath());
		String path = "";
		try {
			path = currentDirectory.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        File folder = new File(path + "\\Music\\");
        File[] listOfFile = folder.listFiles();
        char[] arrTest = "mp3".toCharArray();
        for (int i = 0; i < listOfFile.length; i++)
        {
            if (listOfFile[i].isFile())
            {
               int k = 2; // Dung de duyet mang arrTest
               int kt = 1; //KT co phai mp3 ko neu phai thi add vao listFile
               char[] temp = listOfFile[i].getName().toCharArray();
               for (int j = temp.length - 1; j >= 0 && temp[j] != '.'; j--, k--)
               {
                   if (temp[j] != arrTest[k])
                   {
                       kt = 0;
                       break;
                   }
               }
               if (kt == 1)
                    listFile.add(listOfFile[i].getName());
            }
        }
    }
    
    public static void ReadInfoFile()
    {

    }
    
    // tim kiem file co chua chuoi s trong vector listFile
    public static int FindFile(String s)
    {
    	Files.ReadFileInFolder();
    	int index = 0;
    	s = s.toLowerCase();
    	List<String> a = new ArrayList<String>();
    	String temp;
    	for(int i=0; i< s.length(); i++)
    	{
    		System.out.println(i);
    		if(s.charAt(i) == ' ')
    		{
    			a.add(s.substring(index, i));
    			index = i + 1;
    		}
    	}
    	a.add(s.substring(index, s.length()));
    	index = 0;
    	for(int i=0; i<a.size(); i++)
    	{
    		System.out.print("\"" + a.get(i) + "\"\t\t");
    	}
    	
    	int prev_match = 0;
    	int match = 0;
    	System.out.println("");
    	for(int i=0; i< listFile.size(); i++)
    	{
    		temp = listFile.get(i).toLowerCase();
    		for(int j =0; j<a.size(); j++)
    		{
    			if(temp.contains(a.get(j)) == true)
    			{
    				match++;
    			}
    		}
    		
    		if(match > prev_match)
    		{
    			System.out.println(listFile.get(i) + " \t\tmatch: " + match);
    			index = i;
    			prev_match = match;
    		}
			match = 0;
    	}
    	
    	return index;
    }
    public static void main(String[] args)
    {
    	Files f = new Files();
    	f.FindFile("guitar");
    }
 }
