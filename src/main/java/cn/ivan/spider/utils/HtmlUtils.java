package cn.ivan.spider.utils;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.sun.javadoc.Tag;
/**
 * html页面的utils类
 * @author Hades
 *
 */
public class HtmlUtils {
	/**
	 * 获取指定的标签node
	 * @param tagNode
	 * @param xPath
	 * @return
	 */
	public static TagNode getTagNodeByXpath(TagNode tagNode,String xPath){
		Object[] evaluateXPath;
		TagNode node = null;
		try {
			evaluateXPath = tagNode.evaluateXPath(xPath);
			if(evaluateXPath!=null && evaluateXPath.length>0){
				node = (TagNode)evaluateXPath[0];
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		
		return node;
	}
	/**
	 * 获取指定标签的值
	 * @param tagNode
	 * @param xPath
	 * @return
	 */
	public static String getText(TagNode tagNode,String xPath){
		return getTagNodeByXpath(tagNode, xPath).getText().toString();
	}
	/**
	 * 获取指定标签指定属性的值
	 * @param tagNode
	 * @param Xpath
	 * @param attr
	 * @return
	 */
	public static String getAttributeByAttr(TagNode tagNode,String xPath,String attr){
		TagNode node = getTagNodeByXpath(tagNode, xPath);
		return node.getAttributeByName(attr);
	}
}
