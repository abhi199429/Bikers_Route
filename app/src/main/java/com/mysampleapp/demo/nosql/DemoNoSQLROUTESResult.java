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
import com.amazonaws.models.nosql.ROUTESDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mysampleapp.Application;

import java.util.Set;

public class DemoNoSQLROUTESResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final ROUTESDO result;
    private DynamoDBMapper mapper;

    DemoNoSQLROUTESResult(final ROUTESDO result) {
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
        final String originalValue = result.getCategory();
        result.setCategory(DemoSampleDataGenerator.getRandomSampleString("category"));
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setCategory(originalValue);
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
        final TextView routeIdKeyTextView;
        final TextView routeIdValueTextView;
        final TextView categoryKeyTextView;
        final TextView categoryValueTextView;
        final TextView endLatitudeKeyTextView;
        final TextView endLatitudeValueTextView;
        final TextView endLongitudeKeyTextView;
        final TextView endLongitudeValueTextView;
        final TextView nameKeyTextView;
        final TextView nameValueTextView;
        final TextView numberTraveledKeyTextView;
        final TextView numberTraveledValueTextView;
        final TextView startLatitudeKeyTextView;
        final TextView startLatitudeValueTextView;
        final TextView startLongitudeKeyTextView;
        final TextView startLongitudeValueTextView;
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

            routeIdKeyTextView = new TextView(context);
            routeIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(routeIdKeyTextView, routeIdValueTextView);
            layout.addView(routeIdKeyTextView);
            layout.addView(routeIdValueTextView);

            categoryKeyTextView = new TextView(context);
            categoryValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(categoryKeyTextView, categoryValueTextView);
            layout.addView(categoryKeyTextView);
            layout.addView(categoryValueTextView);

            endLatitudeKeyTextView = new TextView(context);
            endLatitudeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(endLatitudeKeyTextView, endLatitudeValueTextView);
            layout.addView(endLatitudeKeyTextView);
            layout.addView(endLatitudeValueTextView);

            endLongitudeKeyTextView = new TextView(context);
            endLongitudeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(endLongitudeKeyTextView, endLongitudeValueTextView);
            layout.addView(endLongitudeKeyTextView);
            layout.addView(endLongitudeValueTextView);

            nameKeyTextView = new TextView(context);
            nameValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(nameKeyTextView, nameValueTextView);
            layout.addView(nameKeyTextView);
            layout.addView(nameValueTextView);

            numberTraveledKeyTextView = new TextView(context);
            numberTraveledValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(numberTraveledKeyTextView, numberTraveledValueTextView);
            layout.addView(numberTraveledKeyTextView);
            layout.addView(numberTraveledValueTextView);

            startLatitudeKeyTextView = new TextView(context);
            startLatitudeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(startLatitudeKeyTextView, startLatitudeValueTextView);
            layout.addView(startLatitudeKeyTextView);
            layout.addView(startLatitudeValueTextView);

            startLongitudeKeyTextView = new TextView(context);
            startLongitudeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(startLongitudeKeyTextView, startLongitudeValueTextView);
            layout.addView(startLongitudeKeyTextView);
            layout.addView(startLongitudeValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            routeIdKeyTextView = (TextView) layout.getChildAt(3);
            routeIdValueTextView = (TextView) layout.getChildAt(4);

            categoryKeyTextView = (TextView) layout.getChildAt(5);
            categoryValueTextView = (TextView) layout.getChildAt(6);

            endLatitudeKeyTextView = (TextView) layout.getChildAt(7);
            endLatitudeValueTextView = (TextView) layout.getChildAt(8);

            endLongitudeKeyTextView = (TextView) layout.getChildAt(9);
            endLongitudeValueTextView = (TextView) layout.getChildAt(10);

            nameKeyTextView = (TextView) layout.getChildAt(11);
            nameValueTextView = (TextView) layout.getChildAt(12);

            numberTraveledKeyTextView = (TextView) layout.getChildAt(13);
            numberTraveledValueTextView = (TextView) layout.getChildAt(14);

            startLatitudeKeyTextView = (TextView) layout.getChildAt(15);
            startLatitudeValueTextView = (TextView) layout.getChildAt(16);

            startLongitudeKeyTextView = (TextView) layout.getChildAt(17);
            startLongitudeValueTextView = (TextView) layout.getChildAt(18);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        routeIdKeyTextView.setText("routeId");
        routeIdValueTextView.setText(result.getRouteId());
        categoryKeyTextView.setText("category");
        categoryValueTextView.setText(result.getCategory());
        endLatitudeKeyTextView.setText("end_latitude");
        endLatitudeValueTextView.setText("" + result.getEndLatitude().longValue());
        endLongitudeKeyTextView.setText("end_longitude");
        endLongitudeValueTextView.setText("" + result.getEndLongitude().longValue());
        nameKeyTextView.setText("name");
        nameValueTextView.setText(result.getName());
        numberTraveledKeyTextView.setText("number_traveled");
        numberTraveledValueTextView.setText("" + result.getNumberTraveled().longValue());
        startLatitudeKeyTextView.setText("start_latitude");
        startLatitudeValueTextView.setText("" + result.getStartLatitude().longValue());
        startLongitudeKeyTextView.setText("start_longitude");
        startLongitudeValueTextView.setText("" + result.getStartLongitude().longValue());
        return layout;
    }
}
