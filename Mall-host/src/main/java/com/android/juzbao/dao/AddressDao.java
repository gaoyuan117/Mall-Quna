
package com.android.juzbao.dao;

import com.server.api.model.AddressJZB;
import com.server.api.model.Area;
import com.server.api.model.City;
import com.server.api.model.CommonReturn;
import com.server.api.model.Province;
import com.server.api.service.AddressService;
import com.android.zcomponent.http.HttpDataLoader;

public class AddressDao
{

	public static void sendCmdQueryJZBAddress(HttpDataLoader httpDataLoader)
	{
		AddressService.JZBAddressRequest request =
				new AddressService.JZBAddressRequest();

		httpDataLoader.doPostProcess(request, AddressJZB.class);
	}
	
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

	public static void sendCmdQueryMyAddress(HttpDataLoader httpDataLoader)
	{
		AddressService.GetReceiverAddressRequest request =
				new AddressService.GetReceiverAddressRequest();
		httpDataLoader.doPostProcess(request,
				com.server.api.model.Address.class);
	}
	
	public static void sendCmdDelAddress(HttpDataLoader httpDataLoader, String id)
	{
		AddressService.DelReceiverAddressRequest request =
				new AddressService.DelReceiverAddressRequest();
		request.Id = id;
		httpDataLoader.doPostProcess(request,
				com.server.api.model.CommonReturn.class);
	}

	public static void sendCmdQueryAddAddress(HttpDataLoader httpDataLoader,
			String mobile, String realName, String provinceId, String cityId,
			String areaId, String address, boolean isDefault)
	{

		AddressService.AddReceiverAddressRequest request =
				new AddressService.AddReceiverAddressRequest();
		request.Address = address;
		request.AreaId = areaId;
		request.CityId = cityId;
		request.Mobile = mobile;
		request.ProvinceId = provinceId;
		request.Realname = realName;
		if (isDefault)
		{
			request.IsDefault = "1";	
		}
		else
		{
			request.IsDefault = "0";
		}
		httpDataLoader.doPostProcess(request,
				CommonReturn.class);
	}

	public static void sendCmdQueryModifyAddress(HttpDataLoader httpDataLoader, int id,
			String mobile, String realName, String provinceId, String cityId,
			String areaId, String address, boolean isDefault)
	{
		AddressService.EditReceiverAddressRequest request =
				new AddressService.EditReceiverAddressRequest();
		request.Id = id;
		request.Address = address;
		request.AreaId = areaId;
		request.CityId = cityId;
		request.Mobile = mobile;
		request.ProvinceId = provinceId;
		request.Realname = realName;
		
		if (isDefault)
		{
			request.IsDefault = "1";	
		}
		else
		{
			request.IsDefault = "0";
		}
		httpDataLoader.doPostProcess(request,
				CommonReturn.class);
	}
}
