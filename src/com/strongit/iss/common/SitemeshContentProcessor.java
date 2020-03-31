package com.strongit.iss.common;

import java.io.IOException;
import java.nio.CharBuffer;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.Content;
import org.sitemesh.content.tagrules.TagBasedContentProcessor;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.decorate.DecoratorTagRuleBundle;
import org.sitemesh.content.tagrules.html.CoreHtmlTagRuleBundle;
import org.sitemesh.content.tagrules.html.DivExtractingTagRuleBundle;
import org.sitemesh.webapp.WebAppContext;

/**
 * Created by William on 2014/10/14.
 */
public class SitemeshContentProcessor extends TagBasedContentProcessor {

    public SitemeshContentProcessor(){
        this(new CoreHtmlTagRuleBundle(), new DecoratorTagRuleBundle(), new DivExtractingTagRuleBundle());
    }

    public SitemeshContentProcessor(TagRuleBundle... tagRuleBundles) {
        super(tagRuleBundles);
    }

    @Override
    public Content build(CharBuffer data, SiteMeshContext siteMeshContext) throws IOException {

        WebAppContext appContext = (WebAppContext)siteMeshContext;
        if(appContext.getRequest().getAttribute("exception") != null){
            return null;
        }
        
        String view_uri = String.valueOf(appContext.getRequest().getAttribute("struts.view_uri"));
        if("/common/error/error.jsp".equals(view_uri)){
            return null;
        }        

        if(appContext.getRequest().getHeader("X-Requested-With") != null){
            if(appContext.getRequest().getHeader("X-Requested-With").equals("XMLHttpRequest")){
                //"XMLHttpRequest".equals(request.getHeader("X-Requested-With"));)
                return null;
            }
        }


        return super.build(data, siteMeshContext);
    }
}
