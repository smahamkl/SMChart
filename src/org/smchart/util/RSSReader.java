package org.smchart.util;

import java.net.URL;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RSSReader
{
  private static RSSReader instance = null;
  private String ticker;
  private static String rssURL = "http://finance.yahoo.com/rss/headline?s=%s";
  private boolean m_isProxy = false;
  private String m_proxyHost = "";
  private String m_proxyPort = "";

  public RSSReader(String inticker) {
    this.ticker = inticker;
  }
  public RSSReader(String inticker, String inHost, String inPort) {
    this.ticker = inticker;
    this.m_proxyHost = inHost;
    this.m_proxyPort = inPort;
    this.m_isProxy = true;
  }

  public String getNews() {
    StringBuilder sb = null;
    try {
      sb = new StringBuilder();
      sb.append("<table border='1'>");
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      if (this.m_isProxy) {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyPort", this.m_proxyPort);
        System.getProperties().put("proxyHost", this.m_proxyHost);
      }
      URL u = new URL(String.format(rssURL, new Object[] { this.ticker }));

      Document doc = builder.parse(u.openStream());

      NodeList nodes = doc.getElementsByTagName("item");

      for (int i = 0; i < nodes.getLength(); i++)
      {
        Element element = (Element)nodes.item(i);
        sb.append("<tr>");
        sb.append("<td><a href='" + getElementValue(element, "link") + "'><b>" + getElementValue(element, "title") + "</b></a></td>");
        sb.append("<td>Date: " + getElementValue(element, "pubDate") + "</td>");
        sb.append("</tr>");
      }

      sb.append("</table>");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return sb.toString();
  }

  private String getCharacterDataFromElement(Element e)
  {
    try {
      Node child = e.getFirstChild();
      if ((child instanceof CharacterData)) {
        CharacterData cd = (CharacterData)child;
        return cd.getData();
      }
    } catch (Exception ex) {
    }
    return "";
  }

  protected float getFloat(String value) {
    if ((value != null) && (!value.equals(""))) {
      return Float.parseFloat(value);
    }
    return 0.0F;
  }

  protected String getElementValue(Element parent, String label) {
    return getCharacterDataFromElement((Element)parent.getElementsByTagName(label).item(0));
  }
}