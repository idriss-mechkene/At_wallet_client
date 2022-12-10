package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alliancetechnologie.at_wallet_client.Helper.RecyclerViewClickListener;
import com.alliancetechnologie.at_wallet_client.Helper.RecyclerViewTouchListener;
import com.alliancetechnologie.at_wallet_client.adapter.HistoryAdapter;
import com.alliancetechnologie.at_wallet_client.entity.Operator;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.viewmodel.TransactionViewModel;
import com.alliancetechnologie.at_wallet_client.webservice.service.recharge.RechargeServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.recharge.RechargeServiceInterface;
import com.alliancetechnologie.at_wallet_client.webservice.service.transaction.TransactionServiceInterface;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener, TransactionServiceInterface, RechargeServiceInterface {

    BottomNavigationView bottomNavigationView;
    MaterialCardView CardRecharge, CardPayment;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView TxtAll, TxtName, TxtBalance, TxtAccountNumber;
    HistoryAdapter historyAdapter;
    TransactionViewModel transactionViewModel;
    SkeletonScreen skeletonScreen;
    CustomerAlertDialog customerAlertDialog;
    RechargeServiceImpl rechargeService;
    Dialog dialogLoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        initUserData();

        rechargeService = new RechargeServiceImpl(this, this);
        customerAlertDialog = new CustomerAlertDialog(this);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.setTransactionServiceInterface(this);
        historyAdapter = new HistoryAdapter();
        recyclerView.setAdapter(historyAdapter);

        skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(historyAdapter)
                .count(5)
                .frozen(false)
                .load(R.layout.skeleton_item_view)
                .show();
        RefreshTransaction();
        swipeRefreshLayout.setOnRefreshListener(this::RefreshTransaction);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                customerAlertDialog.showDetailTransactionDialog(historyAdapter.getTransactionAt(position), view.getContext());
            }

            @Override
            public void onLongClick(View view, int position) {
            }

        }));
    }

    private void RefreshTransaction() {
        transactionViewModel.getLast5Transaction(RSUserSession.getToken(this), RSUserSession.getIdAccount(this))
                .observe(this, transactions -> {
                    swipeRefreshLayout.setRefreshing(false);
                    historyAdapter.setList(transactions);
                    skeletonScreen.hide();
                });
    }

    private void initUserData() {
        try {
            User user = RSUserSession.getLocalStorage(this);
            TxtBalance.setText(Utils.showFormattedAmount(user.getAccountBalance()));
            TxtName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            Log.e("TAG", "initUserData: " + user.getAccountNumber());
            TxtAccountNumber.setText(user.getAccountNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessListOperator(List<Operator> operators) {
        dialogLoder.dismiss();
        if (operators.size() > 0) {
            Intent intent = new Intent(getApplicationContext(), RechargeActivity.class);
            // Log.e("tag", "onSuccessListOperator: " + operators);
            intent.putExtra("OperatorsList", new Gson().toJson(operators));
            startActivity(intent);
        } else {
            Log.d("tag", "onSuccessListOperator: " + "Aucun operateur");
            customerAlertDialog.showErrorDialog("Service Indisponible");
        }
    }

    @Override
    public void onSuccessTopUp(Transactions transactions) {

    }

    @Override
    public void onError(String msg, int code) {
        Log.e("TAG", "onError: " + msg + " " + code);
        dialogLoder.dismiss();
        if (code == 0) {
            customerAlertDialog.showErrorDialog(msg);
        } else {
            customerAlertDialog.showErrorDialog(code + ": " + msg);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_recharge:
                // startActivity(new Intent(getApplicationContext(), RechargeActivity.class));
                dialogLoder = Utils.StartLoader(this);
                rechargeService.getOperator(RSUserSession.getToken(this));
                break;
            case R.id.card_payment:
                startActivity(new Intent(getApplicationContext(), QrCodeActivity.class));
                break;
            case R.id.txt_all:
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return Utils.NavigationMenus(this, item);
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        CardRecharge = findViewById(R.id.card_recharge);
        CardPayment = findViewById(R.id.card_payment);
        recyclerView = findViewById(R.id.recycleView);
        TxtAll = findViewById(R.id.txt_all);
        TxtName = findViewById(R.id.txt_name);
        TxtBalance = findViewById(R.id.txt_balance);
        TxtAccountNumber = findViewById(R.id.txt_account_number);


        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        CardRecharge.setOnClickListener(this);
        CardPayment.setOnClickListener(this);
        TxtAll.setOnClickListener(this);

        rechargeService = new RechargeServiceImpl(this, this);

    }

    @Override
    public void onBackPressed() {
    }
}