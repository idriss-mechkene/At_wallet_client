package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alliancetechnologie.at_wallet_client.Helper.RecyclerViewClickListener;
import com.alliancetechnologie.at_wallet_client.Helper.RecyclerViewTouchListener;
import com.alliancetechnologie.at_wallet_client.adapter.HistoryAdapter;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.ETransactionType;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.FilterConditions;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.utils.dialog.BottomSheetFilterFragment;
import com.alliancetechnologie.at_wallet_client.utils.dialog.OnFilterInterface;
import com.alliancetechnologie.at_wallet_client.viewmodel.TransactionViewModel;
import com.alliancetechnologie.at_wallet_client.webservice.service.transaction.TransactionServiceInterface;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryActivity extends AppCompatActivity implements OnFilterInterface, NavigationBarView.OnItemSelectedListener, TransactionServiceInterface, MaterialButtonToggleGroup.OnButtonCheckedListener, View.OnClickListener {

    MaterialButtonToggleGroup toggleButtonTypeHistory;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TransactionViewModel transactionViewModel;
    SkeletonScreen skeletonScreen;
    HistoryAdapter historyAdapter;
    CustomerAlertDialog customerAlertDialog;
    List<Transactions> Data = new ArrayList<>();
    MaterialButton btnAll, btnCredit, btnDebit;
    FilterConditions filterConditions;
    ETransactionType eTransactionType;
    String searchTxt = "";
    TextView textReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.setTransactionServiceInterface(this);
        historyAdapter = new HistoryAdapter();
        recyclerView.setAdapter(historyAdapter);

        skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(historyAdapter)
                .count(10)
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
        transactionViewModel.getTransactions(RSUserSession.getToken(this), RSUserSession.getIdAccount(this))
                .observe(this, transactions -> {
                    swipeRefreshLayout.setRefreshing(false);
                    historyAdapter.setList(transactions);
                    skeletonScreen.hide();
                    Data = transactions;
                    toggleButtonTypeHistory.check(R.id.Btn_All);

                });
    }

    @Override
    public void onError(String msg, int code) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        //Log.e("TAG", "onButtonChecked: checkedId:" + checkedId + " isChecked:" + isChecked);
        switch (group.getCheckedButtonId()) {
            case R.id.Btn_All:

                filterConditions.setETransactionType(null);
                applyfilter(filterConditions);
//                historyAdapter.setList(historyAdapter.getFullData());
                break;
            case R.id.Btn_Credit:
                filterConditions.setETransactionType(ETransactionType.CREDIT);

                applyfilter(filterConditions);

//                    List<Transactions> list = Data.stream()
//                            .filter(transactions -> ETransactionType.CREDIT.equals(transactions.getTransactionType()))
//                            .collect(Collectors.toList());
//                    Log.e("TAG", "filter: " + list.size());

                //filter(ETransactionType.CREDIT.getName());
                break;
            case R.id.Btn_Debit:
                filterConditions.setETransactionType(ETransactionType.DEBIT);

                applyfilter(filterConditions);
//                    List<Transactions> list = Data.stream()
//                            .filter(transactions -> ETransactionType.CREDIT.equals(transactions.getTransactionType()))
//                            .collect(Collectors.toList());
//                    Log.e("TAG", "filter: " + list.size());

//                filter(ETransactionType.DEBIT.getName());
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        MenuItem filterIcon = menu.findItem(R.id.action_filter);
        filterIcon.setOnMenuItemClickListener(menuItem -> {
            BottomSheetFilterFragment bottomSheetFilterFragment = BottomSheetFilterFragment.newInstance("Filter");
            bottomSheetFilterFragment.setFilterFragmentInterfaceAndFilter(this, filterConditions);
            Log.d("TAG", "onCreateOptionsMenu: " + filterConditions);
            bottomSheetFilterFragment.show(getSupportFragmentManager(), bottomSheetFilterFragment.getTag());
            return false;
        });
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTxt = newText;
                historyAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        recyclerView = findViewById(R.id.history_recycleView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toggleButtonTypeHistory = findViewById(R.id.toggleButton);
        btnAll = findViewById(R.id.Btn_All);
        btnCredit = findViewById(R.id.Btn_Credit);
        btnDebit = findViewById(R.id.Btn_Debit);
        textReset = findViewById(R.id.txt_reset);

        eTransactionType = null;
        filterConditions = new FilterConditions();
        textReset.setOnClickListener(this);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnItemSelectedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        toggleButtonTypeHistory.addOnButtonCheckedListener(this);

        toolbar.setTitle(R.string.search);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return Utils.NavigationMenus(this, item);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onConfirmFilter(FilterConditions filterConditions) {
        this.filterConditions = filterConditions;
        applyfilter(this.filterConditions);

    }

    private void applyfilter(FilterConditions filterConditions) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            historyAdapter.setListWithoutNotify(Data.stream()
                    .filter(transactions -> FilterConditions.TransactionInConditions(transactions, filterConditions))
                    .collect(Collectors.toList()));
        }
        historyAdapter.getFilter().filter(searchTxt);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_reset:
                historyAdapter.setList(Data);
                this.filterConditions = new FilterConditions();
                toggleButtonTypeHistory.check(R.id.Btn_All);
                break;
        }
    }
}