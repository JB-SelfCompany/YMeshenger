package d.d.meshenger

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import d.d.meshenger.MainService.MainBinder

// the main view with tabs
class MainActivity : MeshengerActivity(), ServiceConnection {
    internal var binder: MainBinder? = null
    private lateinit var viewPager: ViewPager2

    private val PERM_REQUEST_CODE_DRAW_OVERLAYS = 1234

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        permissionToDrawOverlays();
        MainService.start(this)

        viewPager = findViewById(R.id.container)
        viewPager.adapter = ViewPagerFragmentAdapter(this)

        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.title_contacts)
                else -> getString(R.string.title_events)
            }
        }.attach()

        // ask for audio recording permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 2)
        }
    }

    private fun permissionToDrawOverlays() {
        if (Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                    "package:$packageName"))
                startActivityForResult(intent, PERM_REQUEST_CODE_DRAW_OVERLAYS)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERM_REQUEST_CODE_DRAW_OVERLAYS) {
            if (Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
                if (!Settings.canDrawOverlays(this)) {
                    // ADD UI FOR USER TO KNOW THAT UI for SYSTEM_ALERT_WINDOW permission was not granted earlier...
                }
            }
        }
    }

    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
        Log.d(this, "OnServiceConnected")
        binder = iBinder as MainBinder

        // call it here because EventListFragment.onResume is triggered twice
        try {
            binder!!.pingContacts()
        } catch (e: Exception) {
        }
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
        Log.d(this, "OnServiceDisconnected")
        binder = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(this, "onOptionsItemSelected")
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            R.id.action_backup -> {
                startActivity(Intent(this, BackupActivity::class.java))
            }
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
            R.id.action_exit -> {
                MainService.stop(this)
                if (Build.VERSION.SDK_INT >= 21) {
                    finishAndRemoveTask()
                } else {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        Log.d(this, "OnResume")
        super.onResume()
        bindService(Intent(this, MainService::class.java), this, BIND_AUTO_CREATE)
    }

    override fun onPause() {
        Log.d(this, "onPause")
        super.onPause()
        unbindService(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.permission_mic, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(this, "onCreateOptionsMenu")
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    class ViewPagerFragmentAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm)
    {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            if (position == 0) {
                return ContactListFragment()
            } else {
                return EventListFragment()
            }
        }
    }
}
