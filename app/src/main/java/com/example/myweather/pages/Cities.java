package com.example.myweather.pages;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.R;
import com.example.myweather.adapter.CityAdapter;
import com.example.myweather.data.GetAllOperation;
import com.example.myweather.data.Location;
import com.example.myweather.data.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class Cities extends Fragment implements OnItemClickListener, LocationRepository {

    private List<Location> cities = new ArrayList<>();
    private CityAdapter adapter;
    private EditText filter;
    private Button add;
    RecyclerView rv;

    public Cities() { super(R.layout.cities);}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater,container,savedInstanceState);

        add = v.findViewById(R.id.add);
        filter = v.findViewById(R.id.searchCity);


        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Location> citiesFiltered = new ArrayList<>();
                for(Location city : cities){
                    if(city.getCity().toLowerCase().contains(editable.toString().toLowerCase())){
                        citiesFiltered.add(city);
                    }
                }
                adapter.submit(citiesFiltered);
            }
        });


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setVisibility(View.GONE);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right,R.anim.exit_left_to_right);
                Fragment frg = new NewCity();
                fragmentTransaction.replace(R.id.cities,frg);
                fragmentTransaction.commit();
            }
        });
        rv = view.findViewById(R.id.recycle_view_cities);
        new GetAllOperation(this).execute(new Object());
    }

    @Override
    public void onItemClick(Location city) {

        add.setVisibility(View.GONE);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                R.anim.enter_left_to_right,R.anim.exit_left_to_right);
        Fragment frg = new CityDetails();
        Bundle bundle = new Bundle();
        bundle.putString("city",city.getCity());
        bundle.putBoolean("selected", city.getSelected());
        frg.setArguments(bundle);
        fragmentTransaction.replace(R.id.cities,frg);
        fragmentTransaction.commit();
    }

    @Override
    public void getAll(List<Location> locations) {
        cities = locations;
        adapter = new CityAdapter(cities, this);
        if(adapter.getItemCount() == 0){
            filter.setVisibility(View.GONE);
        }else{
            filter.setVisibility(View.VISIBLE);
        }
        rv.setAdapter(adapter);
    }

    @Override
    public void add(String result) {

    }

    @Override
    public void update(String result) {

    }

    @Override
    public void findByCity(Location loc) {

    }

    @Override
    public void findBySelected(Location loc) {

    }

    @Override
    public void delete(String result) {

    }
}
