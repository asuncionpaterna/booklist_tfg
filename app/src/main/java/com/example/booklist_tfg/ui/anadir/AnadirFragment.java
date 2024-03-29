package com.example.booklist_tfg.ui.anadir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnadirFragment extends Fragment {
    private RequestQueue mRequestQueue;
    private ArrayList<Libro> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private Button searchBtn;
    RecyclerView mRecyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anadir, container, false);
        // initializing our views.
        progressBar = view.findViewById(R.id.idLoadingPB);
        searchEdt =  view.findViewById(R.id.idEdtSearchBooks);
        searchBtn =  view.findViewById(R.id.idBtnSearch);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.idRVBooks);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                // checking if our edittext field is empty or not.
                if (searchEdt.getText().toString().isEmpty()) {
                    searchEdt.setError("Please enter search query");
                    return;
                }
                // if the search query is not empty then we are
                // calling get book info method to load all
                // the books from the API.
                getBooksInfo(searchEdt.getText().toString());
            }
        });







        return view;
    }
    private void getBooksInfo(String query) {

        // creating a new array list.
        bookInfoArrayList = new ArrayList<>();

        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(getContext());

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        // below line we are creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        System.out.println(url);
        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                // inside on response method we are extracting all our json data.
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        try {
                            JSONObject itemsObj = itemsArray.getJSONObject(i);
                            JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                            String titulo = volumeObj.optString("title");

                            JSONArray authorsArray = volumeObj.getJSONArray("authors");
                            ArrayList<String> authorsArrayList = new ArrayList<>();
                            if (authorsArray.length() != 0) {
                                for (int j = 0; j < authorsArray.length(); j++) {
                                    authorsArrayList.add(authorsArray.optString(j));
                                }
                            }

                            String genero = volumeObj.optString("categories");
                            String anioPublicacion = volumeObj.optString("publishedDate");
                            String editorial = volumeObj.optString("publisher");
                            int paginas = volumeObj.optInt("pageCount");
                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                            String portada = imageLinks.optString("thumbnail");
                            // after extracting all the data we are
                            // saving this data in our modal class.

                          // below line is use to ---st."-
                            Libro bookInfo = new Libro(titulo,authorsArrayList,editorial,genero,anioPublicacion,paginas,portada);
                            bookInfoArrayList.add(bookInfo);



                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        // below line is use to pass our
                        // array list in adapter class.
                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, getContext());


                        // below line is use to add linear layout
                        // manager for our recycler view.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                        // in below line we are setting layout manager and
                        // adapter to our recycler view.
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
                    Toast.makeText(getContext(), "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also displaying error message in toast.
                Toast.makeText(getContext(), "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest);
    }
}