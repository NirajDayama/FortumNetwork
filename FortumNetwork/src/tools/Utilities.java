package tools;

import model.Link;
import model.Node;

public class Utilities
{
	public static final boolean isFileNameArgumentOK(final String [] args)
	{
		if(args == null || args.length == 0 || args[0] == null || args[0].length() < 3)
		{
			System.err.println("Please provide input Excel data file location");
			System.exit(0);
			return false;
		}
		return true;
	}
	
	public static final Node getOnePreviousNode(final Node node)
	{
		if(node == null)
		{
			System.err.println("Bug: You are asking for parent of null node");
			return null;
		}
			
		for(final Link incomingLink: node.inComingLinks)
		{
			if(incomingLink.fromNode != null)
				return incomingLink.fromNode;
		}
		System.err.println("Did not find any previous node from "+node);
		return null;
	}
	
}
