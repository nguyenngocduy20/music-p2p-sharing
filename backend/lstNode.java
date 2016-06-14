package backend;

public class lstNode 
{
	Node nodes[] = new Node[10];
	lstNode()
	{
		
	}
	
	lstNode(lstNode LstNode)
	{
		int i=0;
		if(LstNode.length() > 10)
			this.nodes = new Node[LstNode.length()];
		while(i > LstNode.length())
		{
			this.nodes[i] = LstNode.nodes[i];
			i++;
		}
	}
	
	int length()
	{
		int l=0;
		try
		{
			while(nodes[l] != null)
			{
				l++;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("");
		}
		return l;
	}
	
	void increaseSize(int n)
	{
		Node copy[] = new Node[this.length() + n];
		System.arraycopy(this.nodes, 0, copy, 0, this.length());
		this.nodes = copy;
	}
	
	void push_back(Node n)
	{
		this.increaseSize(1);
		
		this.nodes[this.length()] = n;
	}
	
	void removeDuplicate()
	{
		for (int i = 0; i< this.length(); i++)
		{
			for(int j = i + 1; j < this.length(); j++)
			{
				if(this.nodes[i].isEqual(this.nodes[j]))
				{
					for(int t = j; t < this.length(); t++)
					{
						this.nodes[t] = this.nodes[t+ 1];
					}
					this.nodes[this.length() - 1] = null;
				}
			}
		}
	}
	
}
