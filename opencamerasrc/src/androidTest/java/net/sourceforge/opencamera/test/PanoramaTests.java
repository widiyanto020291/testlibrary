package net.sourceforge.opencamera.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PanoramaTests {
    /** Tests for Panorama algorithm - only need to run on a single device
     *  Should manually look over the images dumped onto DCIM/
     *  To use these tests, the testdata/ subfolder should be manually copied to the test device in the DCIM/testOpenCamera/
     *  folder (so you have DCIM/testOpenCamera/testdata/). We don't use assets/ as we'd end up with huge APK sizes which takes
     *  time to transfer to the device every time we run the tests.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MainTests.class.getName());
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanoramaWhite"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama1"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama3"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama3_picsperscreen2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama4"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama5"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama6"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama7"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama8"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama9"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama10"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama11"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama12"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama13"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama14"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama15"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama16"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama17"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama18"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama19"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama20"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama21"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama22"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama23"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama24"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama25"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama26"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama27"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama28"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama28_galaxys10e"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama29"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama30"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama30_galaxys10e"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama31"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama32"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama33"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama34"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama35"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama36"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama37"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPanorama38"));
        return suite;
    }
}
