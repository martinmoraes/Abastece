package br.com.appviral.abastece;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SobreFragment extends Fragment {

    public SobreFragment() {
    }


    public static SobreFragment newInstance() {
        SobreFragment fragment = new SobreFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sobre, container, false);

    }

}
