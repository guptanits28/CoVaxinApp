# CoVaxinApp
Check available hospitals in Gautam Budhha Nagar (can be customized see below Point 6)

1. In SendEmail.java change the below line. Generate this app password via: https://support.google.com/accounts/answer/185833#
return new PasswordAuthentication("xxx@gmail.com", "xxxx");

2. Add Recipients list on SendEmail.java
message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("xx@gmail.com,xxxxx@gmail.com"));

3. Below line in VaccinationAvailability.java run the cron job every 15 minutes.
withSchedule(CronScheduleBuilder.cronSchedule("0 0/15 * * * ?"))

4. Below line in AvailableVaccinationCentresJob specifies to check data till one month from now. 
LocalDate endDate = startDate.plusMonths(1);

5. Run as simple java application in any Java IDE (Intellij, Eclipse)

6. Below line in AvailableVaccinationCentresJob specifies the API url exposed by govt. district_id=650 is for Gautam Budhha Nagar
URL url = new URL("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=650&date=" + strDate);

API to get state id:
https://cdn-api.co-vin.in/api/v2/admin/location/states
API to get district id from state
https://cdn-api.co-vin.in/api/v2/admin/location/districts/34

7. This application is based on 
https://apisetu.gov.in/public/marketplace/api/cowin#/Metadata%20APIs/districts
