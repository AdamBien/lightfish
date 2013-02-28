package org.lightfish.business.escalation.control;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.lightfish.business.escalation.entity.Escalation;
import org.lightfish.business.monitoring.entity.Snapshot;
import org.pegdown.PegDownProcessor;

/**
 *
 * @author rveldpau
 */
public class EscalationMessageProcessor {

    @Inject
    Logger LOG;
    private static final String defaultMessage = "An escalation has occured.";

    public String processBasicMessage(String template, Snapshot snapshot) {
        if (template == null || template.trim().isEmpty()) {
            return defaultMessage;
        }
        return replaceVariables(template, snapshot);
    }

    public String processRichMessage(String template, Snapshot snapshot) {
        if (template == null || template.trim().isEmpty()) {
            return defaultMessage;
        }
        String markdownText = replaceVariables(template, snapshot);
        return processMarkdown(markdownText);
    }

    private String replaceVariables(String template, Snapshot snapshot) {
        StringBuffer buffer = new StringBuffer();

        Pattern variablePattern = Pattern.compile("\\$\\{([A-Za-z0-9\\.\\[\\]]+)\\}");
        Matcher variableMatcher = variablePattern.matcher(template);

        if (variableMatcher.find()) {
            do {
                String memberName = variableMatcher.group(1);
                Object value = pullValueFromEscalation(memberName, snapshot);
                if (value != null) {
                    variableMatcher.appendReplacement(buffer, value.toString());
                }else{
                    variableMatcher.appendReplacement(buffer, memberName);
                }
                
            } while (variableMatcher.find());
            return buffer.toString();
        } else {
            return template;
        }
    }

    private Object pullValueFromEscalation(String memberName, Snapshot snapshot) {
        try {
            BeanUtilsBean2 beanUtil = new BeanUtilsBean2();
            return beanUtil.getNestedProperty(snapshot, memberName);
        } catch (IllegalAccessException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        } catch (InvocationTargetException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
        return null;
    }
    
    private String processMarkdown(String text){
        return new PegDownProcessor().markdownToHtml(text);
    }
}
