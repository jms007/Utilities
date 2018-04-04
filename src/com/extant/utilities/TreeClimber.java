package com.extant.utilities;

import java.io.File;
import java.io.IOException;
//import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author jms
 */
public class TreeClimber implements ErrorHandler
{
	public TreeClimber()
	{
		level = 0;
	}

	public void exploreTree(Document document)
	{
		Node root = document.getDocumentElement();
		exploreBranch(root);
	}

	public void exploreBranch(Node node)
	{
		processNode(level, node);
		Node nextNode = node.getFirstChild();
		while (nextNode != null)
		{
			++level;
			exploreBranch(nextNode);
			--level;
			nextNode = nextNode.getNextSibling();
		}
	}

	/* Produces a String representation of one node and prints to System.out */
	public void processNode(int level, Node node)
	{
		String s = "[" + Integer.toString(level) + "] " + node.getNodeName();
		if (node instanceof Element)
		{
			s += " " + ((Element) node).getAttribute("title") + ": ";
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0; i < attributes.getLength(); ++i)
			{
				Node attNode = attributes.item(i);
				if (i > 0)
					s += ",";
				s += attNode.getNodeName() + "=" + attNode.getNodeValue();
			}
		} else
			s += node.getNodeName() + ": " + node.getNodeType();
		System.out.println(s);
	}

	public Document buildDocument(String sourceFilename, LogFile logger, boolean validating) throws UtilitiesException
	{
		return buildDocument(sourceFilename, logger, validating, true, true);
	}

	public Document buildDocument(String sourceFilename, LogFile logger, boolean validating, boolean ignoreComments,
			boolean ignoreElementWhitespace) throws UtilitiesException
	{
		try
		{
			if (!validating & ignoreElementWhitespace)
				throw new ParserConfigurationException("Inconsistent parser parameters.");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(validating);
			factory.setNamespaceAware(false);
			factory.setIgnoringComments(ignoreComments);
			factory.setIgnoringElementContentWhitespace(ignoreElementWhitespace);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(this); // this is why it can't be static
			Document document = builder.parse(new File(sourceFilename));
			// document.normalizeDocument(); // not sure what this does, if anything
			return document;
		} catch (SAXException sxe)
		{ // Error generated during parsing
			throw new UtilitiesException(UtilitiesException.PARSE_ERROR, sxe.getMessage());
		} catch (ParserConfigurationException pcx)
		{ // Parser with specified options can't be built
			// pcx.printStackTrace();
			throw new UtilitiesException(UtilitiesException.PARSE_ERROR, pcx.getMessage());
		} catch (IOException iox)
		{
			throw new UtilitiesException(UtilitiesException.IOEXCEPTION, iox.getMessage());
		}
	}

	public void warning(SAXParseException exception) throws SAXException
	{
		throw new SAXException("Parse Warning Line " + exception.getLineNumber() + ": " + exception.getMessage());
	}

	public void error(SAXParseException exception) throws SAXException
	{
		throw new SAXException("Parse Error Line " + exception.getLineNumber() + ": " + exception.getMessage());
	}

	public void fatalError(SAXParseException exception) throws SAXException
	{
		throw new SAXException("Parse Fatal Error Line " + exception.getLineNumber() + ": " + exception.getMessage());
	}

	// Here's the deal on dates:
	// We access the gl transactions only once, at Chart.init()
	// At this time, we change the date field of transactions as follows:
	// if the date ends with "00", replace the entire field with "BEFORE"
	// if the date ends with "32", replace the entire field with "AFTER"
	// In Julian, these dates are set to "1-1-1000" and "1-1-3000" respectively.
	//
	// The balance-getters for account elements are defined as follows.
	// Note that entries dated AFTER are never included in any period.
	// getBeginBal( element, beginDate )
	// will include all entries with date <= beginDate, including BEFORE
	// if beginDate is null, this will include all entries except AFTER
	// getEndBal( element, endDate )
	// will include all entries with date <= endDate, including BEFORE
	// if endDate is null, this will include all entries except AFTER
	// getDeltaBal( element, beginDate, endDate )
	// will include all entries with beginDate <= date <= endDate
	// if beginDate is null, this will include BEFORE entries
	// if endDate is null, this will include all entries except AFTER
	//
	// After this has been tested, the plan is to change all GL0010.DAT files,
	// replacing
	// the phony dates with BEFORE and AFTER as described above. Then we can remove
	// those
	// replacements from this method.

	// !! The extracted glEntries should be accumulated in a Map <String,GLEntry>
	// where the key is (accountNo,date yymmdd). There will be one entry in this map
	// for each entry in the G/L. <--This is not true, and that's why this will not
	// work.
	// Then sort the keys, and addToTree() can just process the map entries in
	// sorted order,
	// appending the glentry elements to the appropriate account and doing
	// propogates at
	// each accountNo break.
	// This will
	// - allow the chart tree to present the transactions in date order
	// - eliminate one more connection to Account's
	// - eliminate the requirement for this class to extend TreeClimber

	public static void main(String args[])
	{

		if (args.length == 0)
		{
			throw new RuntimeException("Please provide XML file argument");
		}
		String xmlFile = args[0];
		try
		{
			TreeClimber treeClimber = new TreeClimber();

			// Climb a tree with default processing
			LogFile logger = new LogFile("G:\\ACCOUNTING\\TreeClimber.log");
			logger.log("new LogFile by TreeClimber " + new Julian().toString("mm-dd-yyyy hh:mm:ss"));
			// logger.setLogLevel(LogFile.DEBUG_LOG_LEVEL);
			String sourceFilename = Console.prompt("Enter sourceFilename: ", xmlFile);
			// String sourceFilename = "G:\\ACCOUNTING\\JMS\\GL06\\CHART.xml";
			// String sourceFilename = "C:\\Users\\jms\\ACCOUNTING\\JMSCT\\CHART.XML";
			Document document = new TreeClimber().buildDocument(sourceFilename, logger, true, true, true);
			if (document == null)
				System.exit(1);
			treeClimber.exploreTree(document);
		} catch (UtilitiesException vlx)
		{
			Console.println(vlx.getMessage());
		}
	}

	// An array of names for DOM node-types
	// (Array indexes = nodeType() values.)
	static final String[] typeName = { "none", "Element", "Attr", "Text", "CDATA", "EntityRef", "Entity", "ProcInstr",
			"Comment", "Document", "DocType", "DocFragment", "Notation", };

	int level = 0;
}
