package com.example.practice.ui.gallery;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.practice.MainActivity;
import com.example.practice.R;
import com.example.practice.databinding.FragmentGalleryBinding;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private static Service API_SERVICE;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        final  Button BtnConsultar= binding.btnConsultar;
        BtnConsultar.setOnClickListener(new View.OnClickListener() {
            final  TextView textoFact=binding.txtConsulta;
            final  TextView textoLength=binding.txtLength;
            final TextView textoActualizado=binding.txtVActualizar;
            final TextView editarEntrada=binding.edTextEntrada;
            final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            @Override
            public void onClick(View v) {


                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                // Asociamos el interceptor a las peticiones
                final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(logging);
                    Retrofit retrofit=new Retrofit.Builder()
                            .baseUrl("https://catfact.ninja/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();

                    API_SERVICE=retrofit.create(Service.class);
                    Call<CatFact> call=API_SERVICE.getElemento();

                    call.enqueue(new Callback<CatFact>() {
                        @Override
                        public void onResponse(Call<CatFact> call, Response<CatFact> response) {
                            try {
                                if(response.isSuccessful()){
                                    CatFact elemento=response.body();
                                    textoFact.setText(elemento.getFact());
                                    textoLength.setText(elemento.getLength());
                                }

                            }catch (Exception ex){
                                Toast.makeText(getContext(), "Error al realizar la Petición", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CatFact> call, Throwable t) {
                        }
                    });
                textoActualizado.setText(editarEntrada.getText());

                }
        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}