package com.sainsburys.technicalTest.helper;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

/**
 * Traverse through the document and process the elements to retrieve text.
 * 
 * @author Manoj.Dhore
 *
 */
public class FormattingVisitor implements NodeVisitor {

	private static final int maxWidth = 80;
    private int width = 0;
    private StringBuilder accum = new StringBuilder(); // holds the accumulated text

    // hit when the node is first seen
    @Override
    public void head(Node node, int depth) {
        if (node instanceof TextNode)
            append(((TextNode) node).text()); // TextNodes carry all user-readable text in the DOM.
       
    }

    // hit when all of the node's children (if any) have been visited
    @Override
    public void tail(Node node, int depth) {
        String name = node.nodeName();
        
        if (name.equals("a") && !(name.endsWith("tabsContainer")))
            append(String.format(" %s", node.absUrl("href")));
    }

    // appends text to the string builder with a simple word wrap method
    private void append(String text) {
         // reset counter if starts with a newline. only from formats above, not in natural text
        if (text.equals(" ") &&
                (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
            return; // don't accumulate long runs of empty spaces

        if (text.length() + width > maxWidth) { // won't fit, needs to wrap
            String words[] = text.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                boolean last = i == words.length - 1;
                if (!last) // insert a space if not the last word
                    word = word + " ";
                if (word.length() + width > maxWidth) { // wrap and reset counter
                    accum.append("\n").append(word);
                    width = word.length();
                } else {
                    accum.append(word);
                    width += word.length();
                }
            }
        } else { // fits as is, without need to wrap text
            accum.append(text);
            width += text.length();
        }
    }

    @Override
    public String toString() {
        return accum.toString();
    }

}
