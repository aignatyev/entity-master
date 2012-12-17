import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Fox
 * Date: 17.12.12
 * Time: 0:31
 * To change this template use File | Settings | File Templates.
 */
public class TestContext {
    @Test
    public void testContext() {
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("dao/src/main/resources/beans.xml");
        applicationContext.getBean("clientService");
    }
}
