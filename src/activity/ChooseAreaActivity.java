package activity;

import java.util.ArrayList;
import java.util.List;

import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

import com.example.coolweather.R;

import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTY=2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList=new ArrayList<String>();
	
	//ʡ�б�
	private List<Province> provinceList;
	//���б�
	private List<City> CityList;
	//���б�
	private List<County> CountyList;
	//ѡ�е�ʡ��
	private Province selectedProvince;
	//ѡ�еĳ���
	private City selectedCity;
	//��ǰѡ�еļ���
	private int currentLevel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
						selectedProvince = provinceList.get(index);
						queryCities();
			} else if (currentLevel == LEVEL_CITY) {
					selectedCity = CityList.get(index);
					queryCounties();
					}
				}
			});
			queryProvinces(); // ����ʡ������
		}
	//��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ	
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
				dataList.clear();
				for (Province province : provinceList) {
					dataList.add(province.getProvinceName());
		}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
			}
		}
	//��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	private void queryCities() {
		CityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (CityList.size() > 0) {
				dataList.clear();
		for (City city : CityList) {
				dataList.add(city.getCityName());
		}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				titleText.setText(selectedProvince.getProvinceName());
				currentLevel = LEVEL_CITY;
			} else {
				queryFromServer(selectedProvince.getProvinceCode(), "city");
			}
		}
	//��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	private void queryCounties() {
		CountyList = coolWeatherDB.loadCounties(selectedCity.getId());
		if (CountyList.size() > 0) {
				dataList.clear();
		for (County county : CountyList) {
				dataList.add(county.getCountyName());
		}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				titleText.setText(selectedCity.getCityName());
				currentLevel = LEVEL_COUNTY;
		} else {
				queryFromServer(selectedCity.getCityCode(), "county");
			}
		}
	
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code +".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
		@Override
		public void onFinish(String response) {
			boolean result = false;
			if ("province".equals(type)) {
				result = Utility.handleProvincesresponse(coolWeatherDB,response);
		} else if ("city".equals(type)) {
				result = Utility.handleCitiesresponse(coolWeatherDB,response, selectedProvince.getId());
		} else if ("county".equals(type)) {
				result = Utility.handleCountiesresponse(coolWeatherDB,response, selectedCity.getId());
		}
		if (result) {
		// ͨ��runOnUiThread()�����ص����̴߳����߼�
		runOnUiThread(new Runnable() {
				@Override
				public void run() {
					closeProgressDialog();
					if ("province".equals(type)) {
						queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
										queryCounties();
												}
											}
							});
					}
		}
	public void onError(Exception e) {
		// ͨ��runOnUiThread()�����ص����̴߳����߼�
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,"����ʧ��", Toast.LENGTH_SHORT).show();
												}
										});
								}
						});
		}
	private void showProgressDialog() {
		if (progressDialog == null) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("���ڼ���...");
				progressDialog.setCanceledOnTouchOutside(false);
		}
				progressDialog.show();
		}
	
	private void closeProgressDialog() {
		if (progressDialog != null) {
				progressDialog.dismiss();
				}
		}
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
				queryCities();
		} else if (currentLevel == LEVEL_CITY) {
				queryProvinces();
		} else {
				finish();
			}
		}
	
	
}
