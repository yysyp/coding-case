package ps.demo.commonlibx.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
        @PropertySource(value = "classpath:git", ignoreResourceNotFound = true)
})
//@ConfigurationProperties(prefix="git")
@Getter
@Setter
@ToString
public class GitInfoConfig {

    @Value("${git.build.time:-}")
    private String gitBuildTime;

    @Value("${git.build.version:-}")
    private String gitBuildVersion;

    @Value("${git.commit.id.abbrev:-}")
    private String gitCommitIdAbbrev;

    @Value("${git.commit.id.full:-}")
    private String gitCommitIdFull;


}
