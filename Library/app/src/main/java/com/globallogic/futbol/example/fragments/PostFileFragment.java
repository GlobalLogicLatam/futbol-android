package com.globallogic.futbol.example.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.operations.PostFileOperation;

import java.io.File;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class PostFileFragment extends Fragment implements PostFileOperation.Callback, View.OnClickListener {
    public static final String TAG = "PostFileFragment";

    //region Operations
    private final PostFileOperation mPostFileOperation;
    private final PostFileOperation.Receiver mPostFileReceiver;
    //endregion

    //region Variables
    private TextView vOperationResult;
    private TextView vFilePath;
    private Button vSubmit;
    private File mFileToUpload;
    private Button vChoose;
    //endregion

    //region PostFileOperation.Callback
    public PostFileFragment() {
        mPostFileOperation = new PostFileOperation();
        mPostFileReceiver = new PostFileOperation.Receiver(this);
    }

    @Override
    public void onNoInternet() {
        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartOperation() {
        updateOperationStatus();
        enableButtons(false);
    }

    @Override
    public void onFinishOperation() {
        updateOperationStatus();
        enableButtons(true);
    }

    @Override
    public void onSuccess(File aFile) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(new File(aFile.getAbsolutePath())), "image/*");
        startActivity(i);
        enableButtons(true);
    }

    @Override
    public void onError() {
        vOperationResult.setText(mPostFileOperation.getError(getActivity()));
        enableButtons(true);
    }
    //endregion

    //region Fragment lifecycle
    public static PostFileFragment newInstance() {
        return new PostFileFragment();
    }

    private void enableButtons(boolean value) {
        vSubmit.setEnabled(value);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostFileOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_example_file, container, false);
        vSubmit = (Button) rootView.findViewById(R.id.fragment_post_example_file_submit);
        vSubmit.setOnClickListener(this);
        vChoose = (Button) rootView.findViewById(R.id.fragment_post_example_file_choose);
        vChoose.setOnClickListener(this);
        vFilePath = (TextView) rootView.findViewById(R.id.fragment_post_example_file_name);
        vOperationResult = (TextView) rootView.findViewById(R.id.fragment_post_example_file_result);

        mPostFileReceiver.register(mPostFileOperation);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateOperationStatus();
    }
    //endregion

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_post_example_file_submit:
                submit();
                break;
            case R.id.fragment_post_example_file_choose:
                showFileChooser();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardo los datos necesario de la operacion
        mPostFileOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mPostFileReceiver.unRegister();
    }

    private boolean checkRequiredField(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(getString(R.string.required));
            editText.requestFocus();
            return false;
        }
        editText.setError(null);
        return true;
    }

    private void submit() {
        if (mFileToUpload != null) mPostFileOperation.execute(mFileToUpload);
    }

    private void updateOperationStatus() {
        switch (mPostFileOperation.getStatus()) {
            case READY_TO_EXECUTE:
                vOperationResult.setText("Ready to execute");
                break;
            case WAITING_EXECUTION:
                vOperationResult.setText("Waiting execution");
                break;
            case DOING_EXECUTION:
                vOperationResult.setText("Doing execution");
                break;
            case FINISHED_EXECUTION:
                vOperationResult.setText("Finished execution");
                break;
            case UNKNOWN:
            default:
                vOperationResult.setText("Some error");
                break;
        }
    }

    //region File chooser
    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = getPath(getActivity(), uri);
                    Log.d(TAG, "File Path: " + path);
                    // Get the file instance
                    mFileToUpload = new File(path);
                    vFilePath.setText(getString(R.string.file_to_upload_placeholder) + mFileToUpload.getAbsolutePath());
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, Uri uri)
    {
        int currentApiVersion;
        try
        {
            currentApiVersion = android.os.Build.VERSION.SDK_INT;
        }
        catch(NumberFormatException e)
        {
            //API 3 will crash if SDK_INT is called
            currentApiVersion = 3;
        }
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst())
            {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
        else if (currentApiVersion <= Build.VERSION_CODES.HONEYCOMB_MR2 && currentApiVersion >= Build.VERSION_CODES.HONEYCOMB)

        {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null)
            {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
        else
        {

            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }
    //endregion
}