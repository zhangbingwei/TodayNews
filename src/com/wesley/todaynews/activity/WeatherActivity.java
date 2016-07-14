package com.wesley.todaynews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wesley.todaynews.R;
import com.wesley.todaynews.domain.WeatherData;
import com.wesley.todaynews.domain.WeatherData.Today;

/**
 * 今日天气页面
 * 
 * @author zhangbingwei
 * 
 */
public class WeatherActivity extends Activity {

	// 天气对象和数据
	private Today todayWeather;

	private TextView tvDate;
	private TextView tvTemp;
	private TextView tvWeather;
	private TextView tvWind;
	private TextView tvTravel;
	private TextView tvExe;
	private TextView tvDress;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);

		tvDate = (TextView) findViewById(R.id.tv_date);
		tvTemp = (TextView) findViewById(R.id.tv_temp);
		tvWeather = (TextView) findViewById(R.id.tv_weather);
		tvWind = (TextView) findViewById(R.id.tv_wind);
		tvTravel = (TextView) findViewById(R.id.tv_travel);
		tvExe = (TextView) findViewById(R.id.tv_exe);
		tvDress = (TextView) findViewById(R.id.tv_dress);

		Today todayWeather = (Today) getIntent().getSerializableExtra(
				"todayWeather");

		initData();
	}

	public void initData() {
		url = "http://v.juhe.cn/weather/index?cityname=%E4%B8%8A%E6%B5%B7&dtype=&format=&key=7b1909cd875493f88e6661439bda838a";
		getWeatherDataFromServer();

	}

	// 获取服务器上的天气数据
	public void getWeatherDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseWeatherData(result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(WeatherActivity.this, "网络不可用，请检查网络",
						Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
		});
	}

	public void parseWeatherData(String result) {
		Gson gson = new Gson();
		WeatherData weatherData = gson.fromJson(result, WeatherData.class);
		todayWeather = weatherData.result.today;

		tvDate.setText(todayWeather.date_y + "  " + todayWeather.week);
		tvTemp.setText("气温：" + todayWeather.temperature);
		tvWeather.setText("天气：" + todayWeather.weather);
		tvWind.setText("风力：" + todayWeather.wind);
		tvTravel.setText("旅行：" + todayWeather.travel_index);
		tvExe.setText("锻炼：" + todayWeather.exercise_index);
		tvDress.setText(todayWeather.dressing_advice);

	}

}
