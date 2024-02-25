package dev.enricosola.yummy.support;

import org.springframework.web.servlet.support.RequestContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import java.util.Map;

public class FlashMessagesInjector {
    private final Map<String, String> lastActionMessagesMapping;

    public FlashMessagesInjector(Map<String, String> lastActionMessagesMapping){
        this.lastActionMessagesMapping = lastActionMessagesMapping;
    }

    public void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);
        String lastActionMessage = null, generalErrorMessage = null;
        boolean hasFlashMessages = false;
        if ( inputFlashMap != null ){
            if ( inputFlashMap.containsKey("generalErrorMessage") ){
                generalErrorMessage = inputFlashMap.get("generalErrorMessage").toString();
                hasFlashMessages = !generalErrorMessage.isBlank();
            }
            if ( inputFlashMap.containsKey("lastAction") ){
                String lastAction = inputFlashMap.get("lastAction").toString();
                for ( Map.Entry<String, String> entry : this.lastActionMessagesMapping.entrySet() ){
                    if ( lastAction.equals(entry.getKey()) ){
                        lastActionMessage = entry.getValue();
                        break;
                    }
                }
                if ( lastActionMessage != null && !lastActionMessage.isBlank() ){
                    hasFlashMessages = true;
                }
            }
        }
        model.addAttribute("generalErrorMessage", generalErrorMessage);
        model.addAttribute("lastActionMessage", lastActionMessage);
        model.addAttribute("hasFlashMessages", hasFlashMessages);
    }
}
