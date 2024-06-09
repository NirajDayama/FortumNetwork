package model;

public final class Link
{
	public final String name;
	public final Node fromNode;
	public final Node toNode;
	public final double length;
	public final double heatCoefficient;
	public final double diameter;
	public final double capacityKW;
	public final double capacitykgps;
	public final double heatLossW;
	
	public Link(final String name, final Node fromNode, final Node toNode, final double length, final double heatCoefficient, final double diameter, final double capacityKW, final double capacitykgps)
	{
		this.name = name;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.length = length;
		this.heatCoefficient = heatCoefficient;
		this.diameter = diameter;
		this.capacityKW = capacityKW;
		this.capacitykgps = capacitykgps;
		this.heatLossW = heatCoefficient * Math.PI*diameter*length;
	}
}
