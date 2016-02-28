package com.budivnictvo.rssnews.app.html;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Администратор on 05.01.2015.
 */
public class HtmlHelper {
    TagNode rootNode;

    public HtmlHelper(StringReader reader) throws IOException{

        HtmlCleaner cleaner = new HtmlCleaner();
        rootNode = cleaner.clean(reader);
        String str = cleaner.getProperties().getCharset();
}
public List<TagNode> getLinksByTag(String _tag){
        List<TagNode> linkList = new ArrayList<TagNode>();
        TagNode[] linkElements = rootNode.getElementsByName(_tag ,true);
        for (int i = 0 ; i <linkElements.length ; i++ ){
        linkList.add(linkElements[i]);
        }
        return  linkList;
        }

        }
