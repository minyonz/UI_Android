package com.kh.ui_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{

    RadioButton searchName, searchMajor, updateFemale, updateMale;
    Button btnUpdateSearch, btnUpdateCancel, btnUpdateFin;
    EditText edtUpdateSearch, updateNum, updateName, updateYear, updateMajor, updateScore;
    String select = "";
    ArrayList<StudentVo> list = new ArrayList<>();
    SQLiteOpenHelper helper = new MyDBHelper(UpdateActivity.this, "StudentDB", null, 1);
    ListView updateListView;
    TextView updateTxt, updateTxt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_view);

        updateTxt = findViewById(R.id.updateTxt);
        updateTxt2 = findViewById(R.id.updateTxt2);
        updateListView = findViewById(R.id.updateListView);
        searchName = findViewById(R.id.searchName);
        searchMajor = findViewById(R.id.searchMajor);
        edtUpdateSearch = findViewById(R.id.edtUpdateSearch);
        btnUpdateSearch = findViewById(R.id.btnUpdateSearch);
        btnUpdateCancel = findViewById(R.id.btnUpdateCancel);
        btnUpdateFin = findViewById(R.id.btnUpdateFin);
        updateNum = findViewById(R.id.updateNum);
        updateName = findViewById(R.id.updateName);
        updateYear = findViewById(R.id.updateYear);
        updateFemale = findViewById(R.id.updateFemale);
        updateMale = findViewById(R.id.updateMale);
        updateMajor = findViewById(R.id.updateMajor);
        updateScore = findViewById(R.id.updateScore);

        btnUpdateSearch.setOnClickListener(this);
        btnUpdateCancel.setOnClickListener(this);
        btnUpdateFin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // ??????
        if (v == btnUpdateSearch) {
            if (searchName.isChecked()) {
                select = "sname";
            }
            if (searchMajor.isChecked()) {
                select = "major";
            }

            String inputData = edtUpdateSearch.getText().toString();
            if (inputData.equals("")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("??????");
                dialog.setMessage("???????????? ????????? ?????????.");
                dialog.setPositiveButton("??????", null);
                dialog.show();
            } else if( searchName.isChecked() == false && searchMajor.isChecked() == false) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("??????");
                dialog.setMessage("????????? ????????? ?????????.");
                dialog.setPositiveButton("??????", null);
                dialog.show();
            } else {
                updateTxt.setVisibility(View.VISIBLE);
                updateTxt2.setVisibility(View.VISIBLE);
                ArrayList<StudentVo> list = searchStudent(select, inputData);
                // ????????????????????? ?????? ????????? ?????????
                updateListView.setAdapter(new StudentAdapter(this, R.layout.info_view, list));
                // ????????? ????????? ???????????? ??? editText??? ??????
                updateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StudentVo vo = list.get(position);
                        updateNum.setText(vo.getSno());
                        updateName.setText(vo.getSname());
                        updateYear.setText(String.valueOf(vo.getSyear()));
                        if (vo.getGender().equals("???")) {
                            updateMale.setChecked(true);
                        } else if (vo.getGender().equals("???")) {
                            updateFemale.setChecked(true);
                        }
                        updateMajor.setText(vo.getMajor());
                        updateScore.setText(String.valueOf(vo.getScore()));
                    }
                });
            }
        }
        // ????????????
        if (v == btnUpdateFin) {
            updateStudent();
        }
        // ????????????
        if (v == btnUpdateCancel) {
            finish();
        }
    }

    // ?????? ??????
    private void updateStudent() {
        String sno = updateNum.getText().toString();
        String sname = updateName.getText().toString();
        String strYear = updateYear.getText().toString();
        String gender = "";
        if (updateMale.isChecked()) {
            gender = updateMale.getText().toString();
        } else if (updateFemale.isChecked()) {
            gender = updateFemale.getText().toString();
        }
        String major = updateMajor.getText().toString();
        String strScore = updateScore.getText().toString();

        boolean result = checkValue(sno, sname, gender, strYear, major, strScore);
        if (result == true) {
            int syear = Integer.parseInt(strYear);
            int score = Integer.parseInt(strScore);
            String sql = "update tbl_student set" +
                    "       sname = '" + sname + "'," +
                    "       syear = " + syear + "," +
                    "       gender = '" + gender + "'," +
                    "       major = '" + major + "'," +
                    "       score = " + score +
                    "       where sno = '" + sno + "'";
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL(sql);
            db.close();
            showDialog();
        }
    }

    // ???????????? ???????????????
    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("??????");
        dialog.setMessage("?????? ??????");
        dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    // ??? ??? ???????????????
   private void showEmptyDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("??????");
        dialog.setMessage("?????? ????????? ????????? ???????????? ?????????.");
        dialog.setPositiveButton("??????", null);
        dialog.show();
    }

    // ????????????
    private ArrayList<StudentVo> searchStudent(String select, String inputData) {
        Log.d("mytag", select + inputData);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from tbl_student" +
                "     where " + select + " like '%" + inputData + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String sno = cursor.getString(0);
            String sname = cursor.getString(1);
            int syear = cursor.getInt(2);
            String gender = cursor.getString(3);
            String major = cursor.getString(4);
            int score = cursor.getInt(5);

            StudentVo vo = new StudentVo(sno, sname, syear, gender, major, score);
            list.add(vo);
        }
        db.close();
        return list;
    }

    // ????????? ??????
    private boolean checkValue(String sno, String sname, String gender, String strYear, String major, String strScore) {
        // ??? ?????? ???
        if (sno.equals("") || sname.equals("") || gender.equals("") || major.equals("")
                || strYear.equals("") || strScore.equals("")) {
            showEmptyDialog();
            return false;
        }
        // ??????????????? ???????????? ?????? ????????? ???
        try {
            int syear = Integer.parseInt(strYear);
            if (!(syear >= 1 && syear <= 4)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("??????");
                dialog.setMessage("????????? 1~4??? ????????? ????????? ?????????.");
                dialog.setPositiveButton("??????", null);
                dialog.show();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("??????");
            dialog.setMessage("????????? ????????? ????????? ?????????.");
            dialog.setPositiveButton("??????", null);
            dialog.show();
            return false;
        }
        try {
            int score = Integer.parseInt(strScore);
            if (!(score >= 0 && score <= 100)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("??????");
                dialog.setMessage("?????????1~100??? ????????? ????????? ?????????.");
                dialog.setPositiveButton("??????", null);
                dialog.show();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("??????");
            dialog.setMessage("????????? ????????? ????????? ?????????.");
            dialog.setPositiveButton("??????", null);
            dialog.show();
            return false;
        }
        return true;
    }
}