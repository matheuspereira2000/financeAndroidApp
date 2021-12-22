package com.example.android.effectivenavigation;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.effectivenavigation.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class TransactionFragment extends Fragment {
    //XML Attributes to connect to our java files
    private Button fab_main, fab_income_button, fab_expense_button;
    private TextView income_text, expense_text;
    private TextView totalIncomeResult, totalExpenseResult;
    //Is the plus button open?
    private boolean isOpen = false;
    //Animation Objects
    private Animation fadeOpen, fadeClose;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    private DatabaseReference allTransactions;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Data, TransactionFragment.MyViewHolder> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        allTransactions = FirebaseDatabase.getInstance().getReference().child("TransactionData").child(uid);

        //Total income and expense
        totalIncomeResult = rootView.findViewById(R.id.income_set_result);
        totalExpenseResult = rootView.findViewById(R.id.expense_set_result);

        fab_main = rootView.findViewById(R.id.main_plus_button);
        fab_income_button = rootView.findViewById(R.id.income_button);
        fab_expense_button = rootView.findViewById(R.id.expense_button);
        recyclerView = rootView.findViewById(R.id.recycler_id_transaction);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        income_text = rootView.findViewById(R.id.income_ft_text);
        expense_text = rootView.findViewById(R.id.expense_ft_text);

        fadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
                if (isOpen) {
                    fab_income_button.startAnimation(fadeClose);
                    fab_expense_button.startAnimation(fadeClose);
                    fab_income_button.setClickable(false);
                    fab_expense_button.setClickable(false);

                    income_text.startAnimation(fadeClose);
                    expense_text.startAnimation(fadeClose);
                    income_text.setClickable(false);
                    expense_text.setClickable(false);
                    isOpen=false;
                } else {
                    fab_income_button.startAnimation(fadeOpen);
                    fab_expense_button.startAnimation(fadeOpen);
                    fab_income_button.setClickable(true);
                    fab_expense_button.setClickable(true);

                    income_text.startAnimation(fadeOpen);
                    expense_text.startAnimation(fadeOpen);
                    income_text.setClickable(true);
                    expense_text.setClickable(true);
                    isOpen=true;
                }
            }
        });
        //Calculate total income
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalSum = 0;
                for (DataSnapshot mysnap:snapshot.getChildren()) {
                    Data data = mysnap.getValue(Data.class);
                    totalSum += data.getAmount();
                    String stResult = String.valueOf(totalSum);
                    totalIncomeResult.setText(stResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Calculate total expenses
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalSum = 0;
                for (DataSnapshot mysnap:snapshot.getChildren()) {
                    Data data = mysnap.getValue(Data.class);
                    totalSum += data.getAmount();
                    String stResult = String.valueOf(totalSum);
                    totalExpenseResult.setText(stResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rootView;
    }


    private void addData() {
        fab_income_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeDataInsert();
            }
        });

        fab_expense_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseDataInsert();
            }
        });
    }

    public void incomeDataInsert() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_inserting_data, null);
        myDialog.setView(myView);
        AlertDialog dialog = myDialog.create();

        EditText editAmount = myView.findViewById(R.id.amount_edit);
        EditText editType = myView.findViewById(R.id.type_edit);
        EditText editNote = myView.findViewById(R.id.note_edit);
        Button save = myView.findViewById(R.id.saveButton);
        Button cancel = myView.findViewById(R.id.cancelButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)) {
                    editType.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(amount)) {
                    editAmount.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(note)) {
                    editNote.setError("Required Field...");
                    return;
                }

                int amountInt = Integer.parseInt(amount);
                String id = mIncomeDatabase.push().getKey();
                String mDate= DateFormat.getInstance().format(new Date());
                Data data = new Data(amountInt, type, note, id, mDate);
                mIncomeDatabase.child(id).setValue(data);
                allTransactions.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data added...", Toast.LENGTH_SHORT).show();
                retrieveTransactionData(true);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void expenseDataInsert() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_inserting_data, null);
        myDialog.setView(myView);
        AlertDialog dialog = myDialog.create();

        EditText editAmount = myView.findViewById(R.id.amount_edit);
        EditText editType = myView.findViewById(R.id.type_edit);
        EditText editNote = myView.findViewById(R.id.note_edit);
        Button save = myView.findViewById(R.id.saveButton);
        Button cancel = myView.findViewById(R.id.cancelButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)) {
                    editType.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(amount)) {
                    editAmount.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(note)) {
                    editNote.setError("Required Field...");
                    return;
                }

                int amountInt = Integer.parseInt(amount)*-1;
                String id = mExpenseDatabase.push().getKey();
                String mDate= DateFormat.getInstance().format(new Date());
                Data data = new Data(amountInt, type, note, id, mDate);
                mExpenseDatabase.child(id).setValue(data);
                allTransactions.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data added...", Toast.LENGTH_SHORT).show();
                retrieveTransactionData(false);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void retrieveTransactionData(boolean x) {
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>().setQuery(allTransactions, Data.class).build();
        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false));
            }

            protected void onBindViewHolder(MyViewHolder holder, int position, @NonNull Data model) {
                holder.setAmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public MyViewHolder(View view) {
            super(view);
            mView = view;
        }
        private void setType(String type){
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private void setNote(String note){
            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void setDate(String date){
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        private void setAmount(int amount){
            TextView mAmount = mView.findViewById(R.id.amount_txt_income);
            String strAmount = String.valueOf(amount);
            mAmount.setText(strAmount);
        }

    }
}