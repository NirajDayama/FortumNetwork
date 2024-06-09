package model;

import java.util.ArrayList;
import java.util.Collections;

public class SinkNode extends Node
{
	public final int baseDemandKW;
	
	public SinkNode(final String name, final int baseDemandKW)
	{
		super(name);
		super.outGoingLinks = Collections.unmodifiableList(super.outGoingLinks);
		this.baseDemandKW = baseDemandKW;
	}
}
