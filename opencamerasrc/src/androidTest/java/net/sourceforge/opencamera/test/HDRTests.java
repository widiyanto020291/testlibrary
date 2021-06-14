package net.sourceforge.opencamera.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class HDRTests {
    /** Tests for HDR algorithm - only need to run on a single device
     *  Should manually look over the images dumped onto DCIM/
     *  To use these tests, the testdata/ subfolder should be manually copied to the test device in the DCIM/testOpenCamera/
     *  folder (so you have DCIM/testOpenCamera/testdata/). We don't use assets/ as we'd end up with huge APK sizes which takes
     *  time to transfer to the device every time we run the tests.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MainTests.class.getName());
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testDROZero"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testDRODark0"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testDRODark1"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR1"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR3"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR4"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR5"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR6"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR7"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR8"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR9"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR10"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR11"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR12"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR13"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR14"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR15"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR16"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR17"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR18"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR19"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR20"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR21"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR22"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR23"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR24"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR25"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR26"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR27"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR28"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR29"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR30"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR31"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR32"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR33"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR34"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR35"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR36"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR37"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR38Filmic"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR39"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR40"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR40Exponential"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR40Filmic"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR41"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR42"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR43"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR44"));
        // don't include testHDR45, this is tested as part of HDRNTests
        // don't include testHDR46, this is tested as part of HDRNTests
        // don't include testHDR47, this is tested as part of HDRNTests
        // don't include testHDR48, this is tested as part of HDRNTests
        // don't include testHDR49, this is tested as part of HDRNTests
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR50"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR51"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR52"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR53"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR54"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR55"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR56"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDR57"));
        return suite;
    }
}
