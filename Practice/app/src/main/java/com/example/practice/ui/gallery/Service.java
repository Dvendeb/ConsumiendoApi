package com.example.practice.ui.gallery;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    String API_ROUTE="/fact";

    @GET(API_ROUTE)
    Call<CatFact> getElemento();
}
