package com.example.onlinemusicapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoUserFragment extends Fragment {

    apiService mService;
    EditText edtName, edtDescription, edtId;
    TextView tvFile, tvUser;
    Button btnAddSong, btnAddSinger, btnUpdateSong, btnUpdateSinger, btnPath, btnLogOut;
    String name, lyricPath, description, receiveToken, path, idSinger;
    Uri pathUri;
    int id, idSingerInt;

    AlertDialog alertInfoUser;
    AlertDialog.Builder builder;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d("InfoUser", "Read file");
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data == null)
                    return;
                Uri uri = data.getData();
                pathUri = uri;
                path = RealPathUtil.getRealPath(getActivity(), pathUri);
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info_user, container, false);

        // Ánh xạ
        mService = RetrofitClient.getRetrofitInitializedService();
        edtName = v.findViewById(R.id.edt_Name);
        btnPath = v.findViewById(R.id.btn_Path);
        edtDescription = v.findViewById(R.id.edt_Description);
        edtId = v.findViewById(R.id.edt_IdInfo);
        btnAddSong = v.findViewById(R.id.btn_AddSong);
        btnAddSinger = v.findViewById(R.id.btn_AddSinger);
        btnUpdateSong = v.findViewById(R.id.btn_UpdateSong);
        btnUpdateSinger = v.findViewById(R.id.btn_UpdateSinger);
        tvFile = v.findViewById(R.id.tv_File);
        tvUser = v.findViewById(R.id.tv_User);
        builder = new AlertDialog.Builder(getActivity());

        if (MusicAppGlobal.getInstance().hasLoggedIn()) {
            tvUser.setText("Your authorization: YES | Logged in");
        }
        else {

            tvUser.setText("Your authorization: No | Logged out");
        }

        btnPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRequestPermission();
            }
        });

        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().length() == 0 || edtDescription.getText().toString().length() == 0) {
                    builder.setMessage("Please enter data of new song!").setCancelable(true);
                    alertInfoUser = builder.create();
                    alertInfoUser.show();
                } else {
                    path = RealPathUtil.getRealPath(getActivity(), pathUri);
                    name = edtName.getText().toString();
                    idSinger = edtDescription.getText().toString();
                    lyricPath = null;
                    sendSong();
                }
            }
        });

        btnAddSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                if (edtDescription.getText().toString().length() > 0)
                    description = edtDescription.getText().toString();
                else
                    description = null;
                if (name.length() == 0) {
                    builder.setMessage("Please enter name of new singer!").setCancelable(true);
                    alertInfoUser = builder.create();
                    alertInfoUser.show();
                } else {
                    sendSinger();
                }
            }
        });

        btnUpdateSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtId.getText().toString().length() == 0 || edtName.getText().toString().length() == 0 || edtDescription.getText().toString().length() == 0) {
                    builder.setMessage("Please enter data!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertInfoUser = builder.create();
                    alertInfoUser.show();
                } else {
                    id = Integer.parseInt(edtId.getText().toString());
                    name = edtName.getText().toString();
                    idSingerInt = Integer.parseInt(edtDescription.getText().toString());
                    changeSong();
                }
            }
        });

        btnUpdateSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtId.getText().toString().length() == 0 || edtName.getText().toString().length() == 0) {
                    builder.setMessage("Please enter data!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertInfoUser = builder.create();
                    alertInfoUser.show();
                } else {
                    name = edtName.getText().toString();
                    description = edtDescription.getText().toString();
                    id = Integer.parseInt(edtId.getText().toString());
                    changeSinger();
                }
            }
        });

        btnLogOut = (Button) v.findViewById(R.id.btn_LogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser(v);
            }
        });

        return v;
    }

    private void logOutUser(View v) {
        mService.logOutUser(MusicAppGlobal.getInstance().getAccessToken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    MusicAppGlobal.getInstance().removeAccessToken();
                    Log.d("logOut", "Đăng xuất thành công !!");
                    Toast.makeText(v.getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(v.getContext(), "Đăng xuất không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("HomeActivity", "Loi " + t.getMessage());
            }
        });
    }

    private void clickRequestPermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            openFile();
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openFile();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFile();
            }
        }
    }

    private void openFile() {
        Intent storage = new Intent();
        storage.setType("audio/*");
        storage.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(storage, "Select file"));
    }

    private void changeSinger() {
        mService.changeSinger(MusicAppGlobal.getInstance().getAccessToken(), id, name, description).enqueue(new Callback<Singer>() {
            @Override
            public void onResponse(Call<Singer> call, Response<Singer> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "Singer " + name + " was updated", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Update singer successfully: " + response.code());
                } else {
                    Toast.makeText(getActivity(), "Update singer unsuccessfully!", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Update singer unsuccessfully: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Singer> call, Throwable t) {
                Log.d("InfoUser", "Error " + t.getMessage());
            }
        });
    }

    private void changeSong() {
        mService.changeSong(MusicAppGlobal.getInstance().getAccessToken(), id, name, idSingerInt).enqueue(new Callback<SOSong>() {
            @Override
            public void onResponse(Call<SOSong> call, Response<SOSong> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "Song " + name + " was updated", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Update song successfully: " + response.code());
                } else {
                    Toast.makeText(getActivity(), "Update song unsuccessfully!", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Update song unsuccessfully " + response.code());
                }
            }
            @Override
            public void onFailure(Call<SOSong> call, Throwable t) {
                Log.d("InfoUser", "Error " + t.getMessage());
            }
        });
    }

    private void sendSong() {
        File f = new File(path);

        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody requestBodySingerId = RequestBody.create(MediaType.parse("multipart/form-data"), idSinger);
        RequestBody requestBodyPath = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        MultipartBody.Part multipartBodyPath = MultipartBody.Part.createFormData("path", f.getName(), requestBodyPath);

        mService.sendSong(MusicAppGlobal.getInstance().getAccessToken(), requestBodyName, requestBodySingerId, multipartBodyPath).enqueue(new Callback<SOSong>() {
            @Override
            public void onResponse(Call<SOSong> call, Response<SOSong> response) {
                if (response.isSuccessful()) {
                    tvFile.setText(path);
                    Toast.makeText(getActivity(), "Song was added", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Add song successfully: " + response.body() + " " + response.code());
                } else {
                    Toast.makeText(getActivity(), "Add song unsuccessfully!", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Add song unsuccessfully: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<SOSong> call, Throwable t) {
                Log.d("InfoUser", "Error " + t.getMessage());
            }
        });
    }

    private void sendSinger() {
        mService.sendSinger(MusicAppGlobal.getInstance().getAccessToken(), name, description).enqueue(new Callback<Singer>() {
            @Override
            public void onResponse(Call<Singer> call, Response<Singer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Singer " + name + " was added", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Add singer successfully!");
                } else {
                    Toast.makeText(getActivity(), "Add singer successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("InfoUser", "Add singer successfully: " + response.code() + " " + receiveToken);
                }
            }
            @Override
            public void onFailure(Call<Singer> call, Throwable t) {
                Log.d("InfoUser", "Error " + t.getMessage());
            }
        });
    }
}