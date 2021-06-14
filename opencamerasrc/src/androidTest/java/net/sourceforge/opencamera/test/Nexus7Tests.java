package net.sourceforge.opencamera.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class Nexus7Tests {
    // Tests to run specifically on Nexus 7
    public static Test suite() {
        TestSuite suite = new TestSuite(MainTests.class.getName());

        // we run the following tests on the Nexus 7 as a device that supports SAF, but doesn't have Android 7+ (where we use alternative methods for read/writing Exif tags without needing File)
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoSAF"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPhotoStampSAF"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testDirectionOnSAF"));

        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSwitchVideo"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFocusFlashAvailability"));

        return suite;
    }
}
