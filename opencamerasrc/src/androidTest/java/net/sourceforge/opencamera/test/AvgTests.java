package net.sourceforge.opencamera.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AvgTests {
    /** Tests for Avg algorithm - only need to run on a single device
     *  Should manually look over the images dumped onto DCIM/
     *  To use these tests, the testdata/ subfolder should be manually copied to the test device in the DCIM/testOpenCamera/
     *  folder (so you have DCIM/testOpenCamera/testdata/). We don't use assets/ as we'd end up with huge APK sizes which takes
     *  time to transfer to the device every time we run the tests.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MainTests.class.getName());
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg1"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg3"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg4"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg5"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg6"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg7"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg8"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg9"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg10"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg11"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg12"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg13"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg14"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg15"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg16"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg17"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg18"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg19"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg20"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg21"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg22"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg23"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg24"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg25"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg26"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg27"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg28"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg29"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg30"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg31"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg32"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg33"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg34"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg35"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg36"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg37"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg38"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg39"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg40"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg41"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg42"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg43"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg44"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg45"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg46"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg47"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg48"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg49"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg50"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg51"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAvg52"));
        return suite;
    }
}
