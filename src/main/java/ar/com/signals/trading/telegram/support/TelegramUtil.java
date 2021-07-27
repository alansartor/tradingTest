package ar.com.signals.trading.telegram.support;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class TelegramUtil {

	/**
	 * parse_medo=HTML
	 * Telegram solo acepta los siguientes tags:
	 * <b>bold</b>, <strong>bold</strong>
	 * <i>italic</i>, <em>italic</em>
	 * <u>underline</u>, <ins>underline</ins>
	 * <s>strikethrough</s>, <strike>strikethrough</strike>, <del>strikethrough</del>
	 * <b>bold <i>italic bold <s>italic bold strikethrough</s> <u>underline italic bold</u></i> bold</b>
	 * <a href="http://www.example.com/">inline URL</a>
	 * <a href="tg://user?id=123456789">inline mention of a user</a>
	 * <code>inline fixed-width code</code>
	 * <pre>pre-formatted fixed-width code block</pre>
	 * <pre><code class="language-python">pre-formatted fixed-width code block written in the Python programming language</code></pre>
	 * 
	 * Si se manda otro tag telegram arroja un error
	 * @param observaciones
	 * @return
	 */
	public static String limpiarHtmlText(String htmlText) {
		if(StringUtils.isBlank(htmlText)) {
			return htmlText;
		}
		//telegram usa \n y no <br>, entonces reemplazamos
		htmlText = htmlText.replaceAll("<br>", "<pre>\n</pre>");
		
		Document doc = Jsoup.parse(htmlText, "", Parser.xmlParser());
        //select all elements
        Elements elements = doc.select("*");
        //iterate elements using enhanced for loop
        for (Iterator<Element> iterator = elements.iterator(); iterator.hasNext();) {
        	Element tag = iterator.next();
        	if(!"#root".equalsIgnoreCase(tag.tagName())//este tag viene cuando el inicio del html no es un tag!
        			&& !"b".equalsIgnoreCase(tag.tagName())
        			&& !"strong".equalsIgnoreCase(tag.tagName())
        			&& !"i".equalsIgnoreCase(tag.tagName())
        			&& !"em".equalsIgnoreCase(tag.tagName())
        			&& !"u".equalsIgnoreCase(tag.tagName())
        			&& !"ins".equalsIgnoreCase(tag.tagName())
        			&& !"s".equalsIgnoreCase(tag.tagName())
        			&& !"strike".equalsIgnoreCase(tag.tagName())
        			&& !"del".equalsIgnoreCase(tag.tagName())
        			&& !"a".equalsIgnoreCase(tag.tagName())
        			&& !"code".equalsIgnoreCase(tag.tagName())
        			&& !"pre".equalsIgnoreCase(tag.tagName())) {
        		
        		if(tag.parent() != null) {
        			Element parent = tag.parent();
        			List<Node> allNodes = parent.childNodesCopy();//necesito una copia para que no se produzca error
        			tag.remove();//IMPORTANTE si elimino por ejemplo un tag dentro de <a>some_tag + texto<a/> entonces tambien se elimina el texto del link, por lo que cambie por replaceWith
        			for (Node node : allNodes) {
        				if(node instanceof TextNode) {
        					parent.appendChild(node);
        				}
					}
        		}else {
        			tag.remove();//IMPORTANTE si elimino por ejemplo un tag dentro de <a>some_tag + texto<a/> entonces tambien se elimina el texto del link, por lo que cambie por replaceWith
            		
        		}
			}else if("a".equalsIgnoreCase(tag.tagName())) {
				//IMPORTANTE: si el link esta caido, el mensaje de telegram solo muestra el texto pero sin link, por eso en test no anda
				tag.text(tag.text().replace('\'', '"'));//el link tiene que estar entre comillas doble
			}
		}
		return doc.toString();
	}

}
