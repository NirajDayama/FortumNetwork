package solver;

import java.io.IOException;
import java.util.ArrayList;

import model.Node;
import model.SinkNode;
import model.SourceNode;
import tools.Utilities;
import tools.excelRead.LoadFullExcel;
import static tools.excelRead.LoadFullExcel.mapOfSourceNodes;
import static tools.excelRead.LoadFullExcel.mapOfSinkNodes;
import static tools.excelRead.LoadFullExcel.mapOfDummyNodes;
import static tools.excelRead.LoadFullExcel.mapOfLinks;

public class TraceBaseLoad
{

	public static final ArrayList<SinkNode> unConnectedSinkNodes = new ArrayList<SinkNode>();
	
	public static void main(String[] args) throws IOException
	{
		LoadFullExcel.main(args);
		report();
		traceSinkToSource();
	}

	private static void traceSinkToSource()
	{
		//Check on Sink Nodes
		for(final String sinkNodeName:  mapOfSinkNodes.keySet())
		{
			final SinkNode sinkNode = mapOfSinkNodes.get(sinkNodeName);
			if(unConnectedSinkNodes.contains(sinkNode))
			{
				continue;
			}
			else
			{
				System.out.print("BackTracing "+sinkNode +" >> ");
			}
			
			//This sink node should be back traceable till source
			Node onePreviousNode = Utilities.getOnePreviousNode(sinkNode);
			System.out.print(onePreviousNode +" >> ");
			int depth = 0;
			while(!(onePreviousNode instanceof SourceNode))
			{
				if(depth++ > 500)
				{
					System.err.println("Exiting for depth check");
					System.exit(0);
				}
				
				if(onePreviousNode == null)
				{
					unConnectedSinkNodes.add(sinkNode);
					System.out.println("\n"+sinkNodeName + "is also unconnected/orphan");
					break;					
				}
				else
				{
					onePreviousNode = Utilities.getOnePreviousNode(onePreviousNode);
					System.out.print(onePreviousNode +" >> ");					
				}
			}//WHile loop ends
			System.out.println("Traced "+sinkNode + " to "+onePreviousNode);
		}
	}

	private static void report()
	{
		//Check on source Nodes
		for(final String soureNodeName:  mapOfSourceNodes.keySet())
		{
			final SourceNode sourceNode = mapOfSourceNodes.get(soureNodeName);
			if(sourceNode.inComingLinks.size() != 0)
			{
				System.out.println("Why does sourceNode "+soureNodeName+" have incoming links?");
			}
			
			if(sourceNode.outGoingLinks.size() < 1)
			{
				System.out.println("Why does sourceNode "+soureNodeName+" not have outgoing links?");
			}
		}
		
		//Check on Sink Nodes
		for(final String sinkNodeName:  mapOfSinkNodes.keySet())
		{
			final SinkNode sinkNode = mapOfSinkNodes.get(sinkNodeName);
			if(sinkNode.inComingLinks.size() != 1)
			{
				unConnectedSinkNodes.add(sinkNode);
			}
			
			if(sinkNode.outGoingLinks.size() != 0)
			{
				System.out.println("Why does sinkNode "+sinkNodeName+" have outgoing links?");
			}
		}
		System.err.println("The following Sink Nodes do not have any incoming links: "+unConnectedSinkNodes);
	}

}
