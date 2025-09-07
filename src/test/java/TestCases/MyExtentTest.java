package TestCases;

import com.aventstack.extentreports.Status;
import org.testng.annotations.Test;

public class MyExtentTest extends TestTemplate {

    @Test
    public void m1() {
        logger.log(Status.INFO, "This is a test method-m1");
    }

    @Test
    public void m2() {
        logger.log(Status.INFO, "This is a test method-m2");
    }

}