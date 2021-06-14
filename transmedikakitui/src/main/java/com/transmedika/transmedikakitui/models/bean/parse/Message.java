package com.transmedika.transmedikakitui.models.bean.parse;

import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.transmedika.transmedikakitui.models.bean.json.SpaPasien;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.Date;
import java.util.StringTokenizer;


@ParseClassName("Conversations")
public class Message extends ParseObject {
    public static final String NOT_SEND_STATUS = "not_send";
    public static final String SENT_STATUS = "sent";
    public static final String RECEIVED_STATUS = "delivered";
    public static final String SEEN_STATUS = "seen";
    public static final String FAILED_STATUS = "failed";

    public static final String TEXT_TYPE = "text";
    public static final String FILE_TYPE = "file";
    public static final String DATE_TYPE = "date";
    public static final String URL_TYPE = "link";
    public static final String IMAGE_TYPE = "image";
    public static final String VIDEO_TYPE = "video";
    public static final String SESSION_END_TYPE = "sesi_berakhir";
    public static final String RESEP_OBAT_TYPE = "resep_dokter";
    public static final String RESEP_OBAT_SPA = "spa";
    public static final String RESEP_OBAT_CATATAN = "catatan";
    public static final String PHR_REQ_TYPE = "phr_request";
    public static final String PHR_REQ_MESSAGE = "PHR Request";
    public static final String PHR_REQ_STATUS_ALLOWED = "ALLOWED";
    public static final String PHR_REQ_STATUS_DENIED = "DENIED";

    public String getMessageId() {
        return getString("messageId");
    }

    public void setMessageId(String messageId) {
        put("messageId", messageId);
    }

    public ParseUser getSenderId() {
        return getParseUser("sender_id");
    }

    public void setSenderId(ParseUser senderId) {
        put("sender_id", senderId);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }

    public ParseUser getUid() {
        return getParseUser("uid");
    }

    public void setUid(ParseUser uid) {
        put("uid", uid);
    }

    public String getStatus() {
        return getString("status");
    }

    public void setStatus(String status) {
        put("status", status);
    }

    public String getKind() {
        return getString("kind");
    }

    public void setKind(String kind) {
        put("kind", kind);
    }

    public String getKonsultasiId() {
        return getString("consultation_id");
    }

    public void setKonsultasiId(String kind) {
        put("consultation_id", kind);
    }

    public String getFileLocal() {
        return getString("fileLocal");
    }

    public void setFileLocal(String fileLocal) {
        put("fileLocal", fileLocal);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Message))
            return false;
        Message other = (Message) o;
        return this.getMessageId().equals(other.getMessageId());
    }

    public SpaPasien getSpa() {
        return TransmedikaUtils.gsonBuilder().fromJson(getText(), SpaPasien.class);
    }

    public String getSymtomps() {
        String data = null;
        if (getText() != null) {
            if (getText() != null) {
                data = wrapSpa(getSpa().getSpa().getSymptoms());
            }
        }

        return data;
    }

    public String getPossibleDiganosis() {
        String data = null;
        if (getText() != null) {
            data = wrapSpa(getSpa().getSpa().getPossibleDiagnosis());
        }
        return data;
    }

    public String getAdvice() {
        String data = null;
        if (getText() != null) {
            data = wrapSpa(getSpa().getSpa().getAdvice());
        }
        return data;
    }

    private String wrapSpa(String obj) {
        StringTokenizer st;
        int jml;
        st = new StringTokenizer(obj, "|");
        jml = st.countTokens();
        StringBuilder builder = new StringBuilder();
        if (jml > 0 && jml <= 3) {
            for (int i = 0; i < jml; i++) {
                if (i == jml - 1) {
                    builder.append("- ".concat(st.nextToken()));
                } else {
                    builder.append("- ".concat(st.nextToken().concat(System.lineSeparator())));
                }
            }

        } else if (jml > 3) {
            for (int i = 0; i < 3; i++) {
                builder.append("- ".concat(st.nextToken().concat(System.lineSeparator())));
            }
            int xx = jml - 3;
            String cv = "- ";
            builder.append(cv.concat(String.valueOf(xx).concat("+")).concat(" Lainnya"));
        }
        return builder.toString();
    }
}
