package com.tlc.explosion;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final ExplosionView tExplosion = new ExplosionView(this);
		
		TextView mTxt = (TextView)findViewById(R.id.txt);
		mTxt.setClickable(true);
		mTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tExplosion.explode(v);
			}
		});
		
		ImageView mImg = (ImageView)findViewById(R.id.img);
		mImg.setClickable(true);
		mImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tExplosion.explode(v);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
