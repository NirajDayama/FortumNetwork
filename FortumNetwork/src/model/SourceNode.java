package model;

import java.util.Collections;

public class SourceNode extends Node
{
	public final int maxOutputKW;
	
	public SourceNode(final String name, final int maxOutputKW)
	{
		super(name);
		super.inComingLinks = Collections.unmodifiableList(super.inComingLinks);
		this.maxOutputKW = maxOutputKW;
	}
}
