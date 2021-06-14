package com.transmedika.transmedikakitui.modul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {

    private SmsVerificationFragment fragment;

    public void setFragment(SmsVerificationFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            StringBuilder messageBody = new StringBuilder();
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                messageBody.append(smsMessage.getMessageBody()).append(" ");

            }
            Toast.makeText(context, messageBody.toString(), Toast.LENGTH_SHORT).show();
            if (fragment != null) {
                fragment.setMessage(messageBody.toString());
            }
        }
    }
}