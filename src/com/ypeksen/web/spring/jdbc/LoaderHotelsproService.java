package com.ypeksen.web.spring.jdbc;

import java.sql.Types;
import java.util.Date;

import com.ypeksen.web.spring.controllers.UserRegistration;
import com.ypeksen.web.spring.model.UserApplicationExtendedModel;

public class LoaderHotelsproService extends BaseService {
private ProcedureDefinition procaddHotelContent19;


@Override
public void afterPropertiesSet() throws Exception {

//procaddHotelContent19 = new ProcedureDefinition(getSchemaName(), "PKG_ENG_HPRO_LOADER.ADD_HOTEL_HPRO_V1", new TripAdvisorReportObjectMapper(), Types.NUMERIC);

}

public UserRegistration addUser(UserApplicationExtendedModel appuser, String code, String destination, String name,
String country, String master, double star, double latitude, double longitude, String type, 
String address, String zipCode, String currency, String image,String hotelInformation,
String checkInFrom, String checkOutTo, double numberOfRoom, Date update) {
	UserRegistration result = (UserRegistration) executeProcedureSingle(procaddHotelContent19, appuser.getUserId(), appuser.getApplicationId(), 
code, destination, name, country, master, star, latitude, longitude, type, address, zipCode, currency, image, hotelInformation, 
checkInFrom, checkOutTo, numberOfRoom, update);
return result;
}

}
