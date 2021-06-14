package net.sourceforge.opencamera.test;

import android.os.Build;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MainTests {
    // Tests that don't fit into another of the Test suites
    public static Test suite() {
        /*return new TestSuiteBuilder(AllTests.class)
        .includeAllPackagesUnderHere()
        .build();*/
        TestSuite suite = new TestSuite(MainTests.class.getName());
        // put these tests first as they require various permissions be allowed, that can only be set by user action
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSwitchVideo"));
        // other tests:
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPause"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testImmediatelyQuit"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testStartCameraPreviewCount"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCamera2PrefUpgrade"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSaveModes"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFlashVideoMode"));
            //suite.addTest(TestSuite.createTest(MainActivityTest.class, "testSaveFlashTorchSwitchCamera"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFlashStartup"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFlashStartup2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testHDRRestart"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPreviewSize"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPreviewSizeWYSIWYG"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testResolutionMaxMP"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testResolutionBurst"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAutoFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAutoFocusCorners"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPopup"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPopupLeftLayout"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testRightLayout"));
        //suite.addTest(TestSuite.createTest(MainActivityTest.class, "testPopupLayout")); // don't autotest for now, see comments for the test
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSwitchResolution"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFaceDetection"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFocusFlashAvailability"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFocusSwitchVideoSwitchCameras"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFocusRemainMacroSwitchCamera"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFocusRemainMacroSwitchPhoto"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFocusSaveMacroSwitchPhoto"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFocusSwitchVideoResetContinuous"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousPictureFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousPictureRepeatTouch"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousPictureSwitchAuto"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousVideoFocusForPhoto"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testStartupAutoFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSaveQuality"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testZoom"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testZoomIdle"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testZoomSwitchCamera"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSwitchCameraIdle"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSwitchCameraRepeat"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTouchFocusQuick"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testGallery"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSettings"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSettingsSaveLoad"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFolderChooserNew"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFolderChooserInvalid"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSaveFolderHistory"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSaveFolderHistorySAF"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSettingsPrivacyPolicy"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPreviewRotation"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLayoutNoLimits"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLayoutNoLimitsStartup"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCameraModes"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testFailOpenCamera"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testAudioControlIcon"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testOnError"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testGPSString"));
        }
        if( MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPreviewBitmap"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testVideoFPSHighSpeed"));
        }
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            // intensive test, can crash when run as suite on older devices (Nexus 6, Nexus 7) with Camera2 at least
            // also run this test last, just in case
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testSwitchCameraRepeat2"));
        }
        return suite;
    }
}
