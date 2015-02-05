package br.ufpe.cin.autoweka;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import static br.ufpe.cin.autoweka.Classify.classifySingleUnlabeledInstance;

public class MainActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public static class PlaceholderFragment extends Fragment {
		private TextView classification;
		private Context context;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		
			context = rootView.getContext().getApplicationContext();
			
			classification = (TextView) rootView.findViewById(R.id.classification);
			
			return rootView;
		}
		
		@Override
		public void onResume() {
			super.onResume();
			
			try {
                new Classify().run(context);
				/*String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();
				String fileSeparator = System.getProperty("file.separator");
				String model = String.format("%s%sAutoWeka%s%s", sdCard, fileSeparator, fileSeparator, "trained.0.model");
				String attributeSelection = String.format("%s%sAutoWeka%s%s", sdCard, fileSeparator, fileSeparator, "trained.0.attributeselection");
				
				classification.setText("Exists: " + new File(attributeSelection).exists());
				
				String classification1 = classifySingleUnlabeledInstance(new MusicInstance(80.207, 0.49482558127419307, 0.031195728086989782, 0.09090272012346592, 0.33858609898708314), model, attributeSelection);
				String classification2 = classifySingleUnlabeledInstance(new MusicInstance(140.008, 0.436097823900063, 0.03222021207000658, 0.1057052477494161, 0.6061255397822767), model, attributeSelection);
				
				classification.setText(classification1 + classification2);*/
			} catch (IOException e) {
				classification.setText(e.getClass().getSimpleName() + ": " + e.getMessage());
			}
		}
	}
}