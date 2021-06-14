package net.sourceforge.opencamera.minigallery;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public class FileType {
    private Integer type;
    private Uri uri;

    public FileType(Integer type, Uri uri) {
        this.type = type;
        this.uri = uri;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
