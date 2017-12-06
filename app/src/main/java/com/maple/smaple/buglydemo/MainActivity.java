package com.maple.smaple.buglydemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * 补丁包步骤：
 * 1，tinker-support  ->  buildTinkerPatchRelease
 * 2,登陆Bugly 打开当前产品，-> 应用升级 ->热更新 ->发布新补丁包
 * 3,补丁文件 选择文件  路i经 xxx\app\build\outputs\patch\release  选择： patch_signed_7zip.apk
 * 4,选择下发范围： 根据情况选择  一般为全量设备 -> 立即下发
 * 5，下载补丁包或有延迟需要等待片刻，然后杀死应用并重启。
 */
public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Context ctx;
    private static final int REQUEST_CODE_PERMISSION_MULTI = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;

        // 在Activity：
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_MULTI)
                .permission(Permission.PHONE,Permission.STORAGE)
                .callback(this)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(ctx, rationale).show();
                    }
                })
                .start();

        mTextView = findViewById(R.id.textView);
        mTextView.setText("修复成功啦！");
    }


    @PermissionYes(REQUEST_CODE_PERMISSION_MULTI)
    private void getMultiYes(@NonNull List<String> grantedPermissions) {
        Toast.makeText(ctx,"权限申请成功！" , Toast.LENGTH_SHORT).show();


    }

    @PermissionNo(REQUEST_CODE_PERMISSION_MULTI)
    private void getMultiNo(@NonNull List<String> deniedPermissions) {
        Toast.makeText(ctx, "权限申请失败!", Toast.LENGTH_SHORT).show();
    }
}
