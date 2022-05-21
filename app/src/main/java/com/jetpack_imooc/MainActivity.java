package com.jetpack_imooc;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jetpack.libcommon.utils.StatusBar;
import com.jetpack_imooc.model.Destination;
import com.jetpack_imooc.model.User;
import com.jetpack_imooc.ui.login.UserManager;
import com.jetpack_imooc.utils.AppConfig;
import com.jetpack_imooc.utils.NavGraphBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Iterator;
import java.util.Map;

/**
 * 主页面入口
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController navController;

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        StatusBar.fitSystemBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(fragment);
        //  NavigationUI.setupActionBarWithNavController(this, navController);
        // NavigationUI.setupWithNavController(navView,navController);
        NavGraphBuilder.build(this, navController, fragment.getId());
        navView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Map<String, Destination> sDesConfig = AppConfig.getSDesConfig();
        Iterator<Map.Entry<String, Destination>> iterator = sDesConfig.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Destination> entry = iterator.next();
            Destination value = entry.getValue();
            if (value != null && !UserManager.get().isLogin() && value.getNeedLogin() && value.getId() == item.getItemId()) {
                UserManager.get().login(this).observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        navView.setSelectedItemId(item.getItemId());
                    }
                });
                return false;
            }

        }


        navController.navigate(item.getItemId());
        return !TextUtils.isEmpty(item.getTitle());
    }
}