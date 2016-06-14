package backend;

public class Node 
{
	public String ip;
	public int port;
	Node()
	{
		ip ="";
		port = 0;
	}
	
	public Node(String IP, int Port)
	{
		this.ip = IP;
		this.port = Port;
	}
		
	public void setIP(String IP)
	{
		this.ip = IP;
	}
	
	public void setPort(int Port)
	{
		this.port = Port;
	}
	
	public String getIp()
	{
		return this.ip;
	}
	
	public int getPort()
	{
		return port;
	}
	
	boolean isEqual(Node o)
	{
		if(this.ip == o.ip && this.port == o.port)
		{
			return true;
		}
		return false;
	}
	
}

