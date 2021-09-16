package com.kh.ui_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnRegist, btnInquiry, btnUpdate, btnDelete, mainBtnSearch, btnCheck;
    ListView listView;
    TextView txtCheck;
    View dialogView;
    EditText registNum, registName, registYear, registMajor, registScore, mainEdtSearch;
    RadioButton registFemale, registMale, mainRdoSname, mainRdoSmajor;
    SQLiteOpenHelper helper = new MyDBHelper(MainActivity.this, "StudentDB", null, 1);
    ArrayList<StudentVo> list = new ArrayList<>();
    // 검색 rdo 텍스트값 얻기
    String select = "";
    // 학번 중복 체크
    String sno = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBtnSearch = findViewById(R.id.mainBtnSearch);
        btnRegist = findViewById(R.id.btnRegist);
        btnInquiry = findViewById(R.id.btnInquiry);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);
        mainEdtSearch = findViewById(R.id.mainEdtSearch);
        mainRdoSmajor = findViewById(R.id.mainRdoSmajor);
        mainRdoSname = findViewById(R.id.mainRdoSname);

        mainBtnSearch.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
        btnInquiry.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        // 리스트 목록 눌렀을 때 학생 정보 상세 보기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.info_detail_view, null);
                TextView infoDeNum = dialogView.findViewById(R.id.infoDeNum);
                TextView infoDeName = dialogView.findViewById(R.id.infoDeName);
                TextView infoDeYear = dialogView.findViewById(R.id.infoDeYear);
                TextView infoDeGender = dialogView.findViewById(R.id.infoDeGender);
                TextView infoDeMajor = dialogView.findViewById(R.id.infoDeMajor);
                TextView infoDeScore = dialogView.findViewById(R.id.infoDeScore);
                StudentVo vo = list.get(position);
                infoDeNum.setText(vo.getSno());
                infoDeName.setText(vo.getSname());
                infoDeYear.setText(String.valueOf(vo.getSyear()));
                infoDeGender.setText(vo.getGender());
                infoDeMajor.setText(vo.getMajor());
                infoDeScore.setText(String.valueOf(vo.getScore()));
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("학생상세보기");
                dialog.setPositiveButton("닫기", null);
                dialog.setView(dialogView);
                dialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // 학생 찾기
        if (v == mainBtnSearch) {
            if (mainRdoSname.isChecked()) {
                select = "sname";
            }
            if (mainRdoSmajor.isChecked()) {
                select = "major";
            }
            String inputData = mainEdtSearch.getText().toString();
            Log.d("mytag", select + inputData);
            // 리스트를 한번 비워줌
            list.clear();
            if (inputData.equals("")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("알림");
                dialog.setMessage("검색어를 입력해 주세요.");
                dialog.setPositiveButton("확인", null);
                dialog.show();
            } else if( mainRdoSname.isChecked() == false && mainRdoSmajor.isChecked() == false) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("알림");
                dialog.setMessage("항목을 선택해 주세요.");
                dialog.setPositiveButton("확인", null);
                dialog.show();
            } else {
                ArrayList<StudentVo> list = searchStudent(select, inputData);
                listView.setAdapter(new StudentAdapter(this, R.layout.info_view, list));
            }
        }
        // 등록
        if (v == btnRegist) {
            showDialog();
        }
        // 조회
        if (v == btnInquiry) {
            inquiryStudent();
        }
        // 수정
        if (v == btnUpdate) {
            Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
            startActivity(intent);
        }
        // 삭제
        if (v == btnDelete) {
            Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
            startActivity(intent);
        }

    }

    // 학생찾기
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
        Toast.makeText(this, "조회완료", Toast.LENGTH_SHORT).show();
        return list;
    }

    // 전체 조회
    private ArrayList<StudentVo> inquiryStudent() {
        list.clear();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from tbl_student" +
                "     order by sno";
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
        cursor.close();
        db.close();
        listView.setAdapter(new StudentAdapter(this, R.layout.info_view, list));
        return list;
    }

    // 정보 입력
    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialogView = View.inflate(this, R.layout.regist_view, null);
        // 학번 체크용
        btnCheck = dialogView.findViewById(R.id.btnCheck);
        txtCheck = dialogView.findViewById(R.id.txtCheck);
        registNum = dialogView.findViewById(R.id.registNum);
        dialog.setView(dialogView);
        dialog.setTitle("학생 정보 입력");

        // 중복 학번 체크
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자 입력 값 구하기
                sno = registNum.getText().toString();
                Log.d("mytag", "클릭됨");
                boolean result = checkSno(sno);
                if (result == true && !sno.equals("")) {
                    txtCheck.setText("사용 가능한 학번입니다.");
                } else if (sno.equals("")){
                    txtCheck.setText("학번을 입력하고 체크해 주세요.");
                } else {
                    txtCheck.setText("이미 사용중인 학번입니다.");
                }
            }
        });
        dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registName = dialogView.findViewById(R.id.registName);
                registYear = dialogView.findViewById(R.id.registYear);
                registMajor = dialogView.findViewById(R.id.registMajor);
                registScore = dialogView.findViewById(R.id.registScore);
                registFemale = dialogView.findViewById(R.id.registFemale);
                registMale = dialogView.findViewById(R.id.registMale);

                String sname = registName.getText().toString();
                String strYear = registYear.getText().toString();

                String gender = "";
                if (registMale.isChecked()) {
                    gender = registMale.getText().toString();
                } else if (registFemale.isChecked()) {
                    gender = registFemale.getText().toString();
                }
                Log.d("mytag", sno);
                String major = registMajor.getText().toString();
                String strScore = registScore.getText().toString();
                boolean result = checkValue(sno, sname, strYear, gender, major, strScore);
                if (result == true) {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    int syear = Integer.parseInt(strYear);
                    int score = Integer.parseInt(strScore);
                    db.execSQL("insert into tbl_student (sno, sname, syear, gender, major, score)" +
                            "   values('" + sno + "','" + sname + "'," + syear + ",'" + gender + "','" + major + "'," + score + ")");
                    db.close();
                    // 입력 완료 했을 시 다이어로그 띄우기
                    resultDialog();
                }
            }
        });
        dialog.setNegativeButton("취소", null);
        dialog.show();
    }

    // 중복값 체크
    private boolean checkSno(String sno) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select count(*) from tbl_student" +
                "     where sno = '" + sno + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            // count값이 0일때 -> 등록가능
            int count = cursor.getInt(0);
            if (count == 0) {
                return true;
            }
        }
        return false;
    }

    // 등록 시 값 비었을 때 띄울 다이어로그
    private void showEmptyDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("알림");
        dialog.setMessage("모든 항목에 정확하게 입력해 주세요.");
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog();
            }
        });
        dialog.show();
    }

    // 등록 완료 시 다이어로그
    private void resultDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("알림");
        dialog.setMessage("신규등록 완료");
        dialog.setPositiveButton("확인", null);
        dialog.show();
    }

    // 유효성 체크
    public boolean checkValue(String sno, String sname, String strYear, String gender, String major, String strScore) {
        // 빈 값일 때
        if (sno.equals("") || sname.equals("") || gender.equals("") || major.equals("")
                || strYear.equals("") || strScore.equals("")) {
            showEmptyDialog();
            return false;
        }
        // 요구조건에 맞지않는 값을 넣었을 때
        try {
            int syear = Integer.parseInt(strYear);
            if (!(syear >= 1 && syear <= 4)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("알림");
                dialog.setMessage("학년은 1~4의 범위로 입력해 주세요.");
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialog();
                    }
                });
                dialog.show();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("알림");
            dialog.setMessage("학년은 숫자로 입력해 주세요.");
            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDialog();
                }
            });
            dialog.show();
            return false;
        }
        try {
            int score = Integer.parseInt(strScore);
            if (!(score >= 0 && score <= 100)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("알림");
                dialog.setMessage("점수는1~100의 범위로 입력해 주세요.");
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialog();
                    }
                });
                dialog.show();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("알림");
            dialog.setMessage("점수는 숫자로 입력해 주세요.");
            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDialog();
                }
            });
            dialog.show();
            return false;
        }
        return true;
    }

}