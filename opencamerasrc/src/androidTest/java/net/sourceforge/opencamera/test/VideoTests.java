package net.sourceforge.opencamera.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class VideoTests {
    // Tests related to video recording; note that tests to do with video mode that don't record are still part of MainTests
    public static Test suite() {
        TestSuite suite = new TestSuite(MainTests.class.getName());
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideo"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoAudioControl"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSAF"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSubtitles"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSubtitlesGPS"));
        }

        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testIntentVideo"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testIntentVideoDurationLimit"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testImmersiveMode"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testImmersiveModeEverything"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoStabilization"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoExposureLock"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoFocusArea"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoQuick"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoQuickSAF"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxDuration"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxDurationRestart"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxDurationRestartInterrupt"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSettings"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMacro"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoPause"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoPauseStop"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSnapshot"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSnapshotTimer"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSnapshotPausePreview"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSnapshotMax"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoFlashVideo"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testVideoTimerInterrupt"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testVideoPopup"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testVideoTimerPopup"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoAvailableMemory"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoAvailableMemory2"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxFileSize1"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxFileSize2"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxFileSize3"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxFileSize4"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoMaxFileSize4SAF"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoTimeLapse"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoForceFailure"));
        if( MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testVideoLogProfile"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testVideoEdgeModeNoiseReductionMode"));
        }
        // put tests which change bitrate, fps or test 4K at end
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoFPS"));
        if( MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoFPSHighSpeedManual"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakeVideoSlowMotion"));
        }
        // update: now deprecating these tests, as setting these settings can be dodgy on some devices
        /*suite.addTest(TestSuite.createTest(MainActivityTest.class, "testTakeVideoBitrate"));
        suite.addTest(TestSuite.createTest(MainActivityTest.class, "testTakeVideo4K"));*/

        // tests for video log profile (but these don't actually record video)
        if( MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLogProfile1"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLogProfile2"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLogProfile3"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLogProfile1_extra_strong"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLogProfile2_extra_strong"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLogProfile3_extra_strong"));
        }
        return suite;
    }
}
