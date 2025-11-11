package ps.demo.jpademo.cucumber.base;

import java.util.HashMap;
import java.util.Map;

// 创建上下文管理类
public class ScenarioContext {
    private Map<String, Object> scenarioData = new HashMap<>();
    
    public void setContext(String key, Object value) {
        scenarioData.put(key, value);
    }
    
    public Object getContext(String key) {
        return scenarioData.get(key);
    }
    
    public Boolean isContaining(String key) {
        return scenarioData.containsKey(key);
    }
}
