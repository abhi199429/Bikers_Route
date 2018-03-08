package com.mysampleapp;

import android.net.UrlQuerySanitizer;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.api.idlifo3u9d63.RoutesMobileHubClient;
import com.amazonaws.mobile.api.idn11j96w8ll.BikerMobileHubClient;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.api.CloudLogicAPI;
import com.amazonaws.mobile.api.CloudLogicAPIConfiguration;
import com.amazonaws.mobile.api.CloudLogicAPIFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesActivity extends AppCompatActivity {

    private Button New_Route;
    private PopupWindow popupwindow;
    private LayoutInflater layoutinflater;
    private RelativeLayout relativeLayout;

    private RoutesMobileHubClient apiClient;

    AWSMobileClient awsMobileClient;
    CloudLogicAPI client;
    ApiRequest request;

    private EditText mResultField;

    //NEW - setup defaults for Request to AWS
   // private static final String DEFAULT_QUERY_STRING = "?lang=en_US";
    private static final String DEFAULT_QUERY_STRING = "?userId=sruthi";

    private static final String DEFAULT_REQUEST_BODY = "{\n  \"userId\" : \"sruthi\" \n}";

    public static final int DEFAULT_API_INDEX = 0;
    public static final String DEFAULT_PATH = "/getRoute";
    public static final String DEFAULT_METHOD = "PUT";
    private CloudLogicAPIConfiguration apiConfiguration;


    //NEW- setup parameters to make AWS call -use defaults set above
    final int apiIndex = this.DEFAULT_API_INDEX;  //0
    final String path = this.DEFAULT_PATH;   // it is /getRoutes
    final String method = this.DEFAULT_METHOD; // it is POST
    final String queryString = this.DEFAULT_QUERY_STRING; // sets language US English see above
    final String body = this.DEFAULT_REQUEST_BODY;  // is JSON object containing data to send in request, see above


    private static String LOG_TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView item;
        TableRow tr;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_routes);

        New_Route = (Button) findViewById(R.id.New_Route);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);

        mResultField = (EditText) findViewById(R.id.Route_EditText);
        invokeRequest();


        New_Route.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutinflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutinflater.inflate(R.layout.routedetails, null);

                popupwindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                Button Ok = (Button) container.findViewById(R.id.Ok);
                Ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupwindow.dismiss();
                    }
                });


                popupwindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

            }

        });
        /*List<String> l = new ArrayList<String>();
        l.add("asdfg");
        l.add("dgsfhh");
        l.add("dsfdfhcvhb");

        for (int i = 0; i < l.size(); i++) {
            item = new TextView(this);
            tr = new TableRow(this);
            item.setText(l.get(i));
            TableLayout table = (TableLayout) findViewById(R.id.tableScreen2);
            tr.addView(item);
            table.addView(tr);
        }*/
    }

  //  @Override
    public void onClick(View v) {
        invokeRequest();
    }


    /**
     * invokeRequest to AWS performed on separate Thread rather than using AsyncTask
     *Makes /getRoutes request to AWS Lambda backend that retrieves all routs associated with
     * userId specified in the body class variable. Gets results and call setBodyText method
     * to display response.
     */
    private void invokeRequest(){
//NEW - setup log tag to equal class name
       // this.LOG_TAG = this.getClass().getName();

//GET APIs: get APIs from CloudLogicAPIFactory
        apiConfiguration = CloudLogicAPIFactory.getAPIs()[apiIndex];
        final String endpoint = apiConfiguration.getEndpoint();

        Log.d(LOG_TAG, "Endpoint : " + endpoint);
        Log.d(LOG_TAG, "Path : " + path);
        Log.d(LOG_TAG, "Method : " + method);

//AWS SETUP: setup AWS request query string parameters as Map object  -- only sending language type //as US English
        final Map<String, String> parameters = convertQueryStringToParameters(queryString);

//AWSMobileClient & CloudLogicAPI SETUP: create instance of CloudLogicAPI to send request through to //AWS Lambda code
     //  AWSMobileClient.initializeMobileClientIfNecessary(this);
       // awsMobileClient = AWSMobileClient.defaultMobileClient();

        //AWSMobileClient.getInstance().initialize(this).execute();
       // client = awsMobileClient.getClient(apiConfiguration.getClientClass());

       //client = awsMobileClient.createAPIClient(apiConfiguration.getClientClass());

        apiClient = new ApiClientFactory()
                .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
                .build(RoutesMobileHubClient.class);


//DEFINE Variables Related to Request: setup headers for request --initally empty
        final Map<String, String> headers = new HashMap<String, String>();

//SETUP content based on BODY -- THIS contains data sending in request if POST method type
        final byte[] content = body.getBytes(StringUtils.UTF8);

//SETUP ApiReqest = HTTP request given path, method, headers, parameters and copy in body if not //empty as content
        ApiRequest tmpRequest =  new ApiRequest(apiClient.getClass().getSimpleName());
        tmpRequest.withPath(path);
        tmpRequest.withHttpMethod(HttpMethodName.valueOf(method));
        tmpRequest.withHeaders(headers);
        tmpRequest.addHeader("Content-Type", "application/json");
        tmpRequest.withParameters(parameters);

// final ApiRequest request;
// Only set body if it has content.
        if (body.length() > 0) {
            request = tmpRequest
                    .addHeader("Content-Length", String.valueOf(content.length))
                    .withBody(content);
        } else { request = tmpRequest; }

        // Make network call on background thread
        new Thread(new Runnable() {
            Exception exception = null;

            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Invoking API w/ Request : " + request.getHttpMethod() + ":" + request.getPath());
                    long startTime = System.currentTimeMillis();
//THIS EXECUTES THE REQUEST
                    final ApiResponse response = apiClient.execute(request);
                    final long latency = System.currentTimeMillis() - startTime;

                    final InputStream responseContentStream = response.getContent();  //GRAB THE RESPONSE
                    if (responseContentStream != null) {
                        final String responseData = IOUtils.toString(responseContentStream);
                        Log.d(LOG_TAG, " ************Response : ************" + responseData);
                        setResponseBodyText(responseData);
                    }

                } catch (final Exception exception) {
                    Log.e(LOG_TAG, exception.getMessage(), exception);
                    exception.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//take response and DISPLAY IT
                            setResponseBodyText(exception.getMessage());
                        }
                    });
                }
            }

        }).start();
    }



    /** * setups parameters of query string in Map format to pass to AWS request
     * @param queryStringText
     * @return */
    private Map<String,String> convertQueryStringToParameters(String queryStringText) {

        while (queryStringText.startsWith("?") && queryStringText.length() > 1) {

            queryStringText = queryStringText.substring(1);

        }

        final UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
        sanitizer.setAllowUnregisteredParamaters(true);
        sanitizer.parseQuery(queryStringText);

        final List<UrlQuerySanitizer.ParameterValuePair> pairList = sanitizer.getParameterList();
        final Map<String, String> parameters = new HashMap<>();

        for (final UrlQuerySanitizer.ParameterValuePair pair : pairList) {

            Log.d(LOG_TAG, pair.mParameter + " = " + pair.mValue);
            parameters.put(pair.mParameter, pair.mValue);

        }

        return parameters;


    }

    /**
     * receives the JSON string and dumps it into an EditText widget associated with
     *   class variable mResultField
     * @param text */
    private void setResponseBodyText(final String text) {

        ThreadUtils.runOnUiThread(new Runnable() {

            @Override
            public void run() { mResultField.setText(text);}

        });

    }




    /**
     * display error message
     * @param errorMessage
     */
    public void showError(final String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(this.getResources().getString(R.string.app_name) + " AWS error")
                .setMessage(errorMessage)
                .setNegativeButton(this.getResources().getString(R.string.error_dismiss), null)
                .create().show();
    }
}
