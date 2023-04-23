package com.swj.tp08memberlistrefectoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.swj.tp08memberlistrefectoring.databinding.ActivityMainBinding;
import com.swj.tp08memberlistrefectoring.databinding.DialogBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String spSelectedNation = "";
    int spSelectedNationArrNumber = 0;
    MyAdapter adapter;
    ArrayAdapter arrayAdapter;
    SearchView searchView;
    ArrayList<Item> itemArrayList = new ArrayList<>();
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        registerForContextMenu(binding.listview);
        adapter = new MyAdapter(MainActivity.this, itemArrayList);
        binding.listview.setAdapter(adapter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // onCreate 메소드가 실행된 후 자동으로 Option Menu를 만드는 작업을 하는
    // 이 콜백 메소드가 자동 호출
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("이름을 입력하세요.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Item> searchResult = new ArrayList<>();
                for(Item item : itemArrayList) {
                    String name = item.name;
                    if(name.contains(query))
                        searchResult.add(item);
                }
                adapter = new MyAdapter(MainActivity.this, searchResult);
                binding.listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            adapter = new MyAdapter(MainActivity.this, itemArrayList);
            binding.listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }


    // 옵션 메뉴의 특정 아이템을 클릭했을 때 호출
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_member_add) { // 멤버 추가 다이얼로그
            DialogBinding dialogBinding = DialogBinding.inflate(getLayoutInflater());

            AlertDialog dialog = new AlertDialog.Builder(this)
                                    .setView(dialogBinding.getRoot())
                                    .create();
            dialog.show();

            arrayAdapter = ArrayAdapter.createFromResource
                    (this, R.array.nation, R.layout.spinner_item);
            dialogBinding.diaSpNation.setAdapter(arrayAdapter);
            dialogBinding.diaSpNation.setDropDownVerticalOffset(110);

            dialogBinding.diaSpNation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String[] nationArr = getResources().getStringArray(R.array.nation);
                    spSelectedNation = nationArr[i];
                    spSelectedNationArrNumber = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            dialogBinding.diaBtnMemberAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.tvNoData.setVisibility(View.GONE);
                    binding.listview.setVisibility(View.VISIBLE);

                    int genderId = dialogBinding.diaRgGender.getCheckedRadioButtonId();
                    RadioButton genderRb = dialog.findViewById(genderId);

                    itemArrayList.add(0,
                            new Item(
                                    dialogBinding.diaEtName.getText().toString(),
                                    spSelectedNation,
                                    genderRb.getText().toString(),
                                    R.drawable.flag_australia + spSelectedNationArrNumber));
                    adapter.notifyDataSetChanged();
                    binding.listview.setSelection(0);
                    dialog.dismiss();
                }
            });

            dialogBinding.diaBtnCancel.setOnClickListener(view -> dialog.dismiss());
        } // if(item.getItemId() == R.id.menu_member_add)
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected 메소드

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if(item.getItemId() == R.id.context_menu_delete) {
            itemArrayList.remove(index);
            adapter.notifyDataSetChanged();
        } else if(item.getItemId() == R.id.context_menu_modify)
            Toast.makeText(this, "modify : " + index, Toast.LENGTH_SHORT).show();
        else if(item.getItemId() == R.id.context_menu_info)
            Toast.makeText(this, "info : " + index, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }
}