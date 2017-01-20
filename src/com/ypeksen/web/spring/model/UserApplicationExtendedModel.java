package com.ypeksen.web.spring.model;

import java.io.Serializable;

public class UserApplicationExtendedModel implements Serializable {
private static final long serialVersionUID = -2755506881671366112L;

private int userId;
private int agencyId;
private int agencyOfficeId;
private int applicationId;
private int sourceId;
private int couponId;

private String agencyCurrency;
private int msaId;
private boolean b2c;
private int officeCountryId;
private String officeCountry;
private boolean dpack;
private boolean userLogged;

private int[] agencyGroupIds;
private int[] msaGroupIds;

// private String[] agencyGroups;

public UserApplicationExtendedModel() {

}

public UserApplicationExtendedModel(int userId, int agencyId, int applicationId) {
super();
this.userId = userId;
this.agencyId = agencyId;
this.applicationId = applicationId;
}

public String toString() {
StringBuffer buf = new StringBuffer();

buf.append("app:" + applicationId);
buf.append(" agencyId:" + agencyId);
buf.append(" userId:" + userId);
buf.append(" sourceId:" + sourceId);

return buf.toString();
}

public int getUserId() {
return userId;
}

public void setUserId(int userId) {
this.userId = userId;
}

public int getApplicationId() {
return applicationId;
}

public void setApplicationId(int applicationId) {
this.applicationId = applicationId;
}

public int getAgencyId() {
return agencyId;
}

public void setAgencyId(int agencyId) {
this.agencyId = agencyId;
}

public int getSourceId() {
return sourceId;
}

public void setSourceId(int sourceId) {
this.sourceId = sourceId;
}

public int getCouponId() {
return couponId;
}

public void setCouponId(int couponId) {
this.couponId = couponId;
}

public String getAgencyCurrency() {
return agencyCurrency;
}

public void setAgencyCurrency(String agencyCurrency) {
this.agencyCurrency = agencyCurrency;
}

public int getMsaId() {
return msaId;
}

public void setMsaId(int msaId) {
this.msaId = msaId;
}

public int getAgencyOfficeId() {
return agencyOfficeId;
}

public void setAgencyOfficeId(int agencyOfficeId) {
this.agencyOfficeId = agencyOfficeId;
}

public boolean isB2c() {
return b2c;
}

public void setB2c(boolean b2c) {
this.b2c = b2c;
}

public boolean isDpack() {
return dpack;
}

public void setDpack(boolean dpack) {
this.dpack = dpack;
}

public int[] getAgencyGroupIds() {
return agencyGroupIds;
}

public void setAgencyGroupIds(int[] agencyGroupIds) {
this.agencyGroupIds = agencyGroupIds;
}

public boolean isUserLogged() {
return userLogged;
}

public void setUserLogged(boolean userLogged) {
this.userLogged = userLogged;
}

public int getOfficeCountryId() {
return officeCountryId;
}

public void setOfficeCountryId(int officeCountryId) {
this.officeCountryId = officeCountryId;
}

public String getOfficeCountry() {
return officeCountry;
}

public void setOfficeCountry(String officeCountry) {
this.officeCountry = officeCountry;
}

public int[] getMsaGroupIds() {
return msaGroupIds;
}

public void setMsaGroupIds(int[] msaGroupIds) {
this.msaGroupIds = msaGroupIds;
}

}
