package net.sourceforge.opencamera.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PhotoTests {
    // Tests related to taking photos; note that tests to do with photo mode that don't take photos are still part of MainTests
    public static Test suite() {
        TestSuite suite = new TestSuite(MainTests.class.getName());
        // put these tests first as they require various permissions be allowed, that can only be set by user action
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoSAF"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLocationOn"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLocationDirectionOn"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLocationOff"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testLocationOnSAF"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testDirectionOn"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testDirectionOnSAF"));
        }
        // then do memory intensive tests:
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAutoLevel"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAutoLevelLowMemory"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAutoLevelAngles"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAutoLevelAnglesLowMemory"));
        // other tests:
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhoto"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoContinuous"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoContinuousNoTouch"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAutoStabilise"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFlashAuto"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFlashOn"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFlashTorch"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAudioButton"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoNoAutofocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoNoThumbnail"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFlashBug"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFrontCameraAll"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFrontCamera"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFrontCameraMulti"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoFrontCameraScreenFlash"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAutoFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoLockedFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoExposureCompensation"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoLockedLandscape"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoLockedPortrait"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPaused"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedAudioButton"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedSAF"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedTrash"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedTrashSAF"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedTrash2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoQuickFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRepeatFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRepeatFocusLocked"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAfterFocus"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoSingleTap"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoDoubleTap"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoAlt"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTimerBackground"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTimerSettings"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTimerPopup"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoRepeat"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousPicture1"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousPicture2"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousPictureFocusRepeat"));
        if( MainCameraActivityTest.test_camera2 ) {
            // test_wait_capture_result only relevant for Camera2 API
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testContinuousPictureFocusRepeatWaitCaptureResult"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testKeyboardControls"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPhotoStamp"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPhotoStampSAF"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoDRO"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoDROPhotoStamp"));
        }
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoHDR"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testPhotoBackgroundHDR"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoHDRSaveExpo"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoHDRFrontCamera"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoHDRAutoStabilise"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoHDRPhotoStamp"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoExpo"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPanorama"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPanoramaMax"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPanoramaCancel"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPanoramaCancelBySettings"));
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCreateSaveFolder1"));
        if( !MainCameraActivityTest.test_camera2 ) {
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCreateSaveFolder2"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCreateSaveFolder3"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCreateSaveFolder4"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCreateSaveFolderUnicode"));
            suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testCreateSaveFolderEmpty"));
        }
        // testTakePhotoPreviewPausedShare should be last, as sharing the image may sometimes cause later tests to hang
        suite.addTest(TestSuite.createTest(MainCameraActivityTest.class, "testTakePhotoPreviewPausedShare"));
        return suite;
    }
}
