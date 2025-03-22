import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportDir = "test-output";
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs(); // ✅ create test-output if missing
            }

            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportDir + "/ExtentReport.html");
            htmlReporter.config().setReportName("Sauce Demo Test Report");
            htmlReporter.config().setDocumentTitle("Automation Report");

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo("Tester", "Sravya");
            extent.setSystemInfo("Environment", "Jenkins Grid");
        }
        return extent;
    }
}
