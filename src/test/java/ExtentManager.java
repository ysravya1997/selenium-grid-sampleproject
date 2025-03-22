import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            // Ensure Jenkins-compatible folder path
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            File reportDir = new File(System.getProperty("user.dir") + "/test-output");
            if (!reportDir.exists()) {
                reportDir.mkdirs(); // ✅ Create it if missing
            }

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("Automation Report");
            spark.config().setReportName("SauceDemo Selenium Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", "Jenkins");
            extent.setSystemInfo("Executed By", "Sravya");
        }
        return extent;
    }
}
