package tools.excelRead;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.SinkNode;
import model.DummyNode;
import model.Link;
import model.Node;
import model.SourceNode;
import tools.Utilities;

public final class LoadFullExcel
{
	public static final HashMap<String, SourceNode> mapOfSourceNodes 			= new HashMap<String, SourceNode>();
	public static final HashMap<String, SinkNode> mapOfSinkNodes 				= new HashMap<String, SinkNode>();
	public static final HashMap<String, DummyNode> mapOfDummyNodes 				= new HashMap<String, DummyNode>();
	public static final HashMap<String, Link> mapOfLinks 						= new HashMap<String, Link>();
	
	public static final void loadExcelDataFile(final String fileName) throws IOException
	{
		final FileInputStream fis = new FileInputStream(new File(fileName));
		final Workbook workbook = new XSSFWorkbook(fis);
		
		loadSourceNodes(workbook);
		loadSinkNodes(workbook);
		loadDummyNodes(workbook);
		loadLinks(workbook);
		
		workbook.close();
		fis.close();
	}

	private static final void loadSourceNodes(final Workbook workbook)
	{
		final Sheet sheetSourceNodes = workbook.getSheetAt(0);
		boolean firstRowFlag = true;
		for(final Row row: sheetSourceNodes)
		{
			if(firstRowFlag)
			{
				firstRowFlag = false;
				continue;
			}
			final String nodeName = row.getCell(0).getStringCellValue();
			final int maxOutputRateKW = (int)row.getCell(2).getNumericCellValue();
			mapOfSourceNodes.put(nodeName , new SourceNode(nodeName, maxOutputRateKW));
		}
		
		System.out.println(mapOfSourceNodes);
	}
	
	private static final void loadSinkNodes(Workbook workbook)
	{
		final Sheet sheetDestinationNodes = workbook.getSheetAt(1);
		boolean firstRowFlag = true;
		int rowNum = 0;
		for(final Row row: sheetDestinationNodes)
		{
			if(firstRowFlag)
			{
				firstRowFlag = false;
				continue;
			}
			final String nodeName = row.getCell(0).getStringCellValue();
			final int baseDemandKW = (int)row.getCell(5).getNumericCellValue();
			mapOfSinkNodes.put(nodeName , new SinkNode(nodeName, baseDemandKW));
		}
		
		System.out.println(mapOfSinkNodes);
	}
	
	private static final void loadDummyNodes(final Workbook workbook)
	{
		final Sheet sheetAssortedNodes = workbook.getSheetAt(3);
		boolean firstRowFlag = true;
		for(final Row row: sheetAssortedNodes)
		{
			if(firstRowFlag)
			{
				firstRowFlag = false;
				continue;
			}
			final String nodeName = row.getCell(0).getStringCellValue();
			if(mapOfSourceNodes.containsKey(nodeName) || mapOfSinkNodes.containsKey(nodeName) )
			{
				continue;
			}
			else
			{
				mapOfDummyNodes.put(nodeName , new DummyNode(nodeName));				
			}
		}//for loop of all rows ends
	
		System.out.println(mapOfDummyNodes);
		
	}//method loadDummyNodes() ends
	
	private static final void loadLinks(final Workbook workbook)
	{
		final Sheet sheetSourceNodes = workbook.getSheetAt(2);
		boolean firstRowFlag = true;
		for(final Row row: sheetSourceNodes)
		{
			if(firstRowFlag)
			{
				firstRowFlag = false;
				continue;
			}
			final String linkName = row.getCell(0).getStringCellValue();
			final String startNodeName = row.getCell(1).getStringCellValue();
			final String endNodeName = row.getCell(2).getStringCellValue();

			final Node startNode =  getNodeByName(startNodeName);
			final Node endNode =  getNodeByName(endNodeName);
			
			if(startNode instanceof SinkNode || endNode instanceof SourceNode)
			{
				System.out.println("Skipping link "+linkName+" because we are not tracking return links currently.");
				continue;
			}
			
			if(startNode == null || endNode == null || startNode == endNode)
			{
				System.err.println("Error in link:"+linkName);
				System.exit(0);
			}
			
			final double length = row.getCell(3).getNumericCellValue();
			
			final double heatCoefficient = row.getCell(4).getNumericCellValue();
			final double diameter = row.getCell(5).getNumericCellValue();
			final double capacityKW = row.getCell(8).getNumericCellValue();
			final double capacitykgps = row.getCell(9).getNumericCellValue();
			
			final Link link = new Link(linkName, startNode, endNode, length, heatCoefficient, diameter, capacityKW, capacitykgps);
			mapOfLinks.put(linkName, link);
			startNode.outGoingLinks.add(link);
			endNode.inComingLinks.add(link);
		}
	}
	
	public static final Node getNodeByName(final String name)
	{
		Node node = mapOfSourceNodes.get(name);
		if(node == null)
			node = mapOfSinkNodes.get(name);
		if(node == null)
			node = mapOfDummyNodes.get(name);
		if(node == null)
			System.err.println("No such node by name:"+name);
		return node;
	}
	
	public static final void main(String[] args) throws IOException
	{
		if(Utilities.isFileNameArgumentOK(args))
			loadExcelDataFile(args[0]);
	}
}
