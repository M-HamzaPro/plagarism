package com.example.fypwebhost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentAssignment extends AppCompatActivity {

    ResultsModelClass resultsModelClass;
    ListView listView;
    ResultsAdapter adapter;
    public static ArrayList<ResultsModelClass> resultArrayList = new ArrayList<>();
    String classID, assignmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment);

        listView = findViewById(R.id.lestViewResult);

        classID = getIntent().getStringExtra("classID");
        assignmentID = getIntent().getStringExtra("assignmentID");

        Toast.makeText(StudentAssignment.this, "Class Id "+classID+" ass "+assignmentID, Toast.LENGTH_SHORT).show();
        retrieveResult();
    }

    public void retrieveResult()
    {
        resultArrayList.clear();

        StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/getResultTeacher.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            if(success.equals("1")){
//                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(StudentAssignment.this, "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(success.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
//                                        String assignmentID = object.getString("assignmentID");
                                        String similarity = object.getString("resultt");
//                                        String assignmentDueDate =object.getString("assignmentDueDate");
//                                        String assignmentPostDate =object.getString("assignmentPostDate");

                                        resultArrayList.add(
                                                new ResultsModelClass(similarity)
                                        );
                                    }
                                    adapter=new ResultsAdapter(StudentAssignment.this ,resultArrayList);
                                    adapter.notifyDataSetChanged();
                                    listView.setAdapter(adapter);
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentAssignment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("classID", classID);
                params.put("assignmentID", assignmentID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentAssignment.this);
        requestQueue.add(request);
    }
}