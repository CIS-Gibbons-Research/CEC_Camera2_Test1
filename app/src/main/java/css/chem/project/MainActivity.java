package css.chem.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AndroidCameraApi";
    private Button takePictureButton;
    private TextureView textureView;
    TextView tvFocus, tvISO, tvExposure;
    SeekBar seekFocus, seekISO, seekExposure;

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    SensorCam2 cam2;
    //SensorCam3 cam3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(textureListener);

        cam2 = new SensorCam2(this, textureView);
        //cam3 = new SensorCam3(this, textureView);

        setupSeekBars();

        takePictureButton = (Button) findViewById(R.id.takePhotoButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam2.takePicture();
                //cam3.takePicture();
            }
        });
    }

    private void setupSeekBars() {
        tvFocus = findViewById(R.id.textViewFocus);
        tvISO = findViewById(R.id.textViewISO);
        tvExposure = findViewById(R.id.textViewExposure);

        seekFocus = findViewById(R.id.seekBarFocus);
        seekFocus.setMin(0);
        seekFocus.setMax(200);
        seekFocus.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("CIS4444", "Focus seekbar set to "+progress);
                Float focus = cam2.setFocus(progress);
                //Float focus = cam3.setFocus(progress);
                tvFocus.setText(focus.toString());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekFocus.setProgress(100);

        seekISO = findViewById(R.id.seekBarISO);
        seekISO.setMin(100);
        seekISO.setMax(1000);
        seekISO.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("CIS4444", "ISO seekbar set to "+progress);
                Integer iso = cam2.setISO(progress);
                //Integer iso = cam3.setISO(progress);
                tvISO.setText(iso.toString());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekISO.setProgress(500);

        seekExposure = findViewById(R.id.seekBarExposure);
        seekExposure.setMin(1);
        seekExposure.setMax(1000);
        seekExposure.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("CIS4444", "Exposure seekbar set to "+progress);
                Double exposure = cam2.setExposureTime(progress)/1_000_000.0;
                //Double exposure = cam3.setExposureTime(progress)/1_000_000.0;
                tvExposure.setText(exposure.toString());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekExposure.setProgress(300);

    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            //cam3.openCamera(textureView.getWidth(), textureView.getHeight());
            cam2.openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        //cam3.startBackgroundThread();;
        cam2.startBackgroundThread();
        if (textureView.isAvailable()) {
            cam2.openCamera();
            //cam3.openCamera(textureView.getWidth(), textureView.getHeight());

        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        cam2.stopBackgroundThread();
        //cam3.onPause();
        super.onPause();
    }
}