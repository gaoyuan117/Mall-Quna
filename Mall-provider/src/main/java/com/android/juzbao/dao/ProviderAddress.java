
package com.android.juzbao.dao;

import com.server.api.model.Area;
import com.server.api.model.City;
import com.server.api.model.Province;
import com.server.api.service.AddressService;
import com.android.zcomponent.http.HttpDataLoader;

public class ProviderAddress
{

	public static void sendCmdQueryProvince(HttpDataLoader httpDataLoader)
	{
		AddressService.GetProvincesRequest request =
				new AddressService.GetProvincesRequest();

		httpDataLoader.doPostProcess(request, Province.class);
	}

	public static void sendCmdQueryCity(HttpDataLoader httpDataLoader,
			String provinceId)
	{
		AddressService.GetCitiesRequest request =
				new AddressService.GetCitiesRequest();
		request.ProvinceId = provinceId;
		httpDataLoader.doPostProcess(request, City.class);
	}

	public static void sendCmdQueryArea(HttpDataLoader httpDataLoader,
			String cityId)
	{
		AddressService.GetAreasRequest request =
				new AddressService.GetAreasRequest();
		request.CityId = cityId;
		httpDataLoader.doPostProcess(request, Area.class);
	}
}
