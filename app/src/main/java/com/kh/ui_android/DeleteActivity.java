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

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener{

    RadioButton rdoSearchName, rdoSearchMajor;
    EditText edtSearch;
    Button btnSearch, btnDelCancel, btnDelFin;
    TextView delNum, delName, delYear, delGender, delMajor, delScore;
    String select = "";
    ArrayList<StudentVo> list = new ArrayList<>();
    SQLiteOpenHelper helper = new MyDBHelper(DeleteActivity.this, "StudentDB", null, 1);
    ListView deleteListView;
    TextView deleteTxt, deleteTxt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_view);

        deleteListView = findViewById(R.id.deleteListView);
        deleteTxt = findViewById(R.id.deleteTxt);
        deleteTxt2 = findViewById(R.id.deleteTxt2);
        rdoSearchName = findViewById(R.id.rdoSearchName);
        rdoSearchMajor = findViewById(R.id.rdoSearchMajor);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnDelCancel = findViewById(R.id.btnDelCancel);
        btnDelFin = findViewById(R.id.btnDelFin);
        delNum = findViewById(R.id.delNum);
        delName = findViewById(R.id.delName);
        delYear = findViewById(R.id.delYear);
        delGender = findViewById(R.id.delGender);
        delMajor = findViewById(R.id.delMajor);
        delScore = findViewById(R.id.delScore);


        btnSearch.setOnClickListener(this);
        btnDelCancel.setOnClickListener(this);
        btnDelFin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // ??????
        if (v == btnSearch) {
            if (rdoSearchName.isChecked()) {
                select = "sname";
            }
            if (rdoSearchMajor.isChecked()) {
                select = "major";
            }
            String inputData = edtSearch.getText().toString();
            Log.d("mytag", select + inputData);
            if (inputData.equals("")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("??????");
                dialog.setMessage("???????????? ????????? ?????????.");
                dialog.setPositiveButton("??????", null);
                dialog.show();
            } else if( rdoSearchMajor.isChecked() == false && rdoSearchName.isChecked() == false) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("??????");
                dialog.setMessage("????????? ????????? ?????????.");
                dialog.setPositiveButton("??????", null);
                dialog.show();
            } else {
                deleteTxt.setVisibility(View.VISIBLE);
                deleteTxt2.setVisibility(View.VISIBLE);
                ArrayList<StudentVo> list = searchStudent(select, inputData);
                deleteListView.setAdapter(new StudentAdapter(this, R.layout.info_view, list));
                // ????????? ????????? ???????????? ??? editText??? ??????
                deleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StudentVo vo = list.get(position);
                        delNum.setText(vo.getSno());
                        delName.setText(vo.getSname());
                        delYear.setText(String.valueOf(vo.getSyear()));
                        delGender.setText(vo.getGender());
                        delMajor.setText(vo.getMajor());
                        delScore.setText(String.valueOf(vo.getScore()));
                    }
                });
            }
        }
        // ????????????
        if (v == btnDelFin) {
            deleteStudent();
        }
        // ??????
        if (v == btnDelCancel) {
            finish();
        }
    }

    // ?????? ??????
    private void deleteStudent() {
        // ?????? ??????
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("??????");
        dialog.setTitle("?????? ????????? ????????? ?????? ?????????????????????????");
        dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sno = delNum.getText().toString();
                String sql = "delete from tbl_student" +
                        "     where sno = '" + sno + "'";
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL(sql);
                db.close();
                showDialog();
            }
        });
        dialog.setNegativeButton("??????", null);
        dialog.show();
    }

    // ????????????
    private ArrayList<StudentVo> searchStudent(String select, String inputData) {
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
}