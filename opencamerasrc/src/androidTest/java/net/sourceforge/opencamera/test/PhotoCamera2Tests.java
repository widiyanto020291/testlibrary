package net.sourceforge.opencamera.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PhotoCamera2Tests {
    // Tests related to taking photos that require Camera2 - only need to run this suite with Camera2
    public static Test suite() {
        TestSuite suite = new TestSuite(MainTests.class.getName());
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAutoFocusReleaseDuringPhoto"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoManualFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoManualISOExposure"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoManualWB"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRaw"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawWaitCaptureResult"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawMulti"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawOnly"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawExpo"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawExpoWaitCaptureResult"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawOnlyExpo"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedTrashRaw"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedTrashRaw2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoExpo5"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoHDRSaveExpoRaw"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoHDRSaveExpoRawOnly"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFocusBracketing"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFocusBracketingHeavy"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFocusBracketingCancel"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawFocusBracketing"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawOnlyFocusBracketing"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFastBurst"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoContinuousBurst"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoContinuousBurstSlow"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoNR"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFlashAutoFakeMode"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFlashOnFakeMode"));
        // do testTakePhotoRawRepeat last, and is an intensive test, and if it fails for any reason it seems to cause the following test to crash, terminating the run (at least on Nexus 6)!
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRawRepeat"));
        return suite;
    }
}
