package ps.demo.zglj.config;

import brave.Tracer;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TraceIdContext {
    private static Tracer staticTracer;
    @Autowired
    private Tracer instanceTracer;

    @PostConstruct
    public void init() {
        staticTracer = instanceTracer;
    }

    public static String getCurrentTraceId() {
        String mdcId = MDC.get("traceId");
        if (StringUtils.isNotBlank(mdcId)) {
            return mdcId;
        }

        if (staticTracer != null && staticTracer.currentSpan() != null
        && staticTracer.currentSpan().context() != null) {
            return staticTracer.currentSpan().context().traceIdString();
        }
        return "no-trace-id";

    }


}
