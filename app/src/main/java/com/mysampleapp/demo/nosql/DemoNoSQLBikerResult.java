package com.mysampleapp.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.BikerDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mysampleapp.Application;

import java.util.Set;

public class DemoNoSQLBikerResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final BikerDO result;
    private DynamoDBMapper mapper;

    DemoNoSQLBikerResult(final BikerDO result) {
        this.result = result;
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        this.mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();
    }
    @Override
    public void updateItem() {
        final String originalValue = result.getDateLastAccess();
        result.setDateLastAccess(DemoSampleDataGenerator.getRandomSampleString("date_lastAccess"));
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setDateLastAccess(originalValue);
            throw ex;
        }
    }

    @Override
    public void deleteItem() {
        mapper.delete(result);
    }

    private void setKeyTextViewStyle(final TextView textView) {
        textView.setTextColor(KEY_TEXT_COLOR);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(5), dp(2), dp(5), 0);
        textView.setLayoutParams(layoutParams);
    }

    /**
     * @param dp number of design pixels.
     * @return number of pixels corresponding to the desired design pixels.
     */
    private int dp(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void setValueTextViewStyle(final TextView textView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(15), 0, dp(15), dp(2));
        textView.setLayoutParams(layoutParams);
    }

    private void setKeyAndValueTextViewStyles(final TextView keyTextView, final TextView valueTextView) {
        setKeyTextViewStyle(keyTextView);
        setValueTextViewStyle(valueTextView);
    }

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02X", bytes[0]));
        for(int index = 1; index < bytes.length; index++) {
            builder.append(String.format(" %02X", bytes[index]));
        }
        return builder.toString();
    }

    private static String byteSetsToHexStrings(Set<byte[]> bytesSet) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        for (byte[] bytes : bytesSet) {
            builder.append(String.format("%d: ", ++index));
            builder.append(bytesToHexString(bytes));
            if (index < bytesSet.size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public View getView(final Context context, final View convertView, int position) {
        final LinearLayout layout;
        final TextView resultNumberTextView;
        final TextView userIdKeyTextView;
        final TextView userIdValueTextView;
        final TextView dateLastAccessKeyTextView;
        final TextView dateLastAccessValueTextView;
        final TextView firstNameKeyTextView;
        final TextView firstNameValueTextView;
        final TextView lastNameKeyTextView;
        final TextView lastNameValueTextView;
        final TextView passwordKeyTextView;
        final TextView passwordValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            userIdKeyTextView = new TextView(context);
            userIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userIdKeyTextView, userIdValueTextView);
            layout.addView(userIdKeyTextView);
            layout.addView(userIdValueTextView);

            dateLastAccessKeyTextView = new TextView(context);
            dateLastAccessValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(dateLastAccessKeyTextView, dateLastAccessValueTextView);
            layout.addView(dateLastAccessKeyTextView);
            layout.addView(dateLastAccessValueTextView);

            firstNameKeyTextView = new TextView(context);
            firstNameValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(firstNameKeyTextView, firstNameValueTextView);
            layout.addView(firstNameKeyTextView);
            layout.addView(firstNameValueTextView);

            lastNameKeyTextView = new TextView(context);
            lastNameValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(lastNameKeyTextView, lastNameValueTextView);
            layout.addView(lastNameKeyTextView);
            layout.addView(lastNameValueTextView);

            passwordKeyTextView = new TextView(context);
            passwordValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(passwordKeyTextView, passwordValueTextView);
            layout.addView(passwordKeyTextView);
            layout.addView(passwordValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            dateLastAccessKeyTextView = (TextView) layout.getChildAt(3);
            dateLastAccessValueTextView = (TextView) layout.getChildAt(4);

            firstNameKeyTextView = (TextView) layout.getChildAt(5);
            firstNameValueTextView = (TextView) layout.getChildAt(6);

            lastNameKeyTextView = (TextView) layout.getChildAt(7);
            lastNameValueTextView = (TextView) layout.getChildAt(8);

            passwordKeyTextView = (TextView) layout.getChildAt(9);
            passwordValueTextView = (TextView) layout.getChildAt(10);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        dateLastAccessKeyTextView.setText("date_lastAccess");
        dateLastAccessValueTextView.setText(result.getDateLastAccess());
        firstNameKeyTextView.setText("firstName");
        firstNameValueTextView.setText(result.getFirstName());
        lastNameKeyTextView.setText("lastName");
        lastNameValueTextView.setText(result.getLastName());
        passwordKeyTextView.setText("password");
        passwordValueTextView.setText(result.getPassword());
        return layout;
    }
}
