package ps.demo.lqbs.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
public class CrudDevTest extends CrudBaseTest {

    @BeforeAll
    static void beforeAll() {
        log.info("overwrite beforeAll dev");
    }

}
