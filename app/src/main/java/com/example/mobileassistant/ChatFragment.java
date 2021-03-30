package com.example.mobileassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private static final int YES = 0;
    private static final int NO = 1;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the view for the fragment.
     *
     * @param inflater           LayoutInflater to inflate any views in the fragment
     * @param container          ViewGroup of parent view to attach fragment
     * @param savedInstanceState Bundle for previous state
     * @return rootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        final View rootView = inflater.inflate(R.layout.fragment_chat,
                container, false);
        final TextView textView = rootView.findViewById(R.id.command_list);
        textView.setMovementMethod(new ScrollingMovementMethod());
        // Return the View for the fragment's UI.
        return rootView;
    }
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }
}