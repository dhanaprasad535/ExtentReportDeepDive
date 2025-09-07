package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentManager implements ITestListener {

    private static final ExtentReports extentReports = new ExtentReports();
    private static final ThreadLocal<ExtentTest> tLocal = new ThreadLocal<>();
    private static final Map<String, ExtentTest> parent = new ConcurrentHashMap<>();

    protected static ExtentTest logger;

    @Override
    public void onStart(ITestContext iTestContext) {
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/target/log.html");
        extentReports.attachReporter(extentSparkReporter);
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String testGroup = iTestResult.getTestContext().getName();
        String testCaseName = iTestResult.getMethod().getMethodName();
        ExtentTest extentTest = parent.computeIfAbsent(testGroup , name -> extentReports.createTest("Test Group: "+ name));
        ExtentTest child = extentTest.createNode("TestCase: " + testCaseName);
        tLocal.set(child);
        logger = tLocal.get();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        tLocal.get().log(Status.INFO, "Test case "+ result.getMethod().getMethodName() + " succeeded");
        tLocal.get().log(Status.INFO, "Test execution time " + getMilliSeconds(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        tLocal.get().log(Status.FAIL, "Test case "+ result.getMethod().getMethodName() + " failed");
        tLocal.get().fail(result.getThrowable());
        tLocal.get().log(Status.INFO, "Test execution time " + getMilliSeconds(result));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        extentReports.flush();
        tLocal.remove();
    }

    public long getMilliSeconds(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
