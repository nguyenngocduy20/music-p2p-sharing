package backend;
import java.io.*;

public class ReadNode
{
	String fileName = "\\Files\\nodes.txt";
	lstNode lstNodes = new lstNode();
	File currentDirectory = new File(new File(".").getAbsolutePath());
	public static void main(String[] args)
	{
		ReadNode nodes = new ReadNode();
		nodes.read();
		for(int i=0; i<nodes.lstNodes.length(); i++)
		{
			System.out.println("IP: " + nodes.lstNodes.nodes[i].ip + " port: " + nodes.lstNodes.nodes[i].port);
		}
		
		//nodes.read(4);
		//nodes.read(5);
	}
	
	//read notes.txt
	void read()
	{
		String lineRead;
		try
		{
			String path = currentDirectory.getCanonicalPath();
			this.fileName = path.concat(fileName);
			System.out.println(this.fileName);
			
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((lineRead = bufferedReader.readLine()) != null)
			{
				String ip = split2IP(lineRead);
				int port = split2Port(lineRead);
				//System.out.println("IP: " + ip + " port: " + port);
				this.lstNodes.push_back(new Node(ip, port));
			}
			
			bufferedReader.close();
			fileReader.close();
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("File not found!");
		}
		catch(IOException ex)
		{
			System.out.println("Error in reading file " + fileName);
		}
	}
	
	// read nodes.txt with line number
	void read(int linenumber)
	{
		String lineRead;
		try
		{
			FileReader fileReader = new FileReader(this.fileName);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int i = 0;
			while((lineRead = bufferedReader.readLine()) != null)
			{
				i++;
				if(i == linenumber)
				{
					String ip = split2IP(lineRead);
					int port = split2Port(lineRead);
					System.out.println("IP: " + ip + " port: " + port);
					break;
				}
			}
			if(i < linenumber)
			{
				System.out.println("Line number is not valid");
			}
			bufferedReader.close();
			fileReader.close();
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("File not found!");
		}
		catch(IOException ex)
		{
			System.out.println("Error in reading file " + fileName);
		}
	}
	
	// split filename to ip
	String split2IP(String lineRead)
	{
		String ip;
		int pos = lineRead.indexOf(" ");
		ip = lineRead.substring(0,pos);
		return ip;
	}
	
	// split filename to port
	int split2Port(String lineRead)
	{
		int port;
		int pos = lineRead.indexOf(" ") + 1;
		port = Integer.parseInt(lineRead.substring(pos, lineRead.length()));
		return port;
	}
	
}
