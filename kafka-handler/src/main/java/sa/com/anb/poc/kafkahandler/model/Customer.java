package sa.com.anb.poc.kafkahandler.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Customer {

	private Timestamp lastModify;
	private String login;
	private Date birthDate;
	private String gender;
	private String preferredChannel;
	private String nationality;
	private Address address;
	private List<Permission> permissions;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String language;
	private boolean pointExpirationDisabled;
	private String mainIdentifier;
	private String mobileId;
	private String mobileSystem;
	private List<Item> attributes;
	private String blacklistReason;
	private String websiteAccess;
	private String loanEnabled;
	private String childEnrolmentEnabled;
	private String redemptionEnabled;
	private Date householdLockDate;
	private String salutation;

	@Data
	@AllArgsConstructor
	@ToString
	private static class Address {
		private String street;
		private String house;
		private String apartmentNumber;
		private String postalCode;
		private String city;
		private String country;
		private String region;
		private String email;
		private String phone;
		private String mobile;
		private String fax;
		private String additionalInfo;

	}

	@Data
	@AllArgsConstructor
	@ToString
	private static class Permission {
		private boolean sms;
		private boolean push;
		private boolean email;
		private boolean post;
		private boolean phone;
		private boolean info;
		private boolean adverts;
		private boolean facebook;
		private boolean segmentation;
		private boolean removeAvatar;
		private boolean fbMessenger;

	}

	@Data
	@AllArgsConstructor
	@ToString
	private static class Item {
		private String code;
		private String value;
	}

}
