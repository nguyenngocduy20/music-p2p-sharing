package backend;

import java.io.*;

public class available {
	public String content;
	public Node t_node;
	
	available()
	{
		
	}
	
	available(String Content, Node T_node)
	{
		content = Content;
		t_node = T_node;
	}
	
	available(String Content, String IP, int Port)
	{
		content = Content;
		t_node = new Node(IP, Port);
	}
}
