package com.example.greeandao;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greendao.gen.UserDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    UserDao userDao;
    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.butAdd)
    Button mButAdd;
    @BindView(R.id.detele)
    ImageView mDetele;
    @BindView(R.id.rv)
    RecyclerView mRv;
    List<User> userlist = new ArrayList<>();
    private String[] mCustomItems = new String[]{"修改姓名", "删除","取消"};
    private String string;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        initDbHelp();
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void initData() {
        userlist = GreenDaoManager.getInstance().getDaosession().getUserDao().queryBuilder().list();
        adapter = new MyAdapter(this, userlist);
        mRv.setAdapter(adapter);
        adapter.setOnclickSpflAdpter(new MyAdapter.OnClickfl() {
            @Override
            public void onClickxq(final int position) {

                //创建对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请选择：");
                builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText inputServer = new EditText(MainActivity.this);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("修改姓名").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                                    .setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    string = inputServer.getText().toString();
                                    updateUser(userlist.get(position).getName(),string);

                                }
                            });
                            builder.show();


                        } else if (which == 1) {
                            deleteUser(userlist.get(position).getName());

                        }
                    }
                });
                builder.show();

            }
        } );
    }


    //单个删除
    private void deleteUser(String name){
        UserDao userDao=GreenDaoManager.getInstance().getDaosession().getUserDao();
        User findUser=userDao .queryBuilder().where(UserDao.Properties.Name.eq(name)).build().unique();
        if(findUser!=null){
            userDao.deleteByKey(findUser.getId());
            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();

            inLoadAll(userDao.queryBuilder().list());
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(MainActivity.this, "没有找到该用户信息", Toast.LENGTH_LONG).show();
        }
    }

    //修改
    private void updateUser(String name,String newName){
        User findUser=userDao.queryBuilder()
                .where(UserDao.Properties.Name.eq(name)).build().unique();
        if(findUser!=null){
            findUser.setName(newName);
            GreenDaoManager.getInstance().getDaosession().getUserDao().update(findUser);
            Toast.makeText(MainActivity.this, newName+"修改成功", Toast.LENGTH_LONG).show();
            inLoadAll(userDao.queryBuilder().list());
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(MainActivity.this, newName+"没有找到该用户信息", Toast.LENGTH_LONG).show();
        }

    }


    private void AddInster(Long id, String name) {
        User user = new User(id, name);
        userDao.insert(user);
        mEtName.setText("");
        Toast.makeText(MainActivity.this, user.getName() + "", Toast.LENGTH_LONG).show();
        inLoadAll(userDao.queryBuilder().list());
    }

    private void inLoadAll(List<User> list) {
        userlist.clear();
        userlist.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /*初始化数据库相关*/
    private void initDbHelp() {
        userDao = GreenDaoManager.getInstance().getDaosession().getUserDao();
    }


    private boolean isNotEmpty(String s) {
        if (s != null && !s.equals("") || s.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmpty(String s) {
        if (isNotEmpty(s)) {
            return false;
        } else {
            return true;
        }
    }

    private void DeteleInto(String name) {
        User userid = userDao.queryBuilder().where(UserDao.Properties.Name.eq(name)).build().unique();
        if (userid != null) {
            userDao.deleteByKey(userid.getId());
            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();

            inLoadAll(userDao.queryBuilder().list());
        } else {
            Toast.makeText(MainActivity.this, "没有找到该用户", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.etName, R.id.butAdd, R.id.detele, R.id.rv})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.etName:
                break;
            case R.id.butAdd:
                AddInster(null, mEtName.getText().toString());
                break;
            case R.id.detele:
                /*删除指定数据*/
                userDao.deleteAll();
                inLoadAll(userDao.queryBuilder().list());
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                break;
            case R.id.rv:
                break;
        }
    }
}
