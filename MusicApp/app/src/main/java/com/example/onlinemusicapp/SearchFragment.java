package com.example.onlinemusicapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    apiService mService;
    Button btnSearchSong, btnSearchSinger, btnDeleteSong, btnDeleteSinger;
    EditText edtId;
    TextView tvResultSearch;
    int id;
    String singerName;

    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vSearch = inflater.inflate(R.layout.fragment_search, container, false);

        mService = RetrofitClient.getRetrofitInitializedService();
        builder = new AlertDialog.Builder(getContext());

        btnSearchSong = vSearch.findViewById(R.id.btn_SearchSong);
        btnSearchSinger = vSearch.findViewById(R.id.btn_SearchSinger);
        btnDeleteSong = vSearch.findViewById(R.id.btn_DeleteSong);
        btnDeleteSinger = vSearch.findViewById(R.id.btn_DeleteSinger);
        edtId = vSearch.findViewById(R.id.edt_Id);
        tvResultSearch = vSearch.findViewById(R.id.tv_ResultSearch);

        btnSearchSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtId.getText().toString().length() == 0) {
                    builder.setMessage("Please enter song's id!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    id = Integer.parseInt(edtId.getText().toString());
                    getSongId();
                }
            }
        });

        btnSearchSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtId.getText().toString().length() == 0) {
                    builder.setMessage("Please enter singer's id!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    id = Integer.parseInt(edtId.getText().toString());
                    getSingerId();
                }
            }
        });

        btnDeleteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtId.getText().toString().length() == 0) {
                    builder.setMessage("Please enter song's id!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    id = Integer.parseInt(edtId.getText().toString());
                    deleteSong();
                }
            }
        });

        btnDeleteSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtId.getText().toString().length() == 0) {
                    builder.setMessage("Please enter singer's id!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    id = Integer.parseInt(edtId.getText().toString());
                    deleteSinger();
                }
            }
        });
        return vSearch;
    }

    private void getSongId() {
        mService.getSongId(id).enqueue(new Callback<SOSong>() {
            @Override
            public void onResponse(Call<SOSong> call, Response<SOSong> response) {
                if (response.isSuccessful()) {
                    id = response.body().getSinger().getId();
                    tvResultSearch.setText(response.body().getName() + "\n" + response.body().getSinger().getId());
                    Toast.makeText(getActivity(), "The song " + response.body().getName(), Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "The song " + response.body());
                } else {
                    Toast.makeText(getActivity(), "Can't find song!", Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "Can't find song  " + response.code());
                }
            }
            @Override
            public void onFailure(Call<SOSong> call, Throwable t) {
                Log.d("SearchFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void getSingerId() {
        mService.getSingerId(id).enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (id == response.body().get(i).getId()) {
                            singerName = response.body().get(i).getName();
                            break;
                        }
                    }
                    tvResultSearch.setText(singerName);
                    Toast.makeText(getActivity(), "Singer " + singerName, Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "Singer " + response.body());
                } else {
                    Toast.makeText(getActivity(), "Can't find singer!", Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "Can't find singer: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Singer>> call, Throwable t) {
                Log.d("SearchFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void deleteSong() {
        mService.deleteSong(MusicAppGlobal.getInstance().getAccessToken(), id).enqueue(new Callback<SOSong>() {
            @Override
            public void onResponse(Call<SOSong> call, Response<SOSong> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "The song " + response.body().getName() + " was deleted", Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "Delete the song successfully!");
                } else {
                    Toast.makeText(getActivity(), "Delete the song unsuccessfully", Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "Delete the song unsuccessfully: " +  response.code());
                }
            }
            @Override
            public void onFailure(Call<SOSong> call, Throwable t) {
                Log.d("SearchFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void deleteSinger() {
        mService.deleteSinger(MusicAppGlobal.getInstance().getAccessToken(), id).enqueue(new Callback<Singer>() {
            @Override
            public void onResponse(Call<Singer> call, Response<Singer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Singer " + response.body().getName() + " was deleted", Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "Delete singer successfully!");
                } else {
                    Toast.makeText(getActivity(), "Delete singer unsuccessfully!", Toast.LENGTH_SHORT).show();
                    Log.d("SearchFragment", "Delete singer unsuccessfully: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Singer> call, Throwable t) {
                Log.d("SearchFragment", "Error: " + t.getMessage());
            }
        });
    }
}