package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Node
{
	public final String name;
	
	public List<Link> inComingLinks = new ArrayList<Link>();
	public List<Link> outGoingLinks = new ArrayList<Link>();
	
	public Node(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return "Node<"+name+">";
		
	}
}
