package com.wanderease.travelcompanion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView textViewName, textViewEmail;
    private Button buttonChangePassword, buttonLogout;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        // Display user information
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            textViewName.setText(name != null ? name : "Name: N/A");
            textViewEmail.setText(email != null ? email : "Email: N/A");
        }

        // Handle change password button click
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the custom layout
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

                EditText editTextCurrentPassword = dialogView.findViewById(R.id.editTextCurrentPassword);
                EditText editTextNewPassword = dialogView.findViewById(R.id.editTextNewPassword);
                EditText editTextConfirmPassword = dialogView.findViewById(R.id.editTextConfirmPassword);
                Button buttonChange = dialogView.findViewById(R.id.buttonChange);

                // Create a dialog without AlertDialog
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(dialogView);

                // Handle click event of "Change Password" button
                buttonChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentPassword = editTextCurrentPassword.getText().toString().trim();
                        String newPassword = editTextNewPassword.getText().toString().trim();
                        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                        // Check if new password and confirm password match
                        if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(getActivity(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Re-authenticate the user to change password
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                            user.reauthenticate(credential)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Change password
                                            user.updatePassword(newPassword)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss(); // Dismiss the dialog after successful password change
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Failed to re-authenticate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                // Show the dialog
                dialog.show();
            }
        });

        // Handle logout button click
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                mAuth.signOut();
                // Clear any saved user data
                textViewName.setText("Name: N/A");
                textViewEmail.setText("Email: N/A");
                // Redirect to the login activity
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}
