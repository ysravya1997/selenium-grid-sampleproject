import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportDir = System.getProperty("user.dir") + File.separator + "test-output";
            String reportPath = reportDir + File.separator + "ExtentReport.html";

            System.out.println("✅ Extent report path: " + reportPath);

            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs(); // ensure directory exists
            }

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("Automation Report");
            spark.config().setReportName("Sauce Demo Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Tester", "Sravya");
            extent.setSystemInfo("Environment", "Jenkins / Local");

            // Optional shutdown hook to ensure flushing
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (extent != null) {
                    extent.flush();
                    System.out.println("✅ Extent report flushed via shutdown hook");
                }
            }));
        }
        return extent;
    }
}
