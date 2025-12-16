package ps.demo.zglj.cucumber.base;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ScenarioScope
public class ScenarioContext {
    private Map<String, Object> scenarioData = new HashMap<>();
    
    public void setContext(String key, Object value) {
        scenarioData.put(key, value);
    }
    
    public Object getContext(String key) {
        return scenarioData.get(key);
    }

    public String getContextAsString(String key) {
        Object value = scenarioData.get(key);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }
    
    public Boolean isContaining(String key) {
        return scenarioData.containsKey(key);
    }
}
