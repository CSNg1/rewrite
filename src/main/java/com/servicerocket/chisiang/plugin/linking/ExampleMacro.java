package com.servicerocket.chisiang.plugin.linking;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.XhtmlException;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.confluence.xhtml.api.MacroDefinitionHandler;
import com.atlassian.confluence.xhtml.api.XhtmlContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author by CSNg on 15/01/2016.
 * @since 1.0.0.20160115
 */
public class ExampleMacro implements Macro{

    private final XhtmlContent xhtmlUtils;

    public ExampleMacro(XhtmlContent xhtmlUtils) {
        this.xhtmlUtils = xhtmlUtils;
    }

    public String execute(Map<String, String> parameters, String bodyContent, ConversionContext conversionContext) throws MacroExecutionException {
        String body = conversionContext.getEntity().getBodyAsString();

        final List<MacroDefinition> macros = new ArrayList<MacroDefinition>();

        try {
            xhtmlUtils.handleMacroDefinitions(body, conversionContext, new MacroDefinitionHandler() {
                public void handle(MacroDefinition macroDefinition) {
                    macros.add(macroDefinition);
                }
            });
        } catch (XhtmlException e) {
            throw new MacroExecutionException(e);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<p>");
        if (!macros.isEmpty()) {
            builder.append("<table width=\"50%\">");
            builder.append("<tr><th>Macro Name</th><th>Has Body?</th></tr>");
            for (MacroDefinition defn : macros)
            {
                builder.append("<tr>");
                builder.append("<td>").append(defn.getName()).append("</td><td>").append(defn.hasBody()).append("</td>");
                builder.append("</tr>");
            }
            builder.append("</table>");
        } else {
            builder.append("You've done built yourself a macro! Nice work.");
        }
        builder.append("<p>");

        return builder.toString();
    }

    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
