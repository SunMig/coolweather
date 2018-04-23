package util;

import android.text.TextUtils;
import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;

public class Utility {
	//�����ʹ�����������ص�ʡ������
	public synchronized static boolean handleProvincesresponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p:allProvinces){
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//���������������ݴ洢��Province��
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	
	//�����ʹ����ط��������ص��м�����
	
	public synchronized static boolean handleCitiesresponse(CoolWeatherDB coolWeatherDB,String response,int ProvinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String p:allCities){
					String[] array=p.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					//���������������ݴ洢��City��
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	//�����ʹ�����������ص��ؼ�����
	

	public synchronized static boolean handleCountiesresponse(CoolWeatherDB coolWeatherDB,String response,int CityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for(String p:allCounties){
					String[] array=p.split("\\|");
					County county=new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(CityId);
					//���������������ݴ洢��Province��
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}
